package com.example.android.open_api_android_app.openapi.di.auth



import com.example.android.open_api_android_app.openapi.api.auth.OpenApiAuthService
import com.example.android.open_api_android_app.openapi.persistence.AccountPropertiesDao
import com.example.android.open_api_android_app.openapi.persistence.AuthTokenDao
import com.example.android.open_api_android_app.openapi.repository.auth.AuthRepository
import com.example.android.open_api_android_app.openapi.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class AuthModule{

    // TEMPORARY
    @AuthScope
    @Provides
    fun provideFakeApiService(): OpenApiAuthService {
        return Retrofit.Builder()
            .baseUrl("https://open-api.xyz")
            .build()
            .create(OpenApiAuthService::class.java)
    }

    @AuthScope
    @Provides
    fun provideAuthRepository(
        sessionManager: SessionManager,
        authTokenDao: AuthTokenDao,
        accountPropertiesDao: AccountPropertiesDao,
        openApiAuthService: OpenApiAuthService
        ): AuthRepository {
        return AuthRepository(
            authTokenDao,
            accountPropertiesDao,
            openApiAuthService,
            sessionManager
        )
    }

}