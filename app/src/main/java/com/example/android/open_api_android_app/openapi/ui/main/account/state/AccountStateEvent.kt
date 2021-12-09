package com.example.android.open_api_android_app.openapi.ui.main.account.state

sealed class AccountStateEvent {
    class GetAccountPropertiesEvent: AccountStateEvent()

    data class UpdateAccountPropertiesEvent(
        val email: String,
        val username: String
    ): AccountStateEvent()

    data class ChangePasswordEvent(
        val currentPassword: String,
        val newPassword: String,
        val confirmNewPassword: String
    ): AccountStateEvent()

    class None: AccountStateEvent()

}