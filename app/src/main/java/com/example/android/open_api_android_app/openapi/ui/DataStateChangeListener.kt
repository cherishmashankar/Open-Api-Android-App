package com.example.android.open_api_android_app.openapi.ui

interface DataStateChangeListener {

    fun onDataStateChange(dataState: DataState<*>?)
    fun expandAppbar()

    fun hideSoftKeyboard()
}