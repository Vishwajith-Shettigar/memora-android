package com.example.data.repository

import android.net.Uri
import com.example.data.remote.FilesRemoteDataSource
import com.example.model.DownloadFile
import com.example.model.FileUploadProgress
import com.example.model.FileUploaded
import java.util.ArrayList
import javax.inject.Inject

interface UploadFileRepository {
  suspend fun uploadFiles(uri: Uri,capsuleId:String)
  fun getUploadProgress(): List<FileUploadProgress>
  fun getUploadedFiles(): List<FileUploaded>
  fun cancelUpLoading(uri: Uri)
  fun cancelAllUpLoading()
 suspend fun deleteUploadedFile(uri: Uri,capsuleId: String)
 suspend fun downloadFiles(file: DownloadFile):ByteArray?
}

class UploadFileRepositoryImpl @Inject constructor(
  private val filesRemoteDataSource: FilesRemoteDataSource
) : UploadFileRepository {
  override suspend fun uploadFiles(uri: Uri,capsuleId:String) {
    filesRemoteDataSource.uploadFileToFirebase(uri,capsuleId)
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

  override suspend fun deleteUploadedFile(uri: Uri, capsuleId: String) {
    filesRemoteDataSource.deleteUploadedFile(uri,capsuleId)
  }

  override suspend fun downloadFiles(file:DownloadFile):ByteArray? {
   return filesRemoteDataSource.downloadFile(file)
  }
}
