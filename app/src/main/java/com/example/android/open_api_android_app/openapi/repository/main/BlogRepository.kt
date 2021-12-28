package com.example.android.open_api_android_app.openapi.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations.switchMap
import androidx.lifecycle.switchMap
import com.example.android.open_api_android_app.openapi.api.main.OpenApiMainService
import com.example.android.open_api_android_app.openapi.api.main.responses.BlogListSearchResponse
import com.example.android.open_api_android_app.openapi.models.AuthToken
import com.example.android.open_api_android_app.openapi.models.BlogPost
import com.example.android.open_api_android_app.openapi.persistence.AuthTokenDao
import com.example.android.open_api_android_app.openapi.persistence.BlogPostDao
import com.example.android.open_api_android_app.openapi.persistence.returnOrderedBlogQuery
import com.example.android.open_api_android_app.openapi.repository.JobManager
import com.example.android.open_api_android_app.openapi.repository.NetworkBoundResource
import com.example.android.open_api_android_app.openapi.session.SessionManager
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.main.blog.state.BlogViewState
import com.example.android.open_api_android_app.openapi.util.Constants.Companion.PAGINATION_PAGE_SIZE
import com.example.android.open_api_android_app.openapi.util.DateUtils
import com.example.android.open_api_android_app.openapi.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BlogRepository
@Inject
constructor(
    val openApiMainService: OpenApiMainService,
    val blogPostDao: BlogPostDao,
    val sessionManager: SessionManager
): JobManager("BlogRepository") {
    private  val TAG = "BlogRepository"


    fun searchBlogPosts(
        authToken: AuthToken,
        query: String,
        filterAndOrder: String,
        page: Int
        ): LiveData<DataState<BlogViewState>>{
        return object: NetworkBoundResource<BlogListSearchResponse, List<BlogPost>, BlogViewState>(
            sessionManager.isConnectedToInternet(),
            true,
            false,
            true

        ){
            override suspend fun createCacheRequestAndReturn() {
               withContext(Main){
                   //finish by viewing  the db cache
                   result.addSource(loadFromCache()){viewState ->
                       viewState.blogFields.isQueryInProgress = false
                       if(page * PAGINATION_PAGE_SIZE >viewState.blogFields.blogList.size){
                           viewState.blogFields.isQueryExhausted = true
                       }
                       onCompleteJob(DataState.data(viewState, null))
                   }
               }
            }

            override suspend fun handleApiSuccessResponse(response: GenericApiResponse.ApiSuccessResponse<BlogListSearchResponse>) {

               val blogPostList: ArrayList<BlogPost> = ArrayList()
                for(blogPostResponse in response.body.results){
                    blogPostList.add(
                        BlogPost(
                            pk =  blogPostResponse.pk,
                            title = blogPostResponse.title,
                            slug = blogPostResponse.slug,
                            body = blogPostResponse.body,
                            image = blogPostResponse.image,
                            date_updated = DateUtils.convertServerStringDateToLong(
                                blogPostResponse.date_updated
                            ),
                            username = blogPostResponse.username
                        )

                    )
                }
                updateLocalDb(blogPostList)
                createCacheRequestAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<BlogListSearchResponse>> {
                Log.e(TAG, "handleStateEvent: Check points 1")
                return openApiMainService.searchListBlogPosts(
                    "Token ${authToken.token!!}",
                    query = query,
                    ordering = filterAndOrder,
                    page = page
                )
            }

            override fun setJob(job: Job) {
                addJob("searchBlogPosts", job)
            }

            override fun loadFromCache(): LiveData<BlogViewState> {
                return blogPostDao.returnOrderedBlogQuery(
                   query = query,
                   filterAndOrder = filterAndOrder,
                   page = page
                )
                .switchMap{
                    object: LiveData<BlogViewState>(){
                        override fun onActive() {
                            super.onActive()
                            value = BlogViewState(
                                BlogViewState.BlogFields(
                                    blogList = it,
                                    isQueryInProgress = true
                                )
                            )
                        }
                    }
                }
            }

            override suspend fun updateLocalDb(cachedObject: List<BlogPost>?) {
              if(cachedObject != null){
                  withContext(IO){
                      for(blogPost in cachedObject){
                          try {
                            //Launch each insert as a separate jon to execute in parallel
                              launch{
                                  Log.d(TAG, "updateLocalDb: inserting ")
                                  blogPostDao.insert(blogPost)
                              }
                          }catch (e: Exception){
                              Log.e(TAG, "updateLocalDb: error updating cache" + "on blog pot with slug: ${blogPost.slug}" )
                              //optional error handling??
                          }
                      }
                  }

              }
            }

        }.asLiveData()
    }
}