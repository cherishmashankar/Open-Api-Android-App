package com.example.android.open_api_android_app.openapi.di


import com.example.android.open_api_android_app.openapi.di.auth.AuthFragmentBuildersModule
import com.example.android.open_api_android_app.openapi.di.auth.AuthModule
import com.example.android.open_api_android_app.openapi.di.auth.AuthScope
import com.example.android.open_api_android_app.openapi.di.auth.AuthViewModelModule
import com.example.android.open_api_android_app.openapi.ui.auth.AuthActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(
        modules = [AuthModule::class, AuthFragmentBuildersModule::class, AuthViewModelModule::class]
    )
    abstract fun contributeAuthActivity(): AuthActivity

}