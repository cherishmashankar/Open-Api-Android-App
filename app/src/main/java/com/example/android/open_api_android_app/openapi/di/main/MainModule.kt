package com.example.android.open_api_android_app.openapi.di.main

import com.example.android.open_api_android_app.openapi.api.main.OpenApiMainService
import com.example.android.open_api_android_app.openapi.persistence.AccountPropertiesDao
import com.example.android.open_api_android_app.openapi.persistence.BlogPostDao
import com.example.android.open_api_android_app.openapi.repository.main.AccountRepository
import com.example.android.open_api_android_app.openapi.repository.main.BlogRepository
import com.example.android.open_api_android_app.openapi.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import com.example.android.open_api_android_app.openapi.persistence.AppDataBase

@Module
class MainModule {

    @MainScope
    @Provides
    fun provideOpenApiMainService(retrofitBuilder: Retrofit.Builder): OpenApiMainService{
        return retrofitBuilder
            .build()
            .create(OpenApiMainService::class.java)
    }

    @MainScope
    @Provides
    fun provideAccountRepository(
        openApiMainService: OpenApiMainService,
        accountPropertiesDao: AccountPropertiesDao,
        sessionManager: SessionManager
    ): AccountRepository {
        return AccountRepository(
            openApiMainService,
            accountPropertiesDao,
            sessionManager
        )
    }

    @MainScope
    @Provides
    fun provideBlogPostDao(db: AppDataBase): BlogPostDao {
        return db.getBlogPostDao()
    }

    @MainScope
    @Provides
    fun provideBlogRepository(
        openApiMainService: OpenApiMainService,
        blogPostDao: BlogPostDao,
        sessionManager: SessionManager
    ): BlogRepository {
        return BlogRepository(openApiMainService, blogPostDao, sessionManager)
    }


}