package com.example.android.open_api_android_app.openapi.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.example.android.open_api_android_app.openapi.api.GenericResponse
import com.example.android.open_api_android_app.openapi.api.main.OpenApiMainService
import com.example.android.open_api_android_app.openapi.models.AccountProperties
import com.example.android.open_api_android_app.openapi.models.AuthToken
import com.example.android.open_api_android_app.openapi.persistence.AccountPropertiesDao
import com.example.android.open_api_android_app.openapi.repository.JobManager
import com.example.android.open_api_android_app.openapi.repository.NetworkBoundResource
import com.example.android.open_api_android_app.openapi.session.SessionManager
import com.example.android.open_api_android_app.openapi.ui.Data
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.Response
import com.example.android.open_api_android_app.openapi.ui.ResponseType
import com.example.android.open_api_android_app.openapi.ui.main.account.AccountViewModel
import com.example.android.open_api_android_app.openapi.ui.main.account.state.AccountViewState
import com.example.android.open_api_android_app.openapi.util.AbsentLiveData
import com.example.android.open_api_android_app.openapi.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AccountRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val accountPropertiesDao: AccountPropertiesDao,
    val sessionManager: SessionManager
):JobManager("AccountRepository") {
    private val TAG: String = "AppDebug"



    fun getAccountProperties(authToken: AuthToken): LiveData<DataState<AccountViewState>> {
        return object :
            NetworkBoundResource<AccountProperties, AccountProperties, AccountViewState>(
                sessionManager.isConnectedToInternet(),
                true,
                false,
                true
            ) {
            override suspend fun createCacheRequestAndReturn() {
                withContext(Main) {
                    //finish by viewing the db cache

                    result.addSource(loadFromCache()) { viewState ->
                        onCompleteJob(
                            DataState.data(
                                data = viewState,
                                response = null
                            )
                        )

                    }

                }
            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<AccountProperties>) {

                updateLocalDb(response.body)

                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<AccountProperties>> {
                return openApiMainService
                    .getAccountProperties(
                        "Token ${authToken.token}"
                    )
            }

            override fun setJob(job: Job) {
               addJob("getAccountProperties",job)
            }

            override fun loadFromCache(): LiveData<AccountViewState> {
                return accountPropertiesDao.searchByPK(authToken.account_pk!!)
                    .switchMap {
                        object : LiveData<AccountViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = AccountViewState(it)
                            }
                        }
                    }
            }

            override suspend fun updateLocalDb(cachedObject: AccountProperties?) {
                cachedObject?.let {
                    accountPropertiesDao.updateAccountProperties(
                        cachedObject.pk,
                        cachedObject.email,
                        cachedObject.username
                    )
                }
            }

        }.asLiveData()
    }


    fun saveAccountProperties(
        authToken: AuthToken, accountProperties: AccountProperties
    ): LiveData<DataState<AccountViewState>> {

        return object : NetworkBoundResource<GenericResponse, Any, AccountViewState>(
            isNetworkAvailable = sessionManager.isConnectedToInternet(),
            isNetworkRequest = true,
            true,
            shouldLoadFromCache = false
        ){


            //Not applicable
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<GenericResponse>) {
                Log.e(TAG, "handleApiSuccessResponse: " + response )
                updateLocalDb(null)
                withContext(Main){
                    //finish with success response
                    onCompleteJob(
                        DataState.data(
                            data = null,
                            response = Response(response.body.response, ResponseType.Toast())
                        )

                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {

                return openApiMainService.saveAccountProperties(
                    "Token ${authToken.token}",
                    accountProperties.email,
                    accountProperties.username

                )

            }

            override fun setJob(job: Job) {
                addJob("saveAccountProperties",job)
            }

            //Not used in this case
            override fun loadFromCache(): LiveData<AccountViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cachedObject: Any?) {
                return accountPropertiesDao.updateAccountProperties(
                    accountProperties.pk,
                    accountProperties.email,
                    accountProperties.username
                )

            }

        }.asLiveData()
    }

    fun updatePassword(
        authToken: AuthToken,
        currentPassword: String,
        newPassword: String,
        confirmPassword: String
    ): LiveData<DataState<AccountViewState>>{
        return object: NetworkBoundResource<GenericResponse, Any, AccountViewState>(
            sessionManager.isConnectedToInternet(),
            true,
            true,
            false
        ){

            //Not applicable
            override suspend fun createCacheRequestAndReturn() {

            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<GenericResponse>) {

                withContext(Main){
                    //finish with success response
                    onCompleteJob(
                        DataState.data(
                            data=null,
                            response = Response(response.body.response, ResponseType.Dialog())
                        )
                    )

                }

            }

            override fun createCall(): LiveData<GenericApiResponse<GenericResponse>> {
                Log.e(TAG, "onViewCreated: 44 ")
                return openApiMainService.updatePassword(
                    "Token ${authToken.token!!}",
                    currentPassword,
                    newPassword,
                    confirmPassword
                )
            }

            override fun setJob(job: Job) {
                addJob("updatePassword",job)
            }

            //not applicable in this case
            override fun loadFromCache(): LiveData<AccountViewState> {
                return AbsentLiveData.create()
            }

            //not applicable in this case
            override suspend fun updateLocalDb(cachedObject: Any?) {

            }

        }.asLiveData()
    }



}
