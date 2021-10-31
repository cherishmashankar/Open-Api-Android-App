package com.example.android.open_api_android_app.openapi.ui

import com.example.android.open_api_android_app.openapi.session.SessionManager
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity: DaggerAppCompatActivity(){

    public val TAG: String = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager


}











