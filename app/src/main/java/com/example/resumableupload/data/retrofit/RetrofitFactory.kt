package com.example.resumableupload.data.retrofit

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.resumableupload.core.CoreApp
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
    private const val timeOut = 60L
   // private const val baseUrl = "http://localhost:1010"
    private const val baseUrl = "http://192.168.8.103:1010"

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
            .connectTimeout(timeOut, TimeUnit.SECONDS)
            .readTimeout(timeOut, TimeUnit.SECONDS)
            .writeTimeout(timeOut, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor(CoreApp.context))
            .addInterceptor(interceptor)
            .addInterceptor(ResumableUploadInterceptor())
            //.followSslRedirects(true)
            .followRedirects(false)
            .build()
    }
}


