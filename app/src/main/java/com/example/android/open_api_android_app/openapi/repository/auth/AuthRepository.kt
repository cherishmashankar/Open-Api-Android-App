package com.example.android.open_api_android_app.openapi.repository.auth;

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.example.android.open_api_android_app.openapi.api.auth.OpenApiAuthService
import com.example.android.open_api_android_app.openapi.api.auth.network_responses.LoginResponse
import com.example.android.open_api_android_app.openapi.api.auth.network_responses.RegistrationResponse
import com.example.android.open_api_android_app.openapi.models.AccountProperties
import com.example.android.open_api_android_app.openapi.models.AuthToken
import com.example.android.open_api_android_app.openapi.persistence.AccountPropertiesDao
import com.example.android.open_api_android_app.openapi.persistence.AuthTokenDao
import com.example.android.open_api_android_app.openapi.repository.JobManager
import com.example.android.open_api_android_app.openapi.repository.NetworkBoundResource
import com.example.android.open_api_android_app.openapi.session.SessionManager
import com.example.android.open_api_android_app.openapi.ui.Data
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.Response
import com.example.android.open_api_android_app.openapi.ui.ResponseType
import com.example.android.open_api_android_app.openapi.ui.auth.state.AuthViewState
import com.example.android.open_api_android_app.openapi.ui.auth.state.LoginFields
import com.example.android.open_api_android_app.openapi.ui.auth.state.RegistrationFields
import com.example.android.open_api_android_app.openapi.util.AbsentLiveData
import com.example.android.open_api_android_app.openapi.util.ErrorHandling.Companion.ERROR_SAVE_AUTH_TOKEN
import com.example.android.open_api_android_app.openapi.util.ErrorHandling.Companion.ERROR_UNKNOWN
import com.example.android.open_api_android_app.openapi.util.ErrorHandling.Companion.GENERIC_AUTH_ERROR
import com.example.android.open_api_android_app.openapi.util.GenericApiResponse
import com.example.android.open_api_android_app.openapi.util.GenericApiResponse.ApiSuccessResponse
import com.example.android.open_api_android_app.openapi.util.PreferenceKeys
import com.example.android.open_api_android_app.openapi.util.SuccessHandling.Companion.RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE
import kotlinx.coroutines.Job
import javax.inject.Inject

