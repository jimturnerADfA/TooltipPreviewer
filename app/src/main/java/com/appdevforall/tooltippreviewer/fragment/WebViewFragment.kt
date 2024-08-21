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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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
                        activity?.runOnUiThread {
                            webView.clearHistory()
                            webView.loadUrl("about:blank")
                            webView.destroy()
                        }
                        parentFragmentManager.popBackStack()
                        isEnabled = false // Disable this callback to let the default back press behavior occur
                    }
                }
            })

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_webview, container, false)

        // Initialize the WebView
        webView = view.findViewById(R.id.webView)

        // Set up WebChromeClient to support JavaScript
        webView.webChromeClient = WebChromeClient()

        // Enable JavaScript if needed
        webView.settings.javaScriptEnabled = true

        // Load the HTML file from the assets folder
        webView.loadUrl("file:///android_asset/index.html")
//        webView.loadUrl("file:///android_asset/AnchorTestLink.html")

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up the WebView in Fragment
        webView.clearHistory()
        webView.loadUrl("about:blank")
        webView.destroy()
    }
}
