package com.example.paging.data

import com.example.paging.adapter.LoadingState

class ResultBean(var isInit: Boolean = false,
                 var loadingState: LoadingState = LoadingState.Normal)