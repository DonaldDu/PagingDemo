package com.example.paging.data

import com.example.paging.log

//模拟远程数据
object RemoteData {

    private var errorCount = 0

    fun getById(id: Int, size: Int, callback: (isSuccess: Boolean, list: ArrayList<DataBean>) -> Unit) {
        Thread {
            log("id:$id,size:$size")
            val list = arrayListOf<DataBean>()
            val start = id + 1
            val end = id + size
            for (i in start..end) {
                list.add(DataBean().apply {
                    this.id = i
                    this.string = "item$i"/*+(if(i==start) " start" else if (i == end) " end" else "")*/
                })
            }

            val isSuccess = if (id < 100) {
                errorCount = 1
                true
            } else {
                --errorCount < 0
            }
            Thread.sleep(2000)
            callback(isSuccess, if (id > 200) arrayListOf() else list)
//            callback(true, arrayListOf())
        }.start()
    }

    fun getByPage(page: Int, size: Int, callback: (isSuccess: Boolean, list: ArrayList<DataBean>) -> Unit) {
        Thread {
            log("page:$page,size:$size")
            val list = arrayListOf<DataBean>()
            val start = ((page - 1) * size) + 1
            val end = (page - 1) * size + size
            for (i in start..end) {
                list.add(DataBean().apply {
                    this.id = i
                    this.string =  "item$i"/*+(if(i==start) " start" else if (i == end) " end" else "")*/
                })
            }

            val isSuccess = if (page < 5) {
                errorCount = 1
                true
            } else {
                --errorCount < 0
            }
            Thread.sleep(2000)
            callback(isSuccess, if (page > 10) arrayListOf() else list)
//            callback(true, arrayListOf())
        }.start()
    }

}