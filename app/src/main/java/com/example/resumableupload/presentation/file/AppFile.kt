package com.example.resumableupload.presentation.file

import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import com.example.resumableupload.core.CoreApp
import java.io.File

object AppFile {

    fun getType(file: File): String? {
        return getType(file.path)
    }

    fun getType(uri: Uri): String? {
        val path = uri.path ?: return null
        return getType(path)
    }

    private fun getType(path: String): String? {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(path)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension)
    }

    fun createTempImageFile(
        fileName: String = System.currentTimeMillis().toString(),
        fileExtension: String = ".jpg"
    ): File {
        val storageDir = CoreApp.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            fileName,
            fileExtension,
            storageDir
        )
    }
}