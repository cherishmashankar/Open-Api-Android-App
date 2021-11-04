package com.example.android.open_api_android_app.openapi.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.databinding.FragmentLauncherBinding
import com.example.android.open_api_android_app.databinding.FragmentRegisterBinding
import com.example.android.open_api_android_app.openapi.ui.auth.state.AuthStateEvent
import com.example.android.open_api_android_app.openapi.ui.auth.state.RegistrationFields
import com.example.android.open_api_android_app.openapi.util.GenericApiResponse


class RegisterFragment : BaseAuthFragment() {

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
// onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "RegisterFragment: ${viewModel.hashCode()}")
        subscribeObservers()

        binding.registerButton.setOnClickListener {
            register()
        }


    }

    fun subscribeObservers(){
        viewModel.viewState.observe(viewLifecycleOwner, Observer{viewState ->
            viewState.registrationFields?.let {
                it.registration_email?.let{binding.inputEmail.setText(it)}
                it.registration_username?.let{binding.inputUsername.setText(it)}
                it.registration_password?.let{binding.inputPassword.setText(it)}
                it.registration_confirm_password?.let{binding.inputPasswordConfirm.setText(it)}
            }
        })
    }

    fun register(){
        viewModel.setStateEvent(
            AuthStateEvent.RegisterAttemptEvent(
                binding.inputEmail.text.toString(),
                binding.inputUsername.text.toString(),
                binding.inputPassword.text.toString(),
                binding.inputPasswordConfirm.text.toString()

            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRegistrationFields(
            RegistrationFields(
                binding.inputEmail.text.toString(),
                binding.inputUsername.text.toString(),
                binding.inputPassword.text.toString(),
                binding.inputPasswordConfirm.text.toString()
            )
        )
    }


}