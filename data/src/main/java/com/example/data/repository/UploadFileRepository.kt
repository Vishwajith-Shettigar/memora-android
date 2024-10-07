package com.example.data.repository

import android.net.Uri
import com.example.data.remote.FilesRemoteDataSource
import com.example.model.FileUploadProgress
import com.example.model.FileUploaded
import javax.inject.Inject

interface UploadFileRepository {
  suspend fun uploadFiles(uri: Uri)
  fun getUploadProgress(): List<FileUploadProgress>
  fun getUploadedFiles(): List<FileUploaded>
  fun cancelUpLoading(uri: Uri)
  fun cancelAllUpLoading()
  fun deleteUploadedFile(uri: Uri)
}

class UploadFileRepositoryImpl @Inject constructor(
  private val filesRemoteDataSource: FilesRemoteDataSource
) : UploadFileRepository {
  override suspend fun uploadFiles(uri: Uri) {
    filesRemoteDataSource.uploadFileToFirebase(uri)
  }

  override fun getUploadProgress(): List<FileUploadProgress> {
    return filesRemoteDataSource.getUploadProgress()
  }

  override fun getUploadedFiles(): List<FileUploaded> {
    return filesRemoteDataSource.getUploadedFiles()
  }

  override fun cancelUpLoading(uri: Uri) {
    filesRemoteDataSource.cancelUploadingProgressTask(uri)
  }

  override fun cancelAllUpLoading() {
    filesRemoteDataSource.cancelAllUploadings()
  }

  override fun deleteUploadedFile(uri: Uri) {
    filesRemoteDataSource.deleteUploadedFile(uri)
  }
}
