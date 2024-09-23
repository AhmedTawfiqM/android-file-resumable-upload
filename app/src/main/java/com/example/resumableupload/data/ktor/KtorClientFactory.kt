package com.example.resumableupload.data.ktor

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import java.util.concurrent.TimeUnit

object KtorClientFactory {
    private const val baseUrl = "http://192.168.8.103:1010"
    private const val timeOut = 60L

    fun create(): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config {
                    connectTimeout(timeOut, TimeUnit.SECONDS)
                    readTimeout(timeOut, TimeUnit.SECONDS)
                    writeTimeout(timeOut, TimeUnit.SECONDS)
                }
            }

            install(KtorCustomPlugin)
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }

            defaultRequest {
                url(baseUrl)
            }
        }
    }
}
