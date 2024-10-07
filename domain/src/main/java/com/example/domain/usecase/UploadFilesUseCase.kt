package com.example.domain.usecase

import android.net.Uri
import com.example.data.repository.UploadFileRepository
import com.example.model.FileUploadProgress
import com.example.model.FileUploaded
import javax.inject.Inject

class UploadFilesUseCase @Inject constructor(
  private val uploadFileRepository: UploadFileRepository
) {
  suspend fun uploadFile(uri: Uri) {
    uploadFileRepository.uploadFiles(uri)
  }

  fun getUploadProgress(): List<FileUploadProgress> {
    return uploadFileRepository.getUploadProgress()
  }

  fun getUploadedFiles(): List<FileUploaded> {
    return uploadFileRepository.getUploadedFiles()
  }

  fun cancelFileUploding(uri: Uri) {
    uploadFileRepository.cancelUpLoading(uri)
  }

  fun cancelAllFilesUploading() {
    uploadFileRepository.cancelAllUpLoading()
  }

  fun deleteUploadedFile(uri: Uri) {
    uploadFileRepository.deleteUploadedFile(uri)
  }
}
