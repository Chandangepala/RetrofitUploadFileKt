package com.cyens.retrofituploadfilekt.data

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {

    @Multipart
    @POST("upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): ResponseBody

}