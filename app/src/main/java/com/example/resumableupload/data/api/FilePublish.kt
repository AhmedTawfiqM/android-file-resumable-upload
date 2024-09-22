package com.example.resumableupload.data.api

import okhttp3.MultipartBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.HEAD
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface FilePublish {

    @POST("/")
    @Multipart
    @Headers("Upload-Incomplete: ?0", "Upload-Draft-Interop-Version: 3")
    suspend fun uploadFile(@Part file: MultipartBody.Part): retrofit2.Response<ResponseBody>

    //Location: http://localhost:8080/uploads/917d2d61-5107-419a-88af-8910f6556a8e
    @HEAD("uploads/{id}")
    suspend fun checkUploadStatus(@Path("id") id: String): retrofit2.Response<ResponseBody>

    @PATCH("uploads/{id}")
    @Headers("Upload-Offset:{offset}")
    @Multipart
    suspend fun resumeUpload(
        @Path("offset") offset: String,
        @Path("id") id: String,
        @Part file: MultipartBody.Part
    ): retrofit2.Response<ResponseBody>

}