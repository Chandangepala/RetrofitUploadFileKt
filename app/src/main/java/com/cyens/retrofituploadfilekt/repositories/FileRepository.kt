package com.cyens.retrofituploadfilekt.repositories

import coil.network.HttpException
import com.cyens.retrofituploadfilekt.data.UploadService
import com.cyens.retrofituploadfilekt.models.FileUploadResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import java.io.File
import java.io.IOException

class FileRepository {
    suspend fun uploadFile(file: MultipartBody.Part): FileUploadResponse{
        return try {
            UploadService.instance.uploadFile(file)
        }catch (e: IOException){
            e.printStackTrace()
            FileUploadResponse("","Failed to upload:Error: ${e.message}",false)
        }catch (e: Exception){
            e.printStackTrace()
            FileUploadResponse("", "Failed to upload:Error: ${e.message}", false)
        }
    }

    suspend fun downloadFile(fileName: String, destinationPath: String): Result<File>{
        return withContext(Dispatchers.IO){
            val file = File(destinationPath)
            try {
                val response = UploadService.instance.downloadFile(fileName)



                // Create parent directories if they don't exist
                file.parentFile?.mkdirs()

                // Use buffer for better performance
                ByteArray(DEFAULT_BUFFER_SIZE)
                response.contentLength()

                run {
                    response.byteStream().use { inputStream ->
                        file.outputStream().buffered().use { outputStream ->
                            inputStream.copyTo(outputStream)
                            outputStream.flush()
                        }
                    }
                    Result.success(file)
                }
            }catch (e: IOException){
                if (file.exists()){
                    file.delete()
                }
                Result.failure(e)
            }catch (e: Exception){
                Result.failure(e)
            }
        }
    }
}