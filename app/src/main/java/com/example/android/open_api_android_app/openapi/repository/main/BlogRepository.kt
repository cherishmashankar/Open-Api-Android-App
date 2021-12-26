package com.example.android.open_api_android_app.openapi.repository.main

import com.example.android.open_api_android_app.openapi.api.main.OpenApiMainService
import com.example.android.open_api_android_app.openapi.persistence.BlogPostDao
import com.example.android.open_api_android_app.openapi.repository.JobManager
import com.example.android.open_api_android_app.openapi.session.SessionManager
import javax.inject.Inject

class BlogRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("BlogRepository") {
    private  val TAG = "BlogRepository"
}