package com.cyens.retrofituploadfilekt.viewmodel

import androidx.lifecycle.ViewModel
import com.cyens.retrofituploadfilekt.models.FileUploadResponse
import com.cyens.retrofituploadfilekt.repositories.FileRepository
import okhttp3.MultipartBody

class FileViewModel(
        private val fileRepository: FileRepository = FileRepository()
    ): ViewModel() {

        suspend fun uploadFile(file: MultipartBody.Part): FileUploadResponse {
            return fileRepository.uploadFile(file)
        }
}