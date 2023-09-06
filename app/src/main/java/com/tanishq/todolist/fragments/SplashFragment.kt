package com.tanishq.todolist.fragments

import android.os.Bundle
import android.os.Handler
import android.view.AttachedSurfaceControl
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tanishq.todolist.R


class SplashFragment : Fragment() {

    lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        Handler().postDelayed({
            navController.navigate(R.id.action_splashFragment_to_homeFragment)
        },3000)

    }


}