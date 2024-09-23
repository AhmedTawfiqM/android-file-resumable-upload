package com.example.resumableupload.data.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class ResumableUploadInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code == 104) {
            val locationHeader = response.header("Location")
            println("Upload Resumption Supported at: $locationHeader")
        }

        return response
    }
}