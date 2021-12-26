package com.example.android.open_api_android_app.openapi.ui

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import com.example.android.open_api_android_app.openapi.session.SessionManager
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

abstract class BaseActivity: DaggerAppCompatActivity(), DataStateChangeListener{

    public val TAG: String = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onDataStateChange(dataState: DataState<*>?) {
       dataState?.let{
           GlobalScope.launch(Main){
               displayProgressBar(it.loading.isLoading)
               it.error?.let{errorEvent ->
                   handleStateError(errorEvent)
               }
               it.data?.let {
                   it.response?.let{responseEvent ->
                       handleStateResponse(responseEvent)
                   }
               }

           }
       }
    }

    private fun handleStateError(errorEvent: Event<StateError>) {
        errorEvent.getContentIfNotHandled()?.let {
            when(it.response.responseType){
                is ResponseType.Toast ->{
                    it.response.message?.let { message ->
                        displayToast(message)
                    }

                }

                is ResponseType.Dialog ->{
                    it.response.message?.let { message ->
                        displayErrorDialog(message)
                    }

                }

                is ResponseType.None -> {
                    Log.e(TAG, "handleStateError: ${it.response.message}")

                }
            }
        }

    }
    private fun handleStateResponse(event: Event<Response>) {
        event.getContentIfNotHandled()?.let {
            when(it.responseType){
                is ResponseType.Toast ->{
                    it.message?.let { message ->
                        displayToast(message)
                    }

                }

                is ResponseType.Dialog ->{
                    it.message?.let { message ->
                        displaySuccessDialog(message )
                    }

                }

                is ResponseType.None -> {
                    Log.e(TAG, "handleStateError: ${it.message}")

                }
            }
        }

    }

    override fun hideSoftKeyboard() {
        if(currentFocus != null){
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)


        }
    }

    abstract fun displayProgressBar(bool: Boolean)


}











