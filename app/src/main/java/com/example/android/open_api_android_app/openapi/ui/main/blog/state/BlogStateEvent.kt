package com.example.android.open_api_android_app.openapi.ui.main.blog.state

sealed class BlogStateEvent {

    class BlogSearchEvent : BlogStateEvent()

    class None: BlogStateEvent()

    class CheckAuthorOfBlogPost: BlogStateEvent()
}