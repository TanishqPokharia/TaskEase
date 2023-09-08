package com.tanishq.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.tanishq.todolist.R
import com.tanishq.todolist.databinding.FragmentSignupBinding


class SignupFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        registerEvents()
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
    }

    private fun registerEvents() {
        binding.imageViewnextbtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val pass = binding.pass.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                binding.progressbar.visibility = View.VISIBLE
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(
                    OnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Registered Succssfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            navController.navigate(R.id.action_signupFragment_to_homeFragment)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                it.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        binding.progressbar.visibility = View.GONE
                    })
            }else{
                Toast.makeText(
                    requireContext(),
                    "Error: Empty fields",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        binding.textView2.setOnClickListener{
            navController.navigate(R.id.action_signupFragment_to_signInFragment)
        }
    }
}


