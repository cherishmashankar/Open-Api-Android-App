package com.example.android.open_api_android_app.openapi.ui.main.create_blog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.openapi.ui.main.create_blog.BaseCreateBlogFragment

class CreateBlogFragment : BaseCreateBlogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_blog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}