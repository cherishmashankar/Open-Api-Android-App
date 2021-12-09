package com.example.android.open_api_android_app.openapi.ui.main.account

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.databinding.FragmentAccountBinding
import com.example.android.open_api_android_app.databinding.FragmentBlogBinding
import com.example.android.open_api_android_app.openapi.models.AccountProperties
import com.example.android.open_api_android_app.openapi.session.SessionManager
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.main.account.state.AccountStateEvent
import javax.inject.Inject

class AccountFragment : BaseAccountFragment(){

//    @Inject
//    lateinit var sessionManager: SessionManager
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.changePassword.setOnClickListener {
            findNavController().navigate(R.id.action_accountFragment_to_changePasswordFragment)
        }
        binding.logoutButton.setOnClickListener {
            //sessionManager.logout()
            viewModel.logout()
        }

        subscribeObservers()
    }

    private fun setAccountDataFields(accountProperties:  AccountProperties){
        binding.email.setText(accountProperties.email)
        binding.username.setText(accountProperties.username)

    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(
            AccountStateEvent.GetAccountPropertiesEvent()
        )
    }
    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            dataState?.let {
                it.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { viewState ->
                            viewState.accountProperties?.let { accountProperties ->
                                Log.d(TAG, "AccountFragment, DataState: ${accountProperties}")
                                viewModel.setAccountPropertiesData(accountProperties)

                            }

                        }

                    }

                }
            }

        })
    viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
        viewState?.let {
            it.accountProperties?.let {
                Log.d(TAG, "AccountFragment, ViewState: ${it} ")
                setAccountDataFields(it)
            }
        }

    })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.edit_view_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.edit -> {
                    findNavController().navigate(R.id.action_accountFragment_to_updateAccountFragment)
                    return true
                }
            }

        return super.onOptionsItemSelected(item)
    }
}