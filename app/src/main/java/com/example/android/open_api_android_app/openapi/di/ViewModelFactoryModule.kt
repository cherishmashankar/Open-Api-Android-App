package com.example.android.open_api_android_app.openapi.di


import androidx.lifecycle.ViewModelProvider
import com.example.android.open_api_android_app.openapi.viewmodels.ViewModelProviderFactory

import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProviderFactory): ViewModelProvider.Factory
}