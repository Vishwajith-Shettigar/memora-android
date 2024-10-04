package com.example.data.remote

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.model.FileUploadProgress
import com.example.model.FileUploaded
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlin.random.Random


class FilesRemoteDataSource @Inject
constructor(
  private val firebaseStorage: FirebaseStorage,
  private val context: Context
) {

  fun getFileType(uri: Uri): String? {
    val mimeType = context.contentResolver.getType(uri)
    return mimeType?.substringAfter("/") // This will return the string after the "/" in the MIME type
  }


  // Global or class-level variable to keep track of the uploads
  private val uploadProgressMap = mutableMapOf<Uri, FileUploadProgress>()
  private val uploadedFiles = mutableListOf<FileUploaded>()

  fun uploadFileToFirebase(fileUri: Uri) {
    try {


      val fileReference = firebaseStorage.reference.child("uploads/${fileUri.lastPathSegment}")

      // Get the total size of the file

      val pair: Pair<Long, String> = getFileSize(fileUri)
      val totalSize = pair.first
      val fileName = pair.second


      val mimeType = getFileType(fileUri)

      Log.e("#", totalSize.toString() + " " + fileName + " " + mimeType)


      // Initialize the upload progress for this file
      uploadProgressMap[fileUri] =
        FileUploadProgress(
          uri = fileUri,
          fileName = fileName,
          totalSize = totalSize,
          fileType = mimeType ?: "unknown"
        )

      fileReference.putFile(fileUri)
        .addOnSuccessListener { taskSnapshot ->
          // Handle successful upload
          fileReference.downloadUrl.addOnSuccessListener { uri ->
            Log.e("#", "File uploaded successfully. File URL: $uri")
            // Remove from progress map after upload completes
            uploadedFiles.add(
              FileUploaded(
                fileName = uploadProgressMap[fileUri]?.fileName ?: "",
                uri = uri,
                totalSize = totalSize,
                fileType = uploadProgressMap[fileUri]?.fileType ?: "unknown"
              )
            )
            uploadProgressMap.remove(fileUri)
          }
        }
        .addOnFailureListener { exception ->
          // Handle failure
          Log.e("#", "Upload failed: ${exception.message}")
          // Optionally remove from the map if it fails
          uploadProgressMap[fileUri]?.isFailed = true
        }
        .addOnProgressListener { taskSnapshot ->
          // Track progress
          val bytesTransferred = taskSnapshot.bytesTransferred
          val totalByteCount = taskSnapshot.totalByteCount

          // Update upload progress
          val progress = (100.0 * bytesTransferred / totalByteCount)
          Log.e("#", "Upload progress for ${fileUri.lastPathSegment}: $progress%")

          // Update progress in the map
          uploadProgressMap[fileUri]?.apply {
            this.progress = progress
            this.uploadedSize = bytesTransferred
          }
        }
    } catch (e: Exception) {
      Log.e("#", e.toString())
    }
  }

  // Function to get the size of the file
  private fun getFileSize(fileUri: Uri): Pair<Long, String> {
    var size: Long = 0
    var name: String = Random.nextLong(0, 100000).toString() + "xp1233op"
    // Get the file size using ContentResolver
    context.contentResolver.query(fileUri, null, null, null, null)?.use { cursor ->
      if (cursor.moveToFirst()) {
        val sizeColumnIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (sizeColumnIndex != -1) {
          size = cursor.getLong(sizeColumnIndex)
        } else {
          Log.e("File Size", "Column index for size not found.")
        }
        if (nameIndex != -1) {
          name = cursor.getString(nameIndex)
        }
      }
    }
    return Pair(size, name)
  }

  // Function to get upload progress for a specific file
  fun getUploadProgress(): List<FileUploadProgress> {
    Log.e("#","getUploadProgress")
    return uploadProgressMap.values.toList()
  }

  fun getUploadedFiles(): List<FileUploaded> {
    return uploadedFiles.toList()
  }
}
