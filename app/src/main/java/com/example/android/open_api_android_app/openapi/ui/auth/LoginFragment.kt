package com.example.android.open_api_android_app.openapi.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.databinding.FragmentLoginBinding
import com.example.android.open_api_android_app.openapi.ui.auth.state.LoginFields
import com.example.android.open_api_android_app.openapi.util.GenericApiResponse


class LoginFragment : BaseAuthFragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "LoginFragment: ${viewModel.hashCode()}")
        subscribeObserver()

    }


    fun subscribeObserver(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer {
            it.loginFields?.let {
                it.login_email?.let{binding.inputEmail.setText(it)}
                it.login_password?.let{binding.inputPassword.setText(it)}

            } })
    }
    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.setLoginFields(
            LoginFields(
                binding.inputEmail.text.toString(),
                binding.inputPassword.text.toString()
            )
        )
    }
    }


