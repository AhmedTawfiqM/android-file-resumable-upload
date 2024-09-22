package com.example.resumableupload.core

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

@SuppressLint("StaticFieldLeak")
class CoreApp : Application() {


    override fun onCreate() {
        super.onCreate()
        setup(this)
    }

    private fun setup(applicationContext: Context) {
        context = applicationContext
    }

    companion object{
        lateinit var context: Context
    }
}