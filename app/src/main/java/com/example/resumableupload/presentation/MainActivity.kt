package com.example.resumableupload.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.resumableupload.presentation.file.FileMultiPart
import com.example.resumableupload.presentation.file.FilePicker

class MainActivity : ComponentActivity() {
    private lateinit var filePickerActivityResult: ActivityResultLauncher<Intent>
    private lateinit var vm: FileUploadVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickerRegister()
        setContent {
            UploadButton()
        }
        vm = ViewModelProvider(this)[FileUploadVM::class.java]
    }

    private fun pickerRegister() {
        filePickerActivityResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result == null) return@registerForActivityResult
                val uri = result.data?.data ?: return@registerForActivityResult
                uploadFile(uri)
            }
    }

    private fun uploadFile(uri: Uri) {
        val multiPart = FileMultiPart.create(this, uri)
        vm.uploadFile(multiPart.first) {
            runOnUiThread {
                Toast.makeText(this, "File Upload Successfully !", Toast.LENGTH_LONG).show()
            }
        }
    }

    @Composable
    fun UploadButton() {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(modifier = Modifier
                .height(40.dp)
                .background(color = Color.Black),
                onClick = {
                    FilePicker.pick(context = this@MainActivity, filePickerActivityResult)
                }) {
                Text(text = "Upload File")
            }
        }
    }

}

