package com.cyens.retrofituploadfilekt

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.cyens.retrofituploadfilekt.data.UploadService
import com.cyens.retrofituploadfilekt.repositories.FileRepository
import com.cyens.retrofituploadfilekt.ui.theme.RetrofitUploadFileKtTheme
import com.cyens.retrofituploadfilekt.viewmodel.FileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RetrofitUploadFileKtTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        var imageUri by remember { mutableStateOf<Uri?>(null) }

                        // Launcher to open image picker
                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.GetContent()
                        ) { uri: Uri? ->
                            imageUri = uri
                        }

                        val viewModel = viewModel<FileViewModel>()
                        UploadUI(modifier = Modifier, launcher, imageUri, viewModel);
                    }
                }
            }
        }
    }
}

@Composable
fun UploadUI(modifier: Modifier = Modifier,
             contracts: ActivityResultLauncher<String>,
             imageUrl: Uri?,
             viewModel: FileViewModel) {
    val context = LocalContext.current

    //shared click behaviour
    val onImageClick = {
        contracts.launch("image/*")
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val imageModifier = Modifier
            .width(200.dp)
            .height(200.dp)
            .clickable { onImageClick() }

        if(imageUrl != null){
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Upload",
                modifier = imageModifier
            )
        }else{
            Image(
                painter = painterResource(id = R.drawable.upload_file_24),
                contentDescription = "Upload",
                modifier = imageModifier
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            val filesDir = context.filesDir
            val file = File(filesDir, "image.jpg")

            val inputStream = context.contentResolver.openInputStream(imageUrl!!)
            val outputStream = FileOutputStream(file)
            inputStream!!.copyTo(outputStream)

            val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData("file", file.name, requestBody)

            CoroutineScope(Dispatchers.IO).launch {
                val response = FileRepository().uploadFile(part)
                Log.e("ManinActivity", "Upload Response: $response")
                withContext(Dispatchers.Main){
                    Toast.makeText(context, "Response: ${response.message}", Toast.LENGTH_SHORT).show()
                }
            }

        }) { Text("Upload") }

        Spacer(modifier = Modifier.height(80.dp))

        Button(onClick = {
            val fileName = "Adroit6G App.pdf" // File name to download
            val destinationPath = File(context.getExternalFilesDir(null), fileName)

            CoroutineScope(Dispatchers.IO).launch {
               val response = viewModel.downloadFile(fileName, destinationPath.toString())
               Log.e("MainActivity", "Download Response: $response")
               withContext(Dispatchers.Main){
                   Toast.makeText(context, "Download Response: ${response}", Toast.LENGTH_SHORT).show()
               }
            }

        }) { Text("Download") }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RetrofitUploadFileKtTheme {
        //UploadUI()
    }
}