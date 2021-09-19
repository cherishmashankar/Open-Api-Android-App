package com.example.android.open_api_android_app.openapi.di


import android.app.Application
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.openapi.persistence.AccountPropertiesDao
import com.example.android.open_api_android_app.openapi.persistence.AppDataBase
import com.example.android.open_api_android_app.openapi.persistence.AppDataBase.Companion.DATABASE_NAME
import com.example.android.open_api_android_app.openapi.persistence.AuthTokenDao
import dagger.Module

import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule{

    @Singleton
    @Provides
    fun provideAppDb(app: Application): AppDataBase {
        return Room
            .databaseBuilder(app, AppDataBase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration() // get correct db version if schema changed
            .build()
    }

    @Singleton
    @Provides
    fun provideAuthTokenDao(db: AppDataBase): AuthTokenDao {
        return db.getAuthTokenDao()
    }

    @Singleton
    @Provides
    fun provideAccountPropertiesDao(db: AppDataBase): AccountPropertiesDao {
        return db.getAccountPropertiesDao()
    }

    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.drawable.default_image)
            .error(R.drawable.default_image)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(application: Application, requestOptions: RequestOptions): RequestManager {
        return Glide.with(application)
            .setDefaultRequestOptions(requestOptions)
    }

}