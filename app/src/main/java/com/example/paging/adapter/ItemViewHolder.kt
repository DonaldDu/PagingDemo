package com.example.paging.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.paging.R
import com.example.paging.data.DataBean

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textView = itemView.findViewById<TextView>(R.id.textView)

    fun updateView(item: DataBean) {
        textView.text = item.string
    }

}