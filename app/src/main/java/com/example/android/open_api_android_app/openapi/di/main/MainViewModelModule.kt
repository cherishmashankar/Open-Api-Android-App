package com.example.android.open_api_android_app.openapi.di.main

import androidx.lifecycle.ViewModel
import com.example.android.open_api_android_app.openapi.di.ViewModelKey
import com.example.android.open_api_android_app.openapi.ui.main.account.AccountViewModel
import com.example.android.open_api_android_app.openapi.ui.main.blog.BlogViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    abstract fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BlogViewModel::class)
    abstract fun bindBlogViewModel(blogViewModel: BlogViewModel): ViewModel


}