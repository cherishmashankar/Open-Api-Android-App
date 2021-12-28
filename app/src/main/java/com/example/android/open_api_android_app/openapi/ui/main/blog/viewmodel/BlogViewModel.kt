package com.example.android.open_api_android_app.openapi.ui.main.blog.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.bumptech.glide.RequestManager
import com.example.android.open_api_android_app.openapi.models.BlogPost
import com.example.android.open_api_android_app.openapi.persistence.BlogQueryUtils
import com.example.android.open_api_android_app.openapi.repository.main.BlogRepository
import com.example.android.open_api_android_app.openapi.session.SessionManager
import com.example.android.open_api_android_app.openapi.ui.BaseViewModel
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.Loading
import com.example.android.open_api_android_app.openapi.ui.main.blog.state.BlogStateEvent
import com.example.android.open_api_android_app.openapi.ui.main.blog.state.BlogViewState
import com.example.android.open_api_android_app.openapi.util.AbsentLiveData
import com.example.android.open_api_android_app.openapi.util.PreferenceKeys.Companion.BLOG_FILTER
import com.example.android.open_api_android_app.openapi.util.PreferenceKeys.Companion.BLOG_ORDER
import javax.inject.Inject

class BlogViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val blogRepository: BlogRepository,
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
): BaseViewModel<BlogStateEvent, BlogViewState>() {

    init {
        setBlogFilter(
            sharedPreferences.getString(
                BLOG_FILTER,
                BlogQueryUtils.BLOG_FILTER_DATE_UPDATED
            )
        )
        sharedPreferences.getString(
            BLOG_ORDER,
            BlogQueryUtils.BLOG_ORDER_ASC
        )
    }



    override fun handleStateEvent(stateEvent: BlogStateEvent): LiveData<DataState<BlogViewState>> {

      when(stateEvent){
          is BlogStateEvent.BlogSearchEvent -> {
              return sessionManager.cachedToken.value?.let{authToken ->
                  blogRepository.searchBlogPosts(
                     authToken =  authToken,
                     query = getSearchQuery(),
                      filterAndOrder = getOrder() + getFilter(),
                      page = getPage()
                  )
              }?: AbsentLiveData.create()
          }

          is BlogStateEvent.None -> {
              return object: LiveData<DataState<BlogViewState >>(){
                  override fun onActive() {
                      super.onActive()
                      value = DataState(
                          null,
                          Loading(false),
                          null
                      )
                  }
              }
          }

          is BlogStateEvent.CheckAuthorOfBlogPost -> {
              return AbsentLiveData.create()
          }
      }


    }

    override fun initNewViewState(): BlogViewState{
       return BlogViewState()
    }



   fun cancelActiveJobs(){
        blogRepository.cancelActiveJobs()
        handlePendingData()
    }

    private fun handlePendingData() {
       setStateEvent(BlogStateEvent.None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJobs()
    }
    fun saveFilterOptions(filter: String, order: String){
        editor.putString(BLOG_FILTER, filter)
        editor.apply()

        editor.putString(BLOG_ORDER, order)
        editor.apply()
    }


}