package com.example.android.open_api_android_app.openapi.ui.main.account

import androidx.lifecycle.LiveData
import com.example.android.open_api_android_app.openapi.models.AccountProperties
import com.example.android.open_api_android_app.openapi.repository.main.AccountRepository
import com.example.android.open_api_android_app.openapi.session.SessionManager
import com.example.android.open_api_android_app.openapi.ui.BaseViewModel
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.main.account.state.AccountStateEvent
import com.example.android.open_api_android_app.openapi.ui.main.account.state.AccountViewState
import com.example.android.open_api_android_app.openapi.util.AbsentLiveData
import javax.inject.Inject

class AccountViewModel
@Inject
constructor(
    val sessionManager: SessionManager,
    val accountRepository: AccountRepository
): BaseViewModel<AccountStateEvent, AccountViewState>() {

    override fun handleStateEvent(stateEvent: AccountStateEvent): LiveData<DataState<AccountViewState>> {
        when(stateEvent){
            is AccountStateEvent.GetAccountPropertiesEvent ->{
                return sessionManager.cachedToken.value?.let { authToken ->
                    accountRepository.getAccountProperties(authToken)
                }?: AbsentLiveData.create()
            }
            is AccountStateEvent.UpdateAccountPropertiesEvent ->{
                return AbsentLiveData.create()
            }
            is AccountStateEvent.ChangePasswordEvent ->{
                return AbsentLiveData.create()
            }
            is AccountStateEvent.None ->{
                return AbsentLiveData.create()
            }

        }
    }

    override fun initNewViewState(): AccountViewState {
      return AccountViewState()
    }

    fun setAccountPropertiesData(accountProperties: AccountProperties){
        val update = getCurrentViewStateOrNew()
        if(update.accountProperties == accountProperties){
            return
        }
        update.accountProperties = accountProperties
        _viewState.value = update
    }

    fun logout(){
        sessionManager.logout()
    }
}