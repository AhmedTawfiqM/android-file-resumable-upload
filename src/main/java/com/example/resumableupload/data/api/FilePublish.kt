package com.example.resumableupload.data.api

import okhttp3.RequestBody
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.HEAD
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Url

interface FilePublish {

    @POST
    @Headers("Upload-Incomplete: ?0", "Upload-Draft-Interop-Version: 3")
    suspend fun uploadFile(@Body requestBody: RequestBody): Response

    @HEAD
    suspend fun checkUploadStatus(@Url url: String): Response

    @PATCH
    suspend fun resumeUpload(
        @Url url: String,
        @Body requestBody: RequestBody
    ): Response

}