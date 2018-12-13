package com.example.paging.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.paging.R

class LoadingViewHolder(itemView: View, private val retryCallback: () -> Unit, private val loadingState: () -> LoadingState) : RecyclerView.ViewHolder(itemView) {

    private val loading = itemView.findViewById<View>(R.id.loading)
    private val retry = itemView.findViewById<View>(R.id.retry)
    private val notMore = itemView.findViewById<View>(R.id.notMore)

    init {
        retry.setOnClickListener {
            retryCallback()
        }
    }

    fun updateView() {
        loading.visibility = if (loadingState() == LoadingState.Loading) View.VISIBLE else View.GONE
        retry.visibility = if (loadingState() == LoadingState.Failed) View.VISIBLE else View.GONE
        notMore.visibility = if (loadingState() == LoadingState.NotMore) View.VISIBLE else View.GONE
    }

}