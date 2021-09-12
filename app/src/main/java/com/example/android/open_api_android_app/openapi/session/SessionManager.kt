package com.example.android.open_api_android_app.openapi.session

import android.app.Application
import com.example.android.open_api_android_app.openapi.persistence.AuthTokenDao

class SessionManager
constructor(
    val authTokenDao: AuthTokenDao,
    val application: Application
){
}