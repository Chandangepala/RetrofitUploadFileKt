package com.cyens.retrofituploadfilekt.data

import com.cyens.retrofituploadfilekt.models.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadService {

    @Multipart
    @POST("upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): FileUploadResponse

    companion object{
        val instance by lazy {
            Retrofit.Builder().baseUrl("http://10.0.2.2:8080/api/files/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(
                    OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor()
                        .apply { level = HttpLoggingInterceptor.Level.BODY })
                        .build()
                )
                .build()
                .create(UploadService::class.java)
        }
    }
}