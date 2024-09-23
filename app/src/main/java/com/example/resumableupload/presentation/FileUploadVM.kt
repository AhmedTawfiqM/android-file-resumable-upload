package com.example.resumableupload.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.resumableupload.data.Header
import com.example.resumableupload.data.api.ApiFactory
import com.example.resumableupload.data.ktor.KtorClientFactory
import com.example.resumableupload.data.ktor.KtorFileUploadApi
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlinx.coroutines.runBlocking
import android.content.Context
import android.net.Uri
import java.io.InputStream


class FileUploadVM : ViewModel() {
    private val backgroundScope = CoroutineScope(Dispatchers.IO)
    private val retrofitFileUploadApi = ApiFactory.createFileUploadApi()
    private val ktorFileUploadApi = KtorFileUploadApi(KtorClientFactory.create())

    fun uploadFile(filePart: MultipartBody.Part, completion: () -> Unit) {
        backgroundScope.launch {
            try {
                val response = retrofitFileUploadApi.uploadFile(filePart)
                val headers = response.headers()
                val statusCode = response.code()
                response.isSuccessful

                if (statusCode == 104) {
                    val presumableUrl = headers[Header.location]
                    println("Upload started, continue uploading...")
                } else if (statusCode == 201) {
                    completion()
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun uploadByKtor(context: Context, fileUri: Uri) {
        backgroundScope.launch {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(fileUri)
                if (inputStream != null) {
                    val response = ktorFileUploadApi.uploadFile(inputStream, "example.txt")
                    val statusCode = response.status.value

                    when (statusCode) {
                        104 -> {
                            println("Upload started, continue uploading...")
                        }

                        200, 201 -> {
                            println("Upload completed successfully!")
                        }

                        else -> {
                            println("Received unexpected status code: $statusCode")
                        }
                    }

                } else {
                    println("Failed to open input stream from URI")
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun mockTest(filePart: MultipartBody.Part) {
        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "file", filePart.body)
            .build()

        val request = Request.Builder()
            .url("http://192.168.8.103:1010/")
            .post(requestBody)
            .header("Upload-Incomplete", "?0")
            .build()

        val call = client.newCall(request)

        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("RawHttp", "Response Code: ${response.code}")

                if (response.code == 104) {
                    val locationHeader = response.header("Location")
                    Log.d("RawHttp", "Upload Resumption Supported at: $locationHeader")
                } else if (response.isSuccessful) {
                    Log.d("RawHttp", "Upload successful: ${response.body?.string()}")
                } else {
                    Log.e("RawHttp", "Upload failed: ${response.body?.string()}")
                }
            }
        })

    }


    override fun onCleared() {
        backgroundScope.coroutineContext.cancel()
        super.onCleared()
    }
}