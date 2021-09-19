package com.example.android.open_api_android_app.openapi.ui.auth

import android.os.Bundle
import android.util.Log

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.android.open_api_android_app.R


import com.example.android.open_api_android_app.databinding.FragmentLauncherBinding



class LauncherFragment : BaseAuthFragment() {
    private var _binding: FragmentLauncherBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLauncherBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "LauncherFragment: ${viewModel.hashCode()}")

        binding.register.setOnClickListener {
            navRegistration()
        }
        binding.login.setOnClickListener {
            navLogin()
        }
        binding.forgotPassword.setOnClickListener {
            navForgotPassword()
        }
        binding.focusableView.requestFocus()

    }

    private fun navForgotPassword() {
        findNavController().navigate(R.id.action_launcherFragment_to_forgotPasswordFragment)
    }

    private fun navLogin() {
        findNavController().navigate(R.id.action_launcherFragment_to_loginFragment)
    }

    private fun navRegistration() {
        findNavController().navigate(R.id.action_launcherFragment_to_registerFragment)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}