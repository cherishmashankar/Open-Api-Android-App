package com.example.android.open_api_android_app.openapi.ui.main.account

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController

import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.databinding.FragmentAccountBinding
import com.example.android.open_api_android_app.databinding.FragmentBlogBinding
import com.example.android.open_api_android_app.openapi.session.SessionManager
import javax.inject.Inject

class AccountFragment : BaseAccountFragment(){

    @Inject
    lateinit var sessionManager: SessionManager
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
            sessionManager.logout()
        }
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