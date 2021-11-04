package com.example.android.open_api_android_app.openapi.repository.auth;

import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.example.android.open_api_android_app.openapi.api.auth.OpenApiAuthService
import com.example.android.open_api_android_app.openapi.api.auth.network_responses.LoginResponse
import com.example.android.open_api_android_app.openapi.api.auth.network_responses.RegistrationResponse
import com.example.android.open_api_android_app.openapi.models.AuthToken
import com.example.android.open_api_android_app.openapi.persistence.AccountPropertiesDao
import com.example.android.open_api_android_app.openapi.persistence.AuthTokenDao
import com.example.android.open_api_android_app.openapi.session.SessionManager
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.Response
import com.example.android.open_api_android_app.openapi.ui.ResponseType
import com.example.android.open_api_android_app.openapi.ui.auth.state.AuthViewState
import com.example.android.open_api_android_app.openapi.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.example.android.open_api_android_app.openapi.util.GenericApiResponse
import com.example.android.open_api_android_app.openapi.util.GenericApiResponse.ApiSuccessResponse
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager
){

    fun atttemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>>{
        return openApiAuthService.login(email,password)
            .switchMap { response ->
                object : LiveData<DataState<AuthViewState>>(){
                    override fun onActive() {
                        super.onActive()

                        when(response){
                            is ApiSuccessResponse ->{
                                value = DataState.data(
                                    AuthViewState(
                                        authToken = AuthToken(
                                            response.body.pk,
                                            response.body.token
                                        )
                                    ),
                                    response = null
                                )

                            }
                            is GenericApiResponse.ApiErrorResponse ->{
                                value = DataState.error(
                                    response = Response(
                                        message = response.errorMessage,
                                        responseType = ResponseType.Dialog()
                                    )
                                )

                            }
                            is GenericApiResponse.ApiEmptyResponse ->{
                                value = DataState.error(
                                    response = Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog()
                                    )
                                )

                            }
                        }

                    }
                }
            }
    }

    fun atttemptRegistration(
        email: String,username: String,   password: String, confirmPassword: String ):
            LiveData<DataState<AuthViewState>>{
        return openApiAuthService.register(email,username,password,confirmPassword)
            .switchMap { response ->
                object : LiveData<DataState<AuthViewState>>(){
                    override fun onActive() {
                        super.onActive()

                        when(response){
                            is ApiSuccessResponse ->{
                                value = DataState.data(
                                    AuthViewState(
                                        authToken = AuthToken(
                                            response.body.pk,
                                            response.body.token
                                        )
                                    ),
                                    response = null
                                )

                            }
                            is GenericApiResponse.ApiErrorResponse ->{
                                value = DataState.error(
                                    response = Response(
                                        message = response.errorMessage,
                                        responseType = ResponseType.Dialog()
                                    )
                                )

                            }
                            is GenericApiResponse.ApiEmptyResponse ->{
                                value = DataState.error(
                                    response = Response(
                                        message = ERROR_UNKNOWN,
                                        responseType = ResponseType.Dialog()
                                    )
                                )

                            }
                        }

                    }
                }
            }
    }

}

