package com.example.resumableupload.data.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val response = chain.proceed(request)
        val headers = response.headers
        return response
    }
}