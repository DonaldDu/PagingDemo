package com.example.paging

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.paging.adapter.ListAdapter
import com.example.paging.adapter.LoadingState
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[ListViewModel::class.java] }

    private val adapter = ListAdapter { viewModel.retry() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.adapter = adapter

        viewModel.result.observe(this, Observer {
            it?.let { result ->
                if (result.isInit) {
                    if (result.loadingState == LoadingState.Empty) {
                        //初始化加载数据为空，设置recyclerView空视图
                        recyclerView.post { adapter.setEmptyView() }
                    }
                    //更新下拉刷新的状态
                    swipeRefreshLayout.post {
                        swipeRefreshLayout.isRefreshing = (result.loadingState == LoadingState.Loading)
                    }
                } else {
                    recyclerView.post { adapter.setLoadingState(result.loadingState) }
                }
            }
        })

        viewModel.livePagedList.observe(this, Observer {
            //初始化时，添加数据到适配器
            adapter.submitList(it)
        })

        swipeRefreshLayout.setOnRefreshListener {
            //下拉刷新
            viewModel.refresh()
        }
    }

}
