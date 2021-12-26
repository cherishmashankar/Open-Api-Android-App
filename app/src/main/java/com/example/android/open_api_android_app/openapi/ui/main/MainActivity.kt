package com.example.android.open_api_android_app.openapi.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController

import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.openapi.ui.BaseActivity
import com.example.android.open_api_android_app.openapi.ui.auth.AuthActivity
import com.example.android.open_api_android_app.openapi.ui.main.account.ChangePasswordFragment
import com.example.android.open_api_android_app.openapi.ui.main.account.UpdateAccountFragment
import com.example.android.open_api_android_app.openapi.ui.main.blog.UpdateBlogFragment
import com.example.android.open_api_android_app.openapi.ui.main.blog.ViewBlogFragment
import com.example.android.open_api_android_app.openapi.util.BottomNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.Toolbar
import com.example.android.open_api_android_app.openapi.ui.main.account.BaseAccountFragment
import com.example.android.open_api_android_app.openapi.ui.main.blog.BaseBlogFragment
import com.example.android.open_api_android_app.openapi.ui.main.create_blog.BaseCreateBlogFragment
import com.example.android.open_api_android_app.openapi.util.setUpNavigation
import com.google.android.material.appbar.AppBarLayout

//import kotlinx.android.synthetic.main.activity_auth.*
//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.activity_main.progress_bar

class MainActivity : BaseActivity(),
    BottomNavController.NavGraphProvider,
    BottomNavController.OnNavigationGraphChanged,
    BottomNavController.OnNavigationReselectedListener {

    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.nav_blog,
            this,
            this
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tool_bar = findViewById<Toolbar>(R.id.tool_bar)
        setupActionBar(tool_bar)
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }


        subscribeObservers()
    }

    fun subscribeObservers() {
        sessionManager.cachedToken.observe(this, Observer { authToken ->
            Log.d(TAG, "MainActivity, subscribeObservers: ViewState: ${authToken}")
            if (authToken == null || authToken.account_pk == -1 || authToken.token == null) {
                navAuthActivity()
                finish()
            }
        })
    }


    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }


    override fun displayProgressBar(bool: Boolean) {
        val progress_bar = findViewById<ProgressBar>(R.id.progress_bar)
        if (bool) {
            progress_bar.visibility = View.VISIBLE
        } else {
            progress_bar.visibility = View.GONE
        }
    }

    override fun getNavGraphId(itemId: Int) = when (itemId) {
        R.id.nav_blog -> {
            R.navigation.nav_blog
        }
        R.id.nav_account -> {
            R.navigation.nav_account
        }
        R.id.nav_create_blog -> {
            R.navigation.nav_create_blog
        }
        else -> {
            R.navigation.nav_blog
        }
    }

    private fun setupActionBar(tool_bar: Toolbar) {
        setSupportActionBar(tool_bar)

    }

    override fun onGraphChanged() {
        expandAppbar()
        cancelActiveJobs()
    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) =
        when (fragment) {

            is ViewBlogFragment -> {
                navController.navigate(R.id.action_viewBlogFragment_to_blogFragment)
            }

            is UpdateBlogFragment -> {
                navController.navigate(R.id.action_updateBlogFragment_to_blogFragment)
            }

            is UpdateAccountFragment -> {
                navController.navigate(R.id.action_updateAccountFragment_to_accountFragment)
            }

            is ChangePasswordFragment -> {
                navController.navigate(R.id.action_changePasswordFragment_to_accountFragment)
            }
            else -> {
                //do nothing
            }
        }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun cancelActiveJobs() {
        val fragments = bottomNavController.fragmentManager
            .findFragmentById(bottomNavController.containerId)?.childFragmentManager
            ?.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                when (fragment) {
                    is BaseAccountFragment -> fragment.cancelActiveJobs()
                    is BaseBlogFragment -> fragment.cancelActiveJobs()
                    is BaseCreateBlogFragment -> fragment.cancelActiveJobs()

                }
            }
        }
        displayProgressBar(false)
    }


    override fun expandAppbar() {
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }
}
