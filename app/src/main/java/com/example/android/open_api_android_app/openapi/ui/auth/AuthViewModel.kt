package com.example.android.open_api_android_app.openapi.ui.auth

import androidx.lifecycle.ViewModel
import com.example.android.open_api_android_app.openapi.repository.auth.AuthRepository
import javax.inject.Inject

class AuthViewModel
@Inject
constructor(
    authRepository: AuthRepository
): ViewModel() {
}