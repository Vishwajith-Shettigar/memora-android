package com.example.model

import android.net.Uri

data class FileUploadProgress(
  val fileName: String,
  val uri: Uri,
  var progress: Double = 0.0,
  var totalSize: Long = 0,
  var uploadedSize: Long = 0,
  var isFailed: Boolean = false,
  val fileType: String = "unknown"
)

data class FileUploaded(
  val fileName: String,
  val uri: Uri,
  var totalSize: Long = 0,
  val fileType: String = "unknown"
)