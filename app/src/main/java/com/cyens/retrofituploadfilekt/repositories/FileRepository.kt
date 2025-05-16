package com.cyens.retrofituploadfilekt.repositories

import com.cyens.retrofituploadfilekt.data.UploadService
import com.cyens.retrofituploadfilekt.models.FileUploadResponse
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
}