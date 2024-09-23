package com.example.resumableupload.data.ktor

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*

class MyCustomPlugin private constructor() {

    class Config

    companion object : HttpClientPlugin<Config, MyCustomPlugin> {
        override val key: AttributeKey<MyCustomPlugin> = AttributeKey("MyCustomPlugin")

        override fun install(plugin: MyCustomPlugin, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                println("Intercepted Request: ${context.url}")
                proceedWith(subject)
            }

            scope.receivePipeline.intercept(HttpReceivePipeline.Before) { response ->
                println("Intercepted Response: ${response.status}")
                proceedWith(response)
            }
        }

        override fun prepare(block: Config.() -> Unit): MyCustomPlugin {
            return MyCustomPlugin()
        }
    }
}

