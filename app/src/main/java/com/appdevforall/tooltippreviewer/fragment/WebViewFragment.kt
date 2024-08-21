package com.appdevforall.tooltippreviewer.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.appdevforall.tooltippreviewer.R
import com.appdevforall.tooltippreviewer.R.*


class WebviewFragment : Fragment() {
    private lateinit var webView: WebView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Handle back press using OnBackPressedCallback
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        isEnabled =
                            false // Disable this callback to let the default back press behavior occur
                        requireActivity().onBackPressed() // Call default back press behavior
                    }
                }
            })

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_webview, container, false)

        // Initialize the WebView
        webView = view.findViewById(R.id.webView)

        // Set WebViewClient to handle webpage navigation
        webView.webViewClient = WebViewClient()

        // Set up WebChromeClient to support JavaScript
        webView.webChromeClient = WebChromeClient()

        // Enable JavaScript if needed
        webView.settings.javaScriptEnabled = true

        //Enable the back key

        // Load the HTML file from the assets folder
        webView.loadUrl("file:///android_asset/index.html")

        return view
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the WebView in Fragment
        webView.destroy()
    }
}
