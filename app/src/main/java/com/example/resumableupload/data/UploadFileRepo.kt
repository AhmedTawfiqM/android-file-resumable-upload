package com.example.resumableupload.data

import com.example.resumableupload.data.api.FilePublish
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response

class UploadFileRepo(private val api: FilePublish) {

    suspend fun uploadFile(file: MultipartBody.Part): Response {
        return api.uploadFile(file)
    }

    suspend fun checkUploadStatus(url: String): Response {
        return api.checkUploadStatus(url)
    }

    suspend fun resumeUpload(
        url: String,
        file: MultipartBody.Part
    ): Response {
        return api.resumeUpload(url, file)
    }
}