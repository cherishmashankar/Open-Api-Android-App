package com.example.android.open_api_android_app.openapi.ui.main.blog.state

import com.example.android.open_api_android_app.openapi.models.BlogPost

data class BlogViewState (


    //ViewBlog
    //UpdateBlog

    // BlogFragment vars
    var blogFields: BlogFields = BlogFields()
)
{
    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList<BlogPost>(),
        var searchQuery: String = ""
    )


}