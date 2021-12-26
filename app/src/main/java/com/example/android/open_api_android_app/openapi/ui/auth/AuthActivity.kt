package com.example.android.open_api_android_app.openapi.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.openapi.ui.BaseActivity
import com.example.android.open_api_android_app.openapi.ui.ResponseType
import com.example.android.open_api_android_app.openapi.ui.auth.state.AuthStateEvent
import com.example.android.open_api_android_app.openapi.ui.main.MainActivity
import com.example.android.open_api_android_app.openapi.viewmodels.ViewModelProviderFactory
import com.google.android.material.appbar.AppBarLayout
import javax.inject.Inject


class AuthActivity : BaseActivity(), NavController.OnDestinationChangedListener{

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory



    lateinit var viewModel: AuthViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this,providerFactory).get(AuthViewModel::class.java)
        findNavController(R.id.auth_nav_host_fragment).addOnDestinationChangedListener(this)
        subscribeObserver()

    }

    override fun onResume() {
        super.onResume()
        checkPreviousAuthUser()
    }

    override fun expandAppbar() {
        //findViewById<AppBarLayout>(R.id.app_bar)
    }

    fun subscribeObserver(){
        viewModel.dataState.observe(this, Observer { dataState ->
            onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        it.authToken?.let {
                            Log.d(TAG, "AuthActivity: subscribeObserver, DataState  ${it} ")
                            viewModel.setAuthToken(it)
                        }
                    }
                }
            }



        })

        viewModel.viewState.observe(this, Observer {
            it.authToken?.let {
                sessionManager.login(it)
            }
        })


        sessionManager.cachedToken.observe(this, Observer { authToken ->
            Log.d(TAG, "AuthActivity: subscribeObserver: AuthToken: ${authToken}" )
            if(authToken != null && authToken.account_pk != -1 && authToken.token != null ){
                navMainActivity()
            }
        })
    }

    fun checkPreviousAuthUser(){
        viewModel.setStateEvent(AuthStateEvent.CheckPreviousAUthEvent())
    }

    private fun navMainActivity() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        viewModel.cancelActiveJob()
    }

    override fun displayProgressBar(bool: Boolean) {
        val progress_bar = findViewById<ProgressBar>(R.id.progress_bar)
        if(bool){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.INVISIBLE
        }
    }
}