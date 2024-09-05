package com.example.resumableupload.presentation

import androidx.lifecycle.ViewModel
import com.example.resumableupload.data.api.ApiFactory
import com.example.resumableupload.data.retrofit.RetrofitFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File

class FileUploadVM : ViewModel() {
    private val backgroundScope = CoroutineScope(Dispatchers.IO)
    private val uploadFileApi = ApiFactory.createFileUploadApi()

    fun uploadFile(file: File){
        backgroundScope.launch {
            //uploadFileApi.uploadFile()
        }
    }


    override fun onCleared() {
        backgroundScope.coroutineContext.cancel()
        super.onCleared()
    }
}