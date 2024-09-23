package com.example.resumableupload.data.ktor

import io.ktor.client.*
import io.ktor.client.plugins.onUpload
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.InternalAPI
import io.ktor.utils.io.streams.*
import java.io.InputStream

class KtorFileUploadApi(private val client: HttpClient) {

    suspend fun uploadFile(inputStream: InputStream, fileName: String): HttpResponse {
        return client.submitFormWithBinaryData(
            url = "/",
            formData = formData {
                append("file", InputProvider { inputStream.asInput() }, Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }
        ){
            headers {
               val test =  this.get("Location")
                test
            }
            onUpload{ sentBytes,total->
                sentBytes
                total
            }
        }
    }

    suspend fun checkUploadStatus(id: String): HttpResponse {
        return client.head("uploads/$id")
    }

    @OptIn(InternalAPI::class)
    suspend fun resumeUpload(offset: String, id: String, inputStream: InputStream, fileName: String): HttpResponse {
        return client.patch("uploads/$id") {
            header("Upload-Offset", offset)
            body = MultiPartFormDataContent(
                formData {
                    append("file", InputProvider { inputStream.asInput() }, Headers.build {
                        append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                    })
                }
            )
        }
    }
}


