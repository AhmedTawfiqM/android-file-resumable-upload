package com.example.resumableupload.data

import com.example.resumableupload.data.api.FilePublish
import okhttp3.RequestBody
import okhttp3.Response

class UploadFileRepo(val api: FilePublish) {

    suspend fun uploadFile(requestBody: RequestBody): Response {
        return api.uploadFile(requestBody)
    }

    suspend fun checkUploadStatus(url: String): Response {
        return api.checkUploadStatus(url)
    }

    suspend fun resumeUpload(
        url: String,
        requestBody: RequestBody
    ): Response {
        return api.resumeUpload(url, requestBody)
    }
}