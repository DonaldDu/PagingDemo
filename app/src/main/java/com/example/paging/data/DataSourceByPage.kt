package com.example.paging.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PageKeyedDataSource
import com.example.paging.adapter.LoadingState

class DataSourceByPage(private val result: MutableLiveData<ResultBean>) : PageKeyedDataSource<Int, DataBean>(),
    IDataSource {

    private val pageSize = 30
    private val initPageSize = pageSize * 2

    private var retry: (() -> Unit)? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, DataBean>) {
        result.postValue(ResultBean(true, LoadingState.Loading))
        val key = 1
        var isReturn = 0
        RemoteData.getByPage(key, initPageSize) { isSuccess, list ->
            if (isSuccess) {
                result.postValue(ResultBean(true, if (list.isEmpty()) LoadingState.Empty else LoadingState.Normal))
                callback.onResult(list, key - 1, key + (initPageSize / pageSize))
            } else {
                result.postValue(ResultBean(true, LoadingState.Failed))
                retry = {
                    loadInitial(params, callback)
                }
            }
            isReturn = 1
        }

        while (isReturn == 0) {
            Thread.sleep(100)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DataBean>) {
        result.postValue(ResultBean(false, LoadingState.Loading))
        RemoteData.getByPage(params.key, pageSize) { isSuccess, list ->
            if (isSuccess) {
                result.postValue(ResultBean(false, if (list.isEmpty()) LoadingState.NotMore else LoadingState.Normal))
                callback.onResult(list, params.key + 1)
            } else {
                result.postValue(ResultBean(false, LoadingState.Failed))
                retry = {
                    loadAfter(params, callback)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DataBean>) {
    }

    override fun retry() {
        retry?.let {
            retry = null
            it()
        }
    }

    override fun refresh() {
        invalidate()
    }
}