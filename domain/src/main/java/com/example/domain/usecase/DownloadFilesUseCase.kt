package com.example.domain.usecase

import com.example.data.remote.FilesRemoteDataSource
import com.example.data.repository.UploadFileRepository
import com.example.model.DownloadFile
import java.util.ArrayList
import javax.inject.Inject

class DownloadFilesUseCase @Inject constructor(
  private val fileRepository: UploadFileRepository
) {
  suspend operator fun invoke(file:DownloadFile):ByteArray?{
    return fileRepository.downloadFiles(file)
  }

}