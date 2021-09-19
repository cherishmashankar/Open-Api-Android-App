package com.example.android.open_api_android_app.openapi


import com.example.android.open_api_android_app.openapi.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class BaseApplication: DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
//      return DaggerAppComponent.builder().application(this).build()
        return DaggerAppComponent.builder().application(this).build()

    }


}