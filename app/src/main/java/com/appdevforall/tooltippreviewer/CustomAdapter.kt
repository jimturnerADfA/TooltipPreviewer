package com.appdevforall.tooltippreviewer

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CustomAdapter(context: Context,
                    private val items: List<Pair<Pair<String, String>, String>>) :
    ArrayAdapter<Pair<Pair<String, String>, String>>(context, 0, items) {

    private val filteredItems =
        items.filter{
            !(it.first.second.endsWith("_expanded", ignoreCase = true)
                    || it.first.second.endsWith("_links", ignoreCase = true))
        }
    private val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val popupView = inflater.inflate(R.layout.popup_window, null)
    private val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT, true)


    override fun getItem(position: Int): Pair<Pair<String, String>, String>? {
        return filteredItems?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount() : Int {
        return filteredItems.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.listview_item, parent, false)
        val screen = filteredItems[position].first.first
        val tag = filteredItems[position].first.second
        val tvScreen: TextView = itemView.findViewById(R.id.tvScreen)
        tvScreen.text = screen
        val tvTag: TextView = itemView.findViewById(R.id.tvTag)
        tvTag.text = tag

        // Set a tooltip
        itemView.setOnLongClickListener {
            showPopupWindow(itemView, position, 0)
            true
        }

        return itemView
    }

    private fun showPopupWindow(anchorView: View, position : Int, level : Int) {
        // Inflate the PopupWindow layout
        val fab = popupView.findViewById<FloatingActionButton>(R.id.fab)
        val tooltip = when(level) {
            0 -> filteredItems[position].second
            1 -> items[position+level].second
            else -> ""
        }

        fab.setOnClickListener {
            showPopupWindow(anchorView, position, level+1)
        }

        /////popupView.findViewById<FloatingActionButton>(R.id.fab)?.setOnClickListener() { showMore(position) }
        // Create the PopupWindow
        if(popupWindow.isShowing) {
            popupWindow.dismiss()
        }
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

    fun showMore(position:Int) {
        val tooltip = items.get(position*3+1).second

    }
}
