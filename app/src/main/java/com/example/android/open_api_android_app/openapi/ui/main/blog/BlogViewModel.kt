package com.example.android.open_api_android_app.openapi.ui.main.blog

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.bumptech.glide.RequestManager
import com.example.android.open_api_android_app.openapi.models.BlogPost
import com.example.android.open_api_android_app.openapi.repository.main.BlogRepository
import com.example.android.open_api_android_app.openapi.session.SessionManager
import com.example.android.open_api_android_app.openapi.ui.BaseViewModel
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.main.blog.state.BlogStateEvent
import com.example.android.open_api_android_app.openapi.ui.main.blog.state.BlogViewState
import com.example.android.open_api_android_app.openapi.util.AbsentLiveData
import javax.inject.Inject

class BlogViewModel
@Inject
constructor(
    private val sessionManager: SessionManager,
    private val blogRepository: BlogRepository,
    private val sharedPreferences: SharedPreferences,
    private val requestManager: RequestManager
): BaseViewModel<BlogStateEvent, BlogViewState>() {

    override fun handleStateEvent(stateEvent: BlogStateEvent): LiveData<DataState<BlogViewState>> {
      when(stateEvent){
          is BlogStateEvent.BlogSearchEvent -> {
              return AbsentLiveData.create()
          }

          is BlogStateEvent.None -> {
              return AbsentLiveData.create()
          }
      }


    }

    override fun initNewViewState(): BlogViewState{
       return BlogViewState()
    }

    fun setQuery(query: String){
        val update = getCurrentViewStateOrNew()
        if(query.equals(update.blogFields.searchQuery)){
            return
        }
        update.blogFields.searchQuery = query
        _viewState.value = update

    }


    fun setQueryListData(blogList: List<BlogPost>){
        val update = getCurrentViewStateOrNew()

        update.blogFields.blogList = blogList
        _viewState.value = update
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


}