package com.example.android.open_api_android_app.openapi.ui.main.blog

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.openapi.ui.DataStateChangeListener
import com.example.android.open_api_android_app.openapi.viewmodels.ViewModelProviderFactory

import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseBlogFragment : DaggerFragment(){

    val TAG: String = "AppDebug"

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    lateinit var viewModel: BlogViewModel

    lateinit var stateChangeListener: DataStateChangeListener



    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            stateChangeListener = context as DataStateChangeListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement DataStateChangeListener" )
        }
    }
    fun setupActionBarWithNavController(fragmentId: Int, activity: AppCompatActivity){
        val appBarConfiguration = AppBarConfiguration(setOf(fragmentId))
        NavigationUI.setupActionBarWithNavController(
            activity,
            findNavController(),
            appBarConfiguration
        )
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActionBarWithNavController(R.id.blogFragment, activity  as AppCompatActivity)

        viewModel = activity?.run{
            ViewModelProvider(this, providerFactory).get(BlogViewModel::class.java)
        }?:throw Exception("Invalid Activity")

        cancelActiveJobs()
    }

    public fun cancelActiveJobs(){
      viewModel.cancelActiveJobs()
    }
}