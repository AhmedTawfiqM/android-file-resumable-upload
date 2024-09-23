package com.example.resumableupload.data

import com.example.resumableupload.data.retrofit.RetrofitFactory
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL

class UploadHttpUrlTask(
    private val fileToUpload: File,
    val response: (String) -> Unit = {}
) : Runnable {
    override fun run() {
        var connection: HttpURLConnection? = null
        var dos: DataOutputStream? = null
        var fileInputStream: FileInputStream? = null

        try {
            val boundary = "===" + System.currentTimeMillis() + "==="
            val lineEnd = "\r\n"
            val twoHyphens = "--"
            val url = URL(RetrofitFactory.baseUrl)
            connection = url.openConnection() as HttpURLConnection

            // Setup the request
            connection.doOutput = true
            connection.doInput = true
            connection.useCaches = false
            connection.requestMethod = "POST"
            connection.setRequestProperty("Connection", "Keep-Alive")
            connection.setRequestProperty("ENCTYPE", "multipart/form-data")
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
            connection.setRequestProperty("file", fileToUpload.name)
            connection.setRequestProperty("Upload-Incomplete", "?0")
            connection.setRequestProperty("Upload-Draft-Interop-Version", "3")

            // Open streams
            dos = DataOutputStream(connection.outputStream)
            fileInputStream = FileInputStream(fileToUpload)

            // Write the multipart data
            dos.writeBytes(twoHyphens + boundary + lineEnd)
            dos.writeBytes(
                "Content-Disposition: form-data; name=\"file\";filename=\""
                        + fileToUpload.name + "\"" + lineEnd
            )
            dos.writeBytes(lineEnd)

            // Upload file content in chunks
            val buffer = ByteArray(1024 * 1024)
            var bytesRead: Int
            while (fileInputStream.read(buffer).also { bytesRead = it } > 0) {
                dos.write(buffer, 0, bytesRead)
            }

            // Write the ending boundary
            dos.writeBytes(lineEnd)
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)

            // Flush and close the DataOutputStream
            //dos.flush()
            //dos.close()
            //fileInputStream.close()

            // Now check the final response from the server
            val finalResponseCode = connection.responseCode
            val finalResponseMessage = connection.responseMessage
            if (finalResponseCode == 200 || finalResponseCode == 201) {
                response("Upload completed successfully.")
            } else if (finalResponseCode == 104) {
                val locationHeader = connection.getHeaderField("Location")
                response("104 response code , $locationHeader")
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            // Ensure streams and connections are closed in the event of an exception
            try {
                //dos?.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            try {
                //fileInputStream?.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            //connection?.disconnect()
        }
    }
}
