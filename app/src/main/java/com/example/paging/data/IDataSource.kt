package com.example.paging.data

interface IDataSource {
    fun refresh()
    fun retry()
}