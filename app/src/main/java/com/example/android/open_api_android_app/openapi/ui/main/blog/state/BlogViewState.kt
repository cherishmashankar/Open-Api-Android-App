package com.example.android.open_api_android_app.openapi.ui.main.blog.state

import android.view.View
import com.example.android.open_api_android_app.openapi.models.BlogPost
import com.example.android.open_api_android_app.openapi.persistence.BlogQueryUtils.Companion.BLOG_ORDER_ASC
import com.example.android.open_api_android_app.openapi.ui.main.blog.ViewBlogFragment
import com.example.android.open_api_android_app.openapi.persistence.BlogQueryUtils.Companion.ORDER_BY_ASC_DATE_UPDATED
import com.example.android.open_api_android_app.openapi.persistence.BlogQueryUtils.Companion.ORDER_BY_ASC_USERNAME
import com.example.android.open_api_android_app.openapi.persistence.BlogQueryUtils.Companion.ORDER_BY_DESC_DATE_UPDATED
import com.example.android.open_api_android_app.openapi.persistence.BlogQueryUtils.Companion.ORDER_BY_DESC_USERNAME

data class BlogViewState (

    //UpdateBlog

    // BlogFragment vars
    var blogFields: BlogFields = BlogFields(),

    //ViewBloagFragment vars
    var viewBlogFields: ViewBlogFields = ViewBlogFields()

)
{
    data class BlogFields(
        var blogList: List<BlogPost> = ArrayList<BlogPost>(),
        var searchQuery: String = "",
        var page: Int = 1,
        var isQueryInProgress: Boolean = false,
        var isQueryExhausted: Boolean = false,
        var filter: String = ORDER_BY_ASC_DATE_UPDATED,
        var order: String = BLOG_ORDER_ASC
    )

    data class ViewBlogFields(
        var blogPost: BlogPost? = null,
        var isAuthorOfBlogPost: Boolean = false
    )


}