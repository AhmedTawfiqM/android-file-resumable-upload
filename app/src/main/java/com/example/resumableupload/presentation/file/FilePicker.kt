package com.example.resumableupload.presentation.file

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider


object FilePicker {
    lateinit var photoUri: Uri
    const val chooserTitle = "Select File"

    fun pick(
        context: Context,
        activityResult: ActivityResultLauncher<Intent>,
        type: FileType = FileType.All
    ) {
        when (type) {
            FileType.All -> pickAllFiles(context, activityResult)
            FileType.Image -> pickImage(context, activityResult)
            FileType.AllApps -> pickImageAndVideo(activityResult)
        }
    }

    private fun pickImageAndVideo(
        activityResult: ActivityResultLauncher<Intent>
    ) {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
        }
        activityResult.launch(intent)
    }

    private fun pickAllFiles(
        context: Context,
        filePickerActivityResult: ActivityResultLauncher<Intent>
    ) {
        val filesIntent = Intent(Intent.ACTION_GET_CONTENT)
        filesIntent.addCategory(Intent.CATEGORY_OPENABLE)
        filesIntent.type = FileType.All.type

//        val filesIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//            addCategory(Intent.CATEGORY_OPENABLE)
//            type = FileType.All.type
//        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = AppFile.createTempImageFile()

        photoUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        val chooserIntent = Intent.createChooser(filesIntent, chooserTitle)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(cameraIntent))
        filePickerActivityResult.launch(chooserIntent)
    }

    //TODO: to be deleted !
    private fun pickImage(filePickerActivityResult: ActivityResultLauncher<Intent>) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = AppFile.createTempImageFile()
        photoUri = Uri.fromFile(photoFile)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        val chooserIntent = Intent.createChooser(galleryIntent, chooserTitle)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(cameraIntent))

        filePickerActivityResult.launch(chooserIntent)
    }

    private fun pickImage(
        context: Context,
        filePickerActivityResult: ActivityResultLauncher<Intent>
    ) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoFile = AppFile.createTempImageFile()

        photoUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        val chooserIntent = Intent.createChooser(galleryIntent, chooserTitle)
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf<Parcelable>(cameraIntent))

        filePickerActivityResult.launch(chooserIntent)
    }

}