package com.example.paging

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations.switchMap
import android.arch.lifecycle.ViewModel
import android.arch.paging.DataSource
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.example.paging.data.*

class ListViewModel : ViewModel() {

    private val pageSize = 30

    private val config = PagedList.Config.Builder()
        .setInitialLoadSizeHint(pageSize * 2)
        .setPageSize(pageSize)
        .build()

    private var dataSource: IDataSource? = null

    val result = MutableLiveData<ResultBean>()

    private val dataSourceFactory = object : DataSource.Factory<Int, DataBean>() {

        override fun create(): DataSource<Int, DataBean> {
            return DataSourceByPage(result).apply { dataSource = this }
//            return DataSourceByItem().apply { dataSource.postValue(this) }
        }
    }

    val livePagedList = LivePagedListBuilder(dataSourceFactory, config).build()

    fun refresh() {
        dataSource?.refresh()
    }

    fun retry() {
        dataSource?.retry()
    }
}