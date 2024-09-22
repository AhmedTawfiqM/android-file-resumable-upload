package com.example.resumableupload.data.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.HEAD
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Url

interface FilePublish {

    @POST("/")
    @Multipart
    @Headers("Upload-Incomplete: ?0", "Upload-Draft-Interop-Version: 3")
    suspend fun uploadFile(@Part file: MultipartBody.Part): ResponseBody

    @HEAD
    suspend fun checkUploadStatus(@Url url: String): Response

    @PATCH("uploads/{id}") // Endpoint for appending data to an existing upload
    @Multipart
    suspend fun resumeUpload(
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): Response

}