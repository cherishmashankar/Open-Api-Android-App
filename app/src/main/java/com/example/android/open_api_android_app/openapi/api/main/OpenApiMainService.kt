package com.example.android.open_api_android_app.openapi.api.main

import androidx.lifecycle.LiveData
import com.example.android.open_api_android_app.openapi.models.AccountProperties
import com.example.android.open_api_android_app.openapi.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface OpenApiMainService {

    @GET("account/properties")
    fun getAccountProperties(
        @Header("Authorization") authorization: String
    ): LiveData<GenericApiResponse<AccountProperties>>
}