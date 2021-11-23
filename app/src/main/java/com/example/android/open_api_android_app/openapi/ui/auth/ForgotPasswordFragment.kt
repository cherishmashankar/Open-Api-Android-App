package com.example.android.open_api_android_app.openapi.ui.auth

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.navigation.fragment.findNavController

import com.example.android.open_api_android_app.R
import com.example.android.open_api_android_app.databinding.FragmentForgotPasswordBinding
import com.example.android.open_api_android_app.databinding.FragmentLoginBinding
import com.example.android.open_api_android_app.openapi.ui.DataState
import com.example.android.open_api_android_app.openapi.ui.DataStateChangeListener
import com.example.android.open_api_android_app.openapi.ui.Response
import com.example.android.open_api_android_app.openapi.ui.ResponseType
import com.example.android.open_api_android_app.openapi.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.lang.ClassCastException
import java.util.*


class ForgotPasswordFragment : BaseAuthFragment(){

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    lateinit var webView: WebView
    lateinit var stateChangeListener: DataStateChangeListener

    val webInteractionCallback: WebAppInterface.OnWebInteractionCallback = object : WebAppInterface.OnWebInteractionCallback{
        override fun onError(errorMessage: String) {
            Log.e(TAG, "onError: $errorMessage")

            val dataState = DataState.error<Any>(
                response = Response(errorMessage, ResponseType.Dialog())
            )
            stateChangeListener.onDataStateChange(
                dataState = dataState
            )
        }

        override fun onSuccess(email: String) {
            Log.d(TAG, "onSuccess: a reset link will be sent to $email.")
            onPasswordResetLinkSent()
        }

        override fun onLoading(isLoading: Boolean) {
            Log.d(TAG, "onLoading... ")
            CoroutineScope(Main).launch {
                stateChangeListener.onDataStateChange(
                    DataState.loading(isLoading = isLoading, cachedData = null)
                )
            }
        }
    }

    override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {
    _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
    val view = binding.root
    return view}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.webview)
        loadPasswordResetWebView()
        binding.returnToLauncherFragment.setOnClickListener {
            findNavController().popBackStack()
            Log.d(TAG, "FrogotPasswordFragment: ${viewModel.hashCode()}")
        }
    }

        override fun onAttach(context: Context) {
            super.onAttach(context)
            try {
                stateChangeListener = context as DataStateChangeListener
            } catch (e: ClassCastException) {
                Log.e(TAG, "onAttach: $context must implement datStateChangeListener")
            }
        }

        @SuppressLint("SetJavaScriptEnabled")
        fun loadPasswordResetWebView() {
            stateChangeListener.onDataStateChange(
                DataState.loading(isLoading = true, cachedData = null)
            )
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    stateChangeListener.onDataStateChange(
                        DataState.loading(false, cachedData = null)
                    )
                }

            }
            webView.loadUrl(Constants.PASSWORD_RESET_URL)
            webView.settings.javaScriptEnabled = true
            webView.addJavascriptInterface(
                WebAppInterface(webInteractionCallback),
                "AndroidTextListener"
            )
        }

    fun onPasswordResetLinkSent(){
        CoroutineScope(Main).launch{
            binding.parentView.removeView(webView)
            webView.destroy()

            val animation = TranslateAnimation(

                binding.passwordResetDoneContainer
                .width.toFloat(),
                0f,
                0f,
                0f
            )
            animation.duration = 500
            binding.passwordResetDoneContainer.startAnimation(animation)
            binding.passwordResetDoneContainer.visibility = View.VISIBLE
        }
    }

        class WebAppInterface
        constructor(
            private val callback: OnWebInteractionCallback
        ) {

            private val TAG: String = "AppDebug"

            @JavascriptInterface
            fun onSuccess(email: String) {
                callback.onSuccess(email)
            }

            @JavascriptInterface
            fun onError(errorMessage: String) {
                callback.onError(errorMessage)
            }

            @JavascriptInterface
            fun onLoading(isLoading: Boolean) {
                callback.onLoading(isLoading)
            }

            interface OnWebInteractionCallback {

                fun onSuccess(email: String)

                fun onError(errorMessage: String)

                fun onLoading(isLoading: Boolean)
            }
        }

    }
