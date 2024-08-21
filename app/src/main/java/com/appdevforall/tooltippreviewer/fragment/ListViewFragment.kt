package com.appdevforall.tooltippreviewer.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.appdevforall.tooltippreviewer.CustomAdapter
import com.appdevforall.tooltippreviewer.R


class ListViewFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var context : Context
    private var items: ArrayList<Pair<Pair<String, String>, String>> =
        ArrayList<Pair<Pair<String, String>, String>>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_listview, container, false)
        listView = view.findViewById(R.id.listView)

        //extract the Bundle contents
        items = arguments?.getSerializable("items", ArrayList<Pair<Pair<String, String>, String>>().javaClass)!!
        // Set up the ListView with the first two rows
        val adapter =
            CustomAdapter(
                context,
                items
            )
        listView.adapter = adapter


        return view
    }
}