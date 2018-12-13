package com.example.paging.data

import android.arch.lifecycle.MutableLiveData

interface IDataSource {

    fun  getResultBean(): MutableLiveData<ResultBean>

    fun refresh()

    fun retry()

}