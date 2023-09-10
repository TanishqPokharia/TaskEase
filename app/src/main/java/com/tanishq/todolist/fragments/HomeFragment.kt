package com.tanishq.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tanishq.todolist.R
import com.tanishq.todolist.databinding.FragmentHomeBinding
import com.tanishq.todolist.utils.TaskData
import com.tanishq.todolist.utils.ToDoAdapter

class HomeFragment : Fragment(), PopupFragment.DialogNextBtnClickListener,
    ToDoAdapter.ToDoAdapterClickInterface {
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    private val taskref = db.collection("TaskEase")
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private var popupFragment: PopupFragment? = null
    private lateinit var adapter: ToDoAdapter
    private lateinit var mutableTaskList: MutableList<TaskData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getFirebaseData()
        registerEvents()
    }

    private fun init(view: View) {
        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        mutableTaskList = mutableListOf()
        adapter = ToDoAdapter(mutableTaskList)
        adapter.setListener(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getFirebaseData() {
        taskref.get().addOnSuccessListener { documents ->
            for (document in documents) {
                val task = TaskData(document.id, document.data["task"].toString())
                mutableTaskList.add(task)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun registerEvents() {
        binding.fabbtn.setOnClickListener {
            //prevent multipleinstances of popup fragment
            if (popupFragment != null)
                childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
            popupFragment = PopupFragment()
            popupFragment!!.setListener(this)
            popupFragment!!.show(
                childFragmentManager, PopupFragment.tag
            )
        }

        binding.logoutbtn.setOnClickListener {
            auth.signOut()
            navController.navigate(R.id.action_homeFragment_to_signInFragment)
            Toast.makeText(requireContext(), "Logged Out", Toast.LENGTH_SHORT).show()
        }

    }

    override fun saveTask(task: String, et: TextInputEditText) {
        val map = hashMapOf(
            "task" to task
        )
        db.collection("TaskEase").document("task${mutableTaskList.size}").set(map)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Task Added", Toast.LENGTH_SHORT).show()
            }
        mutableTaskList.clear()
        getFirebaseData()

    }

    override fun updateTask(taskData: TaskData, task: String, et: TextInputEditText) {
        val map = mapOf(
            "task" to task
        )
        taskref.document(taskData.taskid).update(map).addOnSuccessListener {
            Toast.makeText(requireContext(), "Task updated", Toast.LENGTH_SHORT).show()
        }
        mutableTaskList.clear()
        getFirebaseData()
    }

    override fun onDeleteTaskBtnClicked(taskData: TaskData) {
        taskref.document(taskData.taskid).delete().addOnCompleteListener {
            if (it.isSuccessful) {
                mutableTaskList.clear()
                getFirebaseData()
                Toast.makeText(requireContext(), "Task Deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditTaskBtnClicked(taskData: TaskData) {
        if (popupFragment != null) {
            childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
        }

        popupFragment = PopupFragment.newinstance(taskData.taskid, taskData.task)
        popupFragment!!.setListener(this)
        popupFragment!!.show(childFragmentManager, PopupFragment.tag)
    }

}