package com.example.android.open_api_android_app.openapi.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.databinding.FragmentAccountBinding
import com.example.android.open_api_android_app.databinding.FragmentChangePasswordBinding
import com.example.android.open_api_android_app.openapi.ui.main.account.state.AccountStateEvent
import com.example.android.open_api_android_app.openapi.util.SuccessHandling.Companion.RESPONSE_PASSWORD_UPDATE_SUCCESS
import kotlin.math.log


class ChangePasswordFragment : BaseAccountFragment() {
    private var _binding: FragmentChangePasswordBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObserver()
        Log.e(TAG, "onViewCreated: 11 ")
        binding.updatePasswordButton.setOnClickListener {
            viewModel.setStateEvent(
                AccountStateEvent.ChangePasswordEvent(
                    binding.inputCurrentPassword.text.toString(),
                    binding.inputNewPassword.text.toString(),
                    binding.inputConfirmNewPassword.text.toString()
                )
            )
        }
    }

    private fun subscribeObserver() {
        Log.e(TAG, "onViewCreated: 22 ")
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange((dataState))
            Log.d(TAG, "ChangePasswordFragment, DataState: ${dataState}")
            if (dataState != null){
                    dataState.data?.let { data ->
                        data.response?.let { event ->
                            if (event.peekContent()
                                    .message.equals(RESPONSE_PASSWORD_UPDATE_SUCCESS)
                            ) {
                                stateChangeListener.hideSoftKeyboard()
                                findNavController().popBackStack()
                            }
                        }
                    }

                }
        })
    }
}
