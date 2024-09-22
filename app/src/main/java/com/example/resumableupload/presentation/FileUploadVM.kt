package com.example.resumableupload.presentation

import androidx.lifecycle.ViewModel
import com.example.resumableupload.data.api.ApiFactory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.io.File

class FileUploadVM : ViewModel() {
    private val backgroundScope = CoroutineScope(Dispatchers.IO)
    private val uploadFileApi = ApiFactory.createFileUploadApi()

    fun uploadFile(filePart: MultipartBody.Part) {
        backgroundScope.launch(CoroutineExceptionHandler { e, ex ->
            ex.printStackTrace()
        }) {
            try {
                val result = uploadFileApi.uploadFile(filePart)
                result.isSuccessful
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    override fun onCleared() {
        backgroundScope.coroutineContext.cancel()
        super.onCleared()
    }
}