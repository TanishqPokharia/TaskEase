package com.tanishq.todolist.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseApiNotAvailableException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.tanishq.todolist.databinding.FragmentHomeBinding
import com.tanishq.todolist.databinding.FragmentPopupBinding
import com.tanishq.todolist.utils.TaskData
import com.tanishq.todolist.utils.ToDoAdapter


class PopupFragment : DialogFragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentPopupBinding
    private lateinit var listener: DialogNextBtnClickListener
    private var db = Firebase.firestore
    private var taskData: TaskData? = null
    fun setListener(listener: DialogNextBtnClickListener) {
        this.listener = listener
    }

    companion object {
        const val tag = "PopupFragment"

        @JvmStatic
        fun newinstance(taskid: String, task: String) = PopupFragment().apply {
            arguments = Bundle().apply {
                putString("taskid", taskid)
                putString("task", task)
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            taskData = TaskData(
                arguments?.getString("taskid").toString(),
                arguments?.getString("task").toString()
            )
            binding.todoEt.setText(taskData?.task)
        }
        registerEvents()

    }

    private fun registerEvents() {

        binding.todoClose.setOnClickListener {
            dismiss()
        }

        binding.addtaskbtn.setOnClickListener {
            val task = binding.todoEt.text.toString()
            if (task.isNotEmpty()){
                if (taskData == null) {
                    listener.saveTask(task, binding.todoEt)
                }
                else{
                    taskData!!.task = task
                    listener.updateTask(taskData!!,task, binding.todoEt)
                }

            }
        }

    }

    //taking input from dialogbox is not a good practice therefore we implement an interface
    //also it is an easy way to interact between home and popup fragment, we can define functions in home and then use them in popup fragment
    interface DialogNextBtnClickListener {
        fun saveTask(task: String, et: TextInputEditText)
        fun updateTask(taskData:TaskData,task: String,et:TextInputEditText)
    }

}