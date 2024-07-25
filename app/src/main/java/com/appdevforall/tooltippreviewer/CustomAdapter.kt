package com.appdevforall.tooltippreviewer

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.PopupWindow
import android.widget.TextView

class CustomAdapter(context: Context, private val items: List<Pair<Pair<String, String>, String>>,
                    var lateinit: Any
) :
    ArrayAdapter<Pair<Pair<String, String>, String>>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false)
        val screen = items[position].first.first
        val tag = items[position].first.second
        val tooltip = items[position].second
        val tvScreen: TextView = itemView.findViewById(R.id.tvScreen)
        tvScreen.text = screen
        val tvTag: TextView = itemView.findViewById(R.id.tvTag)
        tvTag.text = tag

        // Set a tooltip
        itemView.setOnLongClickListener {
            showPopupWindow(itemView, position)
            true
        }

        return itemView
    }

    private fun showPopupWindow(anchorView: View, position : Int) {
        // Inflate the PopupWindow layout
        val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_window, null)
        val tooltip = items[position].second

        // Create the PopupWindow
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)

        // Initialize the WebView
        val webView = popupView.findViewById<WebView>(R.id.webview)
        webView.webViewClient = WebViewClient() // Ensure links open within the WebView
        webView.settings.javaScriptEnabled = true // Enable JavaScript if needed
        webView.loadData(tooltip, "text/html", "UTF-8")

        // Set background drawable (optional)
        popupWindow.setBackgroundDrawable(null)

        // Show the PopupWindow
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0 /*chorView.x.toInt(), anchorView.y.toInt()*/)
    }
}
