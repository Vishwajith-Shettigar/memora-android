package com.example.data.remote

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.example.model.FileUploadProgress
import com.example.model.FileUploaded
import com.example.util.getFileSizeAndName
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class FilesRemoteDataSource @Inject
constructor(
  private val firebaseStorage: FirebaseStorage,
  private val context: Context
) {

  fun getFileType(uri: Uri): String? {
    val mimeType = context.contentResolver.getType(uri)
    return mimeType?.substringAfter("/")
  }

  // Global or class-level variable to keep track of the uploads
  private val uploadProgressMap = mutableMapOf<Uri, FileUploadProgress>()
  private val uploadedFiles = mutableListOf<FileUploaded>()

  fun uploadFileToFirebase(fileUri: Uri) {
    try {
      val fileReference = firebaseStorage.reference.child("uploads/${fileUri.lastPathSegment}")
      // Get the total size of the file
      val pair: Pair<Long, String> = getFileSizeAndName(fileUri, context)
      val totalSize = pair.first
      val fileName = pair.second
      val mimeType = getFileType(fileUri)

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
          uploadProgressMap[fileUri]?.isFailed = true
        }
        .addOnProgressListener { taskSnapshot ->
          // Track progress
          val bytesTransferred = taskSnapshot.bytesTransferred
          val totalByteCount = taskSnapshot.totalByteCount

          // Update upload progress
          val progress = (100.0 * bytesTransferred / totalByteCount)
          // Update progress in the map
          uploadProgressMap[fileUri]?.apply {
            this.progress = progress
            this.uploadedSize = bytesTransferred
          }
        }
    } catch (e: Exception) {
    }
  }

  // Function to get upload progress for a specific file
  fun getUploadProgress(): List<FileUploadProgress> {
    return uploadProgressMap.values.toList()
  }

  fun getUploadedFiles(): List<FileUploaded> {
    return uploadedFiles.toList()
  }

  // TODO: when user exits without creating capsule.
  fun unRegisterStorageOperations() {
  }
}
