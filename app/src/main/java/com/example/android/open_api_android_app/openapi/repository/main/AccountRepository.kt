package com.example.android.open_api_android_app.openapi.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.example.android.open_api_android_app.openapi.api.main.OpenApiMainService
import com.example.android.open_api_android_app.openapi.models.AccountProperties
import com.example.android.open_api_android_app.openapi.models.AuthToken
import com.example.android.open_api_android_app.openapi.persistence.AccountPropertiesDao
import com.example.android.open_api_android_app.openapi.repository.NetworkBoundResource
import com.example.android.open_api_android_app.openapi.session.SessionManager
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.main.account.AccountViewModel
import com.example.android.open_api_android_app.openapi.ui.main.account.state.AccountViewState
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
    ){
    private val TAG: String = "AppDebug"

    private var repositoryJob: Job? = null

    fun getAccountProperties(authToken: AuthToken): LiveData<DataState<AccountViewState>>{
        return object: NetworkBoundResource<AccountProperties, AccountProperties, AccountViewState>(
            sessionManager.isConnectedToInternet(),
            true,
            true
        ){
            override suspend fun createCacheRequestAndReturn() {
               withContext(Main){
                   //finish by viewing the db cache

                   result.addSource(loadFromCache()){ viewState ->
                       onCompleteJob(DataState.data(
                           data = viewState,
                           response = null
                       ))

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
                repositoryJob?.cancel()
                repositoryJob = job
            }

            override fun loadFromCache(): LiveData<AccountViewState> {
                return accountPropertiesDao.searchByPK(authToken.account_pk!!)
                    .switchMap {
                        object: LiveData<AccountViewState>(){
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

    fun cancelActiveJobs(){
        Log.d(TAG, "cancelActiveCancelling on-going jobs... ")
    }

}
