package com.example.android.open_api_android_app.openapi.repository.auth;

import com.example.android.open_api_android_app.openapi.api.auth.OpenApiAuthService
import com.example.android.open_api_android_app.openapi.persistence.AccountPropertiesDao
import com.example.android.open_api_android_app.openapi.persistence.AuthTokenDao
import com.example.android.open_api_android_app.openapi.session.SessionManager

class AuthRepository
constructor(
    val authTokenDao: AuthTokenDao,
    accountPropertiesDao: AccountPropertiesDao,
    openApiAuthService: OpenApiAuthService,
    sessionManager: SessionManager
){

}

