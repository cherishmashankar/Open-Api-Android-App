package com.example.android.open_api_android_app.openapi.di.auth


import androidx.lifecycle.ViewModel
import com.example.android.open_api_android_app.openapi.di.ViewModelKey
import com.example.android.open_api_android_app.openapi.ui.auth.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindAuthViewModel(authViewModel: AuthViewModel): ViewModel

}