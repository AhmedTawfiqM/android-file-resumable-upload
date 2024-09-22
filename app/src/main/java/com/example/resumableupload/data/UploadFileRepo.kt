package com.example.resumableupload.data

import com.example.resumableupload.data.api.FilePublish
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody

class UploadFileRepo(private val api: FilePublish) {

    suspend fun uploadFile(file: MultipartBody.Part): retrofit2.Response<ResponseBody> {
        return api.uploadFile(file)
    }

    suspend fun checkUploadStatus(url: String): retrofit2.Response<ResponseBody> {
        return api.checkUploadStatus(url)
    }

    suspend fun resumeUpload(
        offset: String,
        url: String,
        file: MultipartBody.Part
    ): retrofit2.Response<ResponseBody> {
        return api.resumeUpload(offset,url, file)
    }
}