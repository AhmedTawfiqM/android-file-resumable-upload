package com.example.resumableupload.data.ktor

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.util.*

class KtorCustomPlugin private constructor() {

    class Config

    companion object : HttpClientPlugin<Config, KtorCustomPlugin> {
        override val key: AttributeKey<KtorCustomPlugin> = AttributeKey("KtorCustomPlugin")

        override fun install(plugin: KtorCustomPlugin, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                proceedWith(subject)
            }

            scope.receivePipeline.intercept(HttpReceivePipeline.Before) { response ->
                proceedWith(response)
            }
        }

        override fun prepare(block: Config.() -> Unit): KtorCustomPlugin {
            return KtorCustomPlugin()
        }
    }
}