class AuthRepository
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val accountPropertiesDao: AccountPropertiesDao,
    val openApiAuthService: OpenApiAuthService,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val sharedPrefsEditor: SharedPreferences.Editor

): JobManager("AuthRepository"){
    private  val TAG = "AppDebug"


    fun attemptLogin(email: String, password: String): LiveData<DataState<AuthViewState>>{

        val loginFieldErrors = LoginFields(email, password).isValidForLogin()
        if(!loginFieldErrors.equals(LoginFields.LoginError.none())){
            return returnErrorResponse(loginFieldErrors, ResponseType.Dialog())

        }

        return object: NetworkBoundResource<LoginResponse,Any, AuthViewState>(
            sessionManager.isConnectedToInternet(),
        true,
            true,
        false
        ){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response}")

                //Incorrect Login credentials count as a 200 response from server, so neeed to handle that
                if (response.body.response.equals(GENERIC_AUTH_ERROR)){
                    return onErrorReturn(response.body.errorMessage, true, false)
                    }


                // dont care about results. Just insert if it doesnt exist b/c foreign key relationship
                //with AUthTioken table
                accountPropertiesDao.insertOrIgnore(
                    AccountProperties(
                        response.body.pk,
                    response.body.email,
                    ""
                    )
                )

                //will return -1 if failure
                val result = authTokenDao.insert(
                    AuthToken(
                        response.body.pk,
                        response.body.token
                    )
                )

                if(result < 0){
                    return onCompleteJob(
                        DataState.error(
                            Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                        )
                    )
                }

                saveAuthenticatedUserToPrefs(email)

                    onCompleteJob(
                            DataState.data(
                                data = AuthViewState(
                                    authToken = AuthToken(response.body.pk, response.body.token)
                                )
                            )

                    )
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return openApiAuthService.login(email, password)
            }

            override fun setJob(job: Job) {
               addJob("attemptLogin",job)
            }

            //Ignore
            override suspend fun createCacheRequestAndReturn() {
                TODO("Not yet implemented")
            }

            //Ignore
            override fun loadFromCache(): LiveData<AuthViewState> {
                TODO("Not yet implemented")
            }
            //Ignore
            override suspend fun updateLocalDb(cachedObject: Any?) {
                TODO("Not yet implemented")
            }

        }.asLiveData()

    }

    fun attemptRegistration(
        email: String,username: String,   password: String, confirmPassword: String ):
            LiveData<DataState<AuthViewState>>{
        val registrationFieldsErrors = RegistrationFields(email, username, password, confirmPassword).isValidForRegistration()

        if(!registrationFieldsErrors.equals(RegistrationFields.RegistrationError.none())){
            return returnErrorResponse(registrationFieldsErrors, ResponseType.Dialog())}

            return object: NetworkBoundResource<RegistrationResponse, Any, AuthViewState>(
                sessionManager.isConnectedToInternet(),
                true,
                true,
            false
            ){
                override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RegistrationResponse>) {
                    Log.d(TAG, "handleApiSuccessResponse: ${response}")

                    //Incorrect Login credentials count as a 200 response from server, so neeed to handle that
                    if (response.body.response.equals(GENERIC_AUTH_ERROR)){
                        return onErrorReturn(response.body.errorMessage, true, false)
                    }

                    // dont care about results. Just insert if it doesnt exist b/c foreign key relationship
                    //with AUthTioken table
                    accountPropertiesDao.insertOrIgnore(
                        AccountProperties(
                            response.body.pk,
                            response.body.email,
                            ""
                        )
                    )

                    //will return -1 if failure
                    val result = authTokenDao.insert(
                        AuthToken(
                            response.body.pk,
                            response.body.token
                        )
                    )

                    if(result < 0){
                        return onCompleteJob(
                            DataState.error(
                                Response(ERROR_SAVE_AUTH_TOKEN, ResponseType.Dialog())
                            )
                        )
                    }

                    saveAuthenticatedUserToPrefs(email)



                    onCompleteJob(
                        DataState.data(
                            data = AuthViewState(
                                authToken = AuthToken(response.body.pk, response.body.token)
                            )
                        )

                    )
                }

                override fun createCall(): LiveData<GenericApiResponse<RegistrationResponse>> {
                    return openApiAuthService.register(email, username, password, confirmPassword)
                }

                override fun setJob(job: Job) {
                   addJob("attemptRegistration",job)
                }

                //Ignore
                override suspend fun createCacheRequestAndReturn() {
                    TODO("Not yet implemented")
                }
                //Ignore
                override fun loadFromCache(): LiveData<AuthViewState> {
                    TODO("Not yet implemented")
                }
                //Ignore
                override suspend fun updateLocalDb(cachedObject: Any?) {
                    TODO("Not yet implemented")
                }

            }.asLiveData()




    }

    fun checkPreviousAuthUser(): LiveData<DataState<AuthViewState>>{
        val previousAuthUserEmail: String? =
            sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER,null)
        if(previousAuthUserEmail.isNullOrBlank()){
            Log.d(TAG, "checkPreviousAuthUser: No previoulsly authenticated user found...")
            return returnNoTokenFound()
        }

       return object: NetworkBoundResource<Void, Any, AuthViewState>(
            sessionManager.isConnectedToInternet(),
        false,
           false,
           false
        ){
           override suspend fun createCacheRequestAndReturn() {

            accountPropertiesDao.searchByEmail(previousAuthUserEmail).let { accountProperties ->
                Log.d(TAG, "createCacheRequestAndReturn: searching for token:  $accountProperties")

                accountProperties?.let {
                    if(accountProperties.pk > -1){
                        authTokenDao.searchByPK(accountProperties.pk).let{authToken ->
                            if(authToken != null){
                                onCompleteJob(
                                    DataState.data(
                                        data = AuthViewState(
                                            authToken = authToken
                                        )
                                    )
                                )
                                return
                            }

                        }
                    }
                }
                Log.d(TAG, "createCacheRequestAndReturn: AuthToken not found...")
                onCompleteJob(
                    DataState.data(
                        data = null,
                        response = Response(
                            RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE,
                            ResponseType.None()
                        )
                    )
                )
}
           }

           //not used in this case, no network request made
           override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<Void>) {

           }

            //not used in this case
           override fun createCall(): LiveData<GenericApiResponse<Void>> {
            return AbsentLiveData.create()
           }

           override fun setJob(job: Job) {
               addJob("checkPreviousAuthUser", job)

           }

            //Ignore
           override fun loadFromCache(): LiveData<AuthViewState> {
               TODO("Not yet implemented")
           }

            //Ignore
           override suspend fun updateLocalDb(cachedObject: Any?) {
               TODO("Not yet implemented")
           }
       }.asLiveData()
    }

    private fun returnNoTokenFound(): LiveData<DataState<AuthViewState>> {
    return object: LiveData<DataState<AuthViewState>>(){
        override fun onActive() {
            super.onActive()
            value = DataState.data(
                data = null,
                response = Response(RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE, ResponseType.None())

            )
        }
    }
    }

    private fun saveAuthenticatedUserToPrefs(email: String) {
        sharedPrefsEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER,email)
        sharedPrefsEditor.apply()

    }



    private fun returnErrorResponse(errorMessage: String, responseType: ResponseType): LiveData<DataState<AuthViewState>> {
        Log.d(TAG, "returnErrorResponse: ${errorMessage}")
        return object: LiveData<DataState<AuthViewState>>(){
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    Response(
                        errorMessage,
                        responseType
                    )
                )
            }
        }
    }



}

