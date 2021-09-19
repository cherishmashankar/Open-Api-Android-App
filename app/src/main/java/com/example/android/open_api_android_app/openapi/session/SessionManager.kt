package com.example.android.open_api_android_app.openapi.session

import android.app.Application
import com.example.android.open_api_android_app.openapi.persistence.AuthTokenDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
){
}