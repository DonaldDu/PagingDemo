package com.example.paging.data

import android.arch.lifecycle.MutableLiveData
import android.arch.paging.ItemKeyedDataSource
import com.example.paging.adapter.LoadingState

class DataSourceByItem : ItemKeyedDataSource<Int, DataBean>(), IDataSource {

    private val result = MutableLiveData<ResultBean>()

    private var retry: (() -> Unit)? = null

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<DataBean>) {
        result.postValue(ResultBean(true, LoadingState.Loading))
        var isReturn = 0
        RemoteData.getById(0, params.requestedLoadSize) { isSuccess, list ->
            if (isSuccess) {
                result.postValue(ResultBean(true, if (list.isEmpty()) LoadingState.Empty else LoadingState.Normal))
                callback.onResult(list)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<DataBean>) {
        result.postValue(ResultBean(false, LoadingState.Loading))
        RemoteData.getById(params.key, params.requestedLoadSize) { isSuccess, list ->
            if (isSuccess) {
                result.postValue(ResultBean(false, if (list.isEmpty()) LoadingState.NotMore else LoadingState.Normal))
                callback.onResult(list)
            } else {
                result.postValue(ResultBean(false, LoadingState.Failed))
                retry = {
                    loadAfter(params, callback)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<DataBean>) {
    }

    override fun getKey(item: DataBean): Int {
        return item.id
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