package com.example.android.open_api_android_app.openapi.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer

import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.databinding.FragmentAccountBinding
import com.example.android.open_api_android_app.databinding.FragmentUpdateAccountBinding
import com.example.android.open_api_android_app.openapi.models.AccountProperties
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.main.account.BaseAccountFragment
import com.example.android.open_api_android_app.openapi.ui.main.account.state.AccountStateEvent

class UpdateAccountFragment : BaseAccountFragment(){
    private var _binding: FragmentUpdateAccountBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateAccountBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        subscribeObserver()
    }

    private fun subscribeObserver(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            Log.d(TAG, "subscribeObserver: UpdateAccount, DataState: ${DataState}")

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.accountProperties?.let {
                    Log.d(TAG, "UpdateAccountFragment, ViewState: ${viewState}")
                    setAccountDataFields(it)
                }
            }
        })
    } 

    private fun setAccountDataFields(accountProperties: AccountProperties){
        binding.inputEmail?.let {
            it.setText(accountProperties.email)
        }
        binding.inputUsername?.let{
            it.setText(accountProperties.username)
        }

    }

    private fun saveChanges(){
        Log.e(TAG, "handleStateEvent: called 222")
        viewModel.setStateEvent(
            AccountStateEvent.UpdateAccountPropertiesEvent(
                binding.inputEmail.text.toString(),
                binding.inputUsername.text.toString()
            )

        )
        stateChangeListener.hideSoftKeyboard()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.save ->{
                saveChanges()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}