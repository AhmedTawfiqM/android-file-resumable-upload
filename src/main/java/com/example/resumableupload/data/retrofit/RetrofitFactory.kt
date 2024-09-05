package com.example.resumableupload.data.retrofit

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {
    private val gson = GsonBuilder()
        .setLenient()
        .create()
    private val timeOut = 60L
    private val baseUrl = "http://localhost:8080/"

    fun create(): Retrofit {
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getHttpClient())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))

        return builder.build()
    }

    private fun getHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        HttpLoggingInterceptor.Level.BODY

        return AppOkHttpClient.create()
            .followRedirects(true)
            .followSslRedirects(true)
            .connectTimeout(timeOut, TimeUnit.MINUTES)
            .readTimeout(timeOut, TimeUnit.MINUTES)
            .writeTimeout(timeOut, TimeUnit.MINUTES)
            .addInterceptor(interceptor)
            .addInterceptor(interceptor)
            .followSslRedirects(true)
            .followRedirects(true)
            .build()
    }


}


