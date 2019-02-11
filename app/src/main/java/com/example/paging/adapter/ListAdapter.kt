package com.example.paging.adapter

import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.paging.R
import com.example.paging.data.DataBean

class ListAdapter(private val retryCallback: () -> Unit) :
    PagedListAdapter<DataBean, RecyclerView.ViewHolder>(comparator) {

    private var listState = LoadingState.Normal

    private fun isShowLastRow(): Boolean {
        //是否显示加载视图
        return when (listState) {
            LoadingState.Loading, LoadingState.Failed, LoadingState.NotMore -> true
            else -> false
        }
    }

    private fun isShowEmptyView(): Boolean {
        //是否显示空视图
        return super.getItemCount() == 0 && listState == LoadingState.Empty
    }

    override fun getItemCount(): Int {
        return super.getItemCount() +
                if (isShowLastRow() || isShowEmptyView()) {
                    1
                } else {
                    0
                }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isShowLastRow() && position == itemCount - 1) {
            R.layout.list_item_loading
        } else if (isShowEmptyView()) {
            R.layout.list_item_empty
        } else {
            R.layout.list_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.list_item -> ItemViewHolder(view)
            R.layout.list_item_loading -> LoadingViewHolder(view, retryCallback) { listState }
            else -> object : RecyclerView.ViewHolder(view) {}
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.list_item -> getItem(position)?.let { (holder as ItemViewHolder).updateView(it) }
            R.layout.list_item_loading -> (holder as LoadingViewHolder).updateView()
        }
    }

    fun setLoadingState(loadingState: LoadingState) {
        if (this.listState == loadingState) return

        val lastItemCount = itemCount
        this.listState = loadingState

        if (itemCount - 1 == lastItemCount) {
            notifyItemInserted(lastItemCount - 1)
        } else if (itemCount + 1 == lastItemCount) {
            notifyItemRemoved(lastItemCount - 1)
        } else if (itemCount == lastItemCount) {
            notifyItemChanged(lastItemCount - 1)
        }

//        notifyItemChanged(lastItemCount - 1)
    }

    fun setEmptyView() {
        if (this.listState == LoadingState.Empty)
            return

        this.listState = LoadingState.Empty

        notifyDataSetChanged()
    }

    companion object {

        private val comparator = object : DiffUtil.ItemCallback<DataBean>() {
            override fun areItemsTheSame(oldItem: DataBean, newItem: DataBean) = (oldItem.id == newItem.id)
            override fun areContentsTheSame(oldItem: DataBean, newItem: DataBean) = (oldItem == newItem)
        }
    }

}