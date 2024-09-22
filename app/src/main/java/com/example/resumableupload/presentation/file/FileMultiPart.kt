package com.example.resumableupload.presentation.file

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.resumableupload.core.CoreApp
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object FileMultiPart {

    fun create(
        context: Context = CoreApp.context,
        uri: Uri,
    ): Triple<MultipartBody.Part, String, String> {
        // Get MIME type
        //val type = contentResolver.getType(uriAttached)?.toMediaTypeOrNull()

        val realFileName = getRealFileNameFromUri(context, uri)
        val realExtension = realFileName.substringAfterLast('.', "")

        val tempFile = File(context.cacheDir, realFileName)
        val inputStream = context.contentResolver.openInputStream(uri)
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }

        // Prepare the multipart part
        val part = prepareFilePart(
            "file",
            realExtension.toMediaTypeOrNull(),
            tempFile
        )

        return Triple(part, realExtension, realFileName)
    }

    private fun prepareFilePart(
        partName: String,
        type: MediaType?,
        file: File
    ): MultipartBody.Part {
        val requestFile: RequestBody = file.let {
            return@let it
                .asRequestBody(type)
        }
        return requestFile.let { MultipartBody.Part.createFormData(partName, file.name, it) }
    }

    // Function to retrieve real file name from URI
    private fun getRealFileNameFromUri(
        context: Context = CoreApp.context,
        uri: Uri
    ): String {
        var fileName = ""
        val cursor = context.contentResolver.query(uri, null, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                fileName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return fileName
    }
}