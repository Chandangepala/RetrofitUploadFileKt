package com.cyens.retrofituploadfilekt.data

import com.cyens.retrofituploadfilekt.models.FileUploadResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Streaming
import java.io.File

interface UploadService {

    @Multipart
    @POST("upload")
    suspend fun uploadFile(@Part file: MultipartBody.Part): FileUploadResponse

    @Streaming
    @GET("download/{fileName}")
    suspend fun downloadFile(
        @Path("fileName") fileName: String
    ): ResponseBody

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