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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
    private var isKtor = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickerRegister()
        setContent {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                UploadByRetrofitButton()
                Spacer(Modifier.height(2.dp))
                UploadByKtorButton()
            }
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
        if (isKtor) {
            vm.uploadByKtor(this, uri)
//            val file = FileMultiPart.createTempFile(this, uri)
//            vm.uploadByHttpUrlConnection(file) {
//                runOnUiThread {
//                    Toast.makeText(this, it, Toast.LENGTH_LONG).show()
//                }
//            }
        } else {
            val multiPart = FileMultiPart.create(this, uri)
            //vm.mockTest(multiPart.first)
            vm.uploadFile(multiPart.first) {
                runOnUiThread {
                    Toast.makeText(this, "File Upload Successfully !", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    @Composable
    fun UploadByRetrofitButton() {
        Button(modifier = Modifier
            .height(40.dp)
            .background(color = Color.Black),
            onClick = {
                isKtor = false
                FilePicker.pick(context = this@MainActivity, filePickerActivityResult)
            }) {
            Text(text = "Upload By Retrofit")
        }
    }

    @Composable
    fun UploadByKtorButton() {
        Button(modifier = Modifier
            .height(40.dp)
            .background(color = Color.Black),
            onClick = {
                isKtor = true
                FilePicker.pick(context = this@MainActivity, filePickerActivityResult)
            }) {
            Text(text = "Upload By Ktor")
        }
    }

}

