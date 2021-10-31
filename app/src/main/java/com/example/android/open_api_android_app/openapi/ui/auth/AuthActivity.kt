package com.example.android.open_api_android_app.openapi.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.openapi.ui.BaseActivity
import com.example.android.open_api_android_app.openapi.ui.main.MainActivity
import com.example.android.open_api_android_app.openapi.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class AuthActivity : BaseActivity(){

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        viewModel = ViewModelProvider(this,providerFactory).get(AuthViewModel::class.java)
        subscribeObserver()
    }

    fun subscribeObserver(){
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

    private fun navMainActivity() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}