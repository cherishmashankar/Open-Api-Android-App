package com.example.android.open_api_android_app.openapi.di.auth



import com.example.android.open_api_android_app.openapi.ui.auth.ForgotPasswordFragment
import com.example.android.open_api_android_app.openapi.ui.auth.LauncherFragment
import com.example.android.open_api_android_app.openapi.ui.auth.LoginFragment
import com.example.android.open_api_android_app.openapi.ui.auth.RegisterFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector()
    abstract fun contributeLauncherFragment(): LauncherFragment

    @ContributesAndroidInjector()
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector()
    abstract fun contributeRegisterFragment(): RegisterFragment

    @ContributesAndroidInjector()
    abstract fun contributeForgotPasswordFragment(): ForgotPasswordFragment

}