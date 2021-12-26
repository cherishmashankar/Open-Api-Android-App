package com.example.android.open_api_android_app.openapi.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GenericResponse (

    @SerializedName("response")
    @Expose
    var response: String
)