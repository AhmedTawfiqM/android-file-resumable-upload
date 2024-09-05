package com.example.resumableupload.data.api

import com.example.resumableupload.data.retrofit.RetrofitFactory

object ApiFactory {

    fun createFileUploadApi(): FilePublish {
        return create(FilePublish::class.java)
    }

    private fun <T> create(service: Class<T>): T {
        return RetrofitFactory.create().create(service)
    }
}