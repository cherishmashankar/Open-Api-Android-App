package com.example.android.open_api_android_app.openapi.session

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.open_api_android_app.openapi.models.AuthToken
import com.example.android.open_api_android_app.openapi.persistence.AuthTokenDao
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SessionManager
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
){

    private val TAG = "SessionManager"
    private val _cachedToken = MutableLiveData<AuthToken>()

    val cachedToken: LiveData<AuthToken>
        get() = _cachedToken

    fun login(newValue: AuthToken){
        setValue(newValue)
    }

    fun logout(){
        Log.d(TAG, "logout.. ")
        GlobalScope.launch(IO){
            var errorMessage: String? = null
            try{
                cachedToken.value!!.account_pk?.let{
                    authTokenDao.nullifyToken(it)
                }
            }catch (e: CancellationException){
                Log.e(TAG, "logout: ${e.message}")
                errorMessage = e.message

            }catch (e: Exception){
                Log.e(TAG, "logout: ${e.message}")
                errorMessage = errorMessage + "\n" + e.message
            }finally {
                errorMessage.let {
                    Log.e(TAG, "logout: ${errorMessage}")
                }
                Log.e(TAG, "logout: finally...")
                setValue(null)
            }

        }
        
    }
    
    fun setValue(newValue: AuthToken?){
        GlobalScope.launch(Main) {
            if(_cachedToken.value != newValue){
                _cachedToken.value = newValue
            }
        }
    }
    fun isConnectedToInternet(): Boolean {
        try {
            val command = "ping -c 1 google.com"
            return Runtime.getRuntime().exec(command).waitFor() == 0

        }catch (e: Exception){
            Log.e(TAG, "isConnectedToInternet: ${e.message}")
        }
        return false
    }




}