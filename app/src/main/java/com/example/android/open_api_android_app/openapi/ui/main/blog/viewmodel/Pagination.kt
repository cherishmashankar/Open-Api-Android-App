package com.example.android.open_api_android_app.openapi.ui.main.blog.viewmodel

import android.util.Log
import com.example.android.open_api_android_app.openapi.ui.main.blog.state.BlogStateEvent
import com.example.android.open_api_android_app.openapi.ui.main.blog.state.BlogViewState

fun BlogViewModel.resetPage(){
    val update = getCurrentViewStateOrNew()
    update.blogFields.page = 1
    setViewState(update)
}

fun BlogViewModel.loadFirstPage() {
    setQueryInProgress(true)
    setQueryExhausted(false)
    resetPage()
    setStateEvent(BlogStateEvent.BlogSearchEvent())
    Log.e(TAG, "BlogViewModel: loadFirstPage: ${getSearchQuery()}")
}

private fun BlogViewModel.incrementPageNumber(){
    val update = getCurrentViewStateOrNew()
    val page = update.copy().blogFields.page // get current page
    update.blogFields.page = page + 1
    setViewState(update)
}

fun BlogViewModel.nextPage(){
    if(!getIsQueryInProgress()
        && !getIsQueryExhausted()){
        Log.d(TAG, "BlogViewModel: Attempting to load next page...")
        incrementPageNumber()
        setQueryInProgress(true)
        setStateEvent(BlogStateEvent.BlogSearchEvent())
    }
}

fun BlogViewModel.handleIncomingBlogListData(viewState: BlogViewState){
    Log.d(TAG, "BlogViewModel, DataState: ${viewState}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryInProgress?: " +
            "${viewState.blogFields.isQueryInProgress}")
    Log.d(TAG, "BlogViewModel, DataState: isQueryExhausted?: " +
            "${viewState.blogFields.isQueryExhausted}")
    setQueryInProgress(viewState.blogFields.isQueryInProgress)
    setQueryExhausted(viewState.blogFields.isQueryExhausted)
    setBlogListData(viewState.blogFields.blogList)
}