package com.example.model

import android.net.Uri
import com.google.firebase.Timestamp

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
  val fileUri:Uri,
  var totalSize: Long = 0,
  val fileType: String = "unknown"
)

data class TempUploaded(
  val uri: Uri,
  val userId: String,
  val timeStamp: Timestamp,
)