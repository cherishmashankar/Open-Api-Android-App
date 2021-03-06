package com.example.android.open_api_android_app.openapi.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.android.open_api_android_app.openapi.api.auth.network_responses.LoginResponse
import com.example.android.open_api_android_app.openapi.api.auth.network_responses.RegistrationResponse
import com.example.android.open_api_android_app.openapi.models.AuthToken
import com.example.android.open_api_android_app.openapi.repository.auth.AuthRepository
import com.example.android.open_api_android_app.openapi.ui.BaseViewModel
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.auth.state.AuthStateEvent
import com.example.android.open_api_android_app.openapi.ui.auth.state.AuthStateEvent.*
import com.example.android.open_api_android_app.openapi.ui.auth.state.AuthViewState
import com.example.android.open_api_android_app.openapi.ui.auth.state.LoginFields
import com.example.android.open_api_android_app.openapi.ui.auth.state.RegistrationFields
import com.example.android.open_api_android_app.openapi.util.AbsentLiveData
import com.example.android.open_api_android_app.openapi.util.GenericApiResponse
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
   val authRepository: AuthRepository
): BaseViewModel<AuthStateEvent, AuthViewState>() {



    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        when(stateEvent){

            is LoginAttemptEvent -> {
                return authRepository.attemptLogin(
                    stateEvent.email,
                    stateEvent.password
                )
            }

            is RegisterAttemptEvent -> {
                return authRepository.attemptRegistration(
                    stateEvent.email,
                    stateEvent.username,
                    stateEvent.password,
                    stateEvent.confirm_password
                )
            }

            is CheckPreviousAUthEvent -> {
                return authRepository.checkPreviousAuthUser()
            }
            is None ->{
                return object: LiveData<DataState<AuthViewState>>(){
                    override fun onActive() {
                        super.onActive()
                        value = DataState.data(null,null)
                    }
                }
            }


        }
    }

    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }
    fun setRegistrationFields(registrationFields: RegistrationFields){
        val update = getCurrentViewStateOrNew()
        if(update.registrationFields == registrationFields){
            return
        }
        update.registrationFields = registrationFields
        _viewState.value = update
    }

    fun setLoginFields(loginFields: LoginFields){
        val update = getCurrentViewStateOrNew()
        if(update.loginFields == loginFields){
            return
        }
        update.loginFields = loginFields
        _viewState.value = update
    }

    fun setAuthToken(authToken: AuthToken){
        val update = getCurrentViewStateOrNew()
        if(update.authToken == authToken){
            return
        }
        update.authToken = authToken
        _viewState.value = update
    }

    fun cancelActiveJob(){
        authRepository.cancelActiveJobs()
        handlePendingData()
    }

    fun handlePendingData(){
        setStateEvent(None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJob()
    }




}