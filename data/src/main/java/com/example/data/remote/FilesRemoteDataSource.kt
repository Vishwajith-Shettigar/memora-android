package com.example.data.remote

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.model.DownloadFile
import com.example.model.FileUploadProgress
import com.example.model.FileUploaded
import com.example.model.TempUploaded
import com.example.util.getFileSizeAndName
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileOutputStream
import java.util.ArrayList
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random
import kotlin.random.nextULong
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FilesRemoteDataSource @Inject
constructor(
  private val firebaseStorage: FirebaseStorage,
  private val context: Context,
  private val firestore: FirebaseFirestore,
  authRemoteDataSource: AuthRemoteDataSource
) {

  private val userId: String = authRemoteDataSource.getAuth()?.uid!!

  fun getFileType(uri: Uri): String? {
    val mimeType = context.contentResolver.getType(uri)
    return mimeType?.substringAfter("/")
  }

  // Global or class-level variable to keep track of the uploads
  private val uploadProgressMap = mutableMapOf<Uri, FileUploadProgress>()
  private val uploadedFiles = mutableMapOf<Uri, FileUploaded>()

  private val tempFilesMetaDataRef = mutableMapOf<Uri, String>()

  private val uploadTaskMap = mutableMapOf<Uri, UploadTask>()

  // To prevent from uploading duplicate file.
  private val uploadedFileList = mutableListOf<Uri>()

  // Remove file path from tempFilesMetaDataRef and uploadedFiles.
  suspend fun deleteUploadedFile(uri: Uri, capsuleId: String) {
    try {
      val fileUri = uploadedFiles[uri]?.fileUri!!
      uploadedFileList.remove(fileUri)
      uploadedFiles.remove(uri)
      tempFilesMetaDataRef.remove(uri)
      val filePath = filePathParser(fileUri)
      firebaseStorage.reference.child("uploads/${userId}/${capsuleId}/${filePath}")
        .delete()
        .await()
    } catch (_: Exception) {
    }
  }

  fun filePathParser(fileUri: Uri): String {
    return try {
      fileUri.path?.replace("/", "_") ?: fileUri.lastPathSegment.toString()
    } catch (_: Exception) {
      fileUri.lastPathSegment.toString()
    }
  }

  fun uploadFileToFirebase(fileUri: Uri, capsuleId: String) {
    try {
      if (uploadProgressMap.containsKey(fileUri) || uploadedFileList.contains(fileUri))
        return

      val filePath = filePathParser(fileUri)
      val fileReference =
        firebaseStorage.reference.child("uploads/${userId}/${capsuleId}/${filePath}")
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

      val uploadTask = fileReference.putFile(fileUri)
      uploadTaskMap[fileUri] = uploadTask
      uploadTask.addOnSuccessListener { taskSnapshot ->
        // Handle successful upload
        fileReference.downloadUrl.addOnSuccessListener { uri ->
          // Remove from progress map after upload completes
          if (uploadProgressMap[fileUri] != null) {
            uploadedFileList.add(fileUri)
            uploadedFiles[uri] =
              FileUploaded(
                fileName = uploadProgressMap[fileUri]?.fileName ?: "",
                uri = uri,
                fileUri = uploadProgressMap[fileUri]!!.uri,
                totalSize = totalSize,
                fileType = uploadProgressMap[fileUri]?.fileType ?: "unknown"
              )

            uploadProgressMap.remove(fileUri)
            CoroutineScope(Dispatchers.IO).launch {
              storeTempUploadedFileMetaData(uri)
            }
          }
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
    return uploadedFiles.values.toList()
  }

  // Store uploaded files metadata who's payment pending.
  suspend fun storeTempUploadedFileMetaData(uri: Uri) {
    try {
      val path = userId +
        Random.nextULong(0u, 10000u).toString() + Random.nextULong(0u, 10000u).toString()
      val tempFile = TempUploaded(uri = uri, userId = userId, timeStamp = Timestamp.now())
      firestore.collection("temp_files").document(
        path
      ).set(tempFile).await()
      tempFilesMetaDataRef[uri] = path
    } catch (_: Exception) {
    }
  }

  // When payment is done, delete all uploaded file metadata.
  // We only store file metadata when the user exits the app or cancels the capsule creation in the middle.
  // Later, we can delete all files for which payment hasn't been made.
  suspend fun deleteTempUploadedFileMetaData() {
    try {
      tempFilesMetaDataRef.values.forEach { path ->
        firestore.collection("temp_files").document(
          path
        ).delete().await()
      }
    } catch (_: Exception) {
    }
  }

  fun cancelUploadingProgressTask(fileUri: Uri) {
    uploadTaskMap[fileUri]?.cancel()
    uploadProgressMap.remove(fileUri)
  }

  fun cancelAllUploadings() {
    uploadTaskMap.forEach { _, uploadTask ->
      uploadTask.cancel()
    }
    uploadProgressMap.clear()
    uploadTaskMap.clear()
    uploadedFiles.clear()
    uploadedFileList.clear()
  }

   suspend fun downloadFile(file: DownloadFile): ByteArray? {
    return try {
      val ref = firebaseStorage.getReferenceFromUrl(file.url)
      val bytes = ref.getBytes(Long.MAX_VALUE).await()
      saveFileToStorage(fileContent =  bytes, file =file, context = context)
      bytes
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  fun saveFileToStorage(context: Context, file: DownloadFile, fileContent: ByteArray) {
    val resolver = context.contentResolver
    val contentValues = ContentValues().apply {
      put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
      put(MediaStore.MediaColumns.MIME_TYPE, getMimeTypeFromExtension(file.fileType))
      put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
    }
    val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
    uri?.let {
      resolver.openOutputStream(it).use { outputStream ->
        outputStream?.write(fileContent)
      }
    }

  }

  private fun getMimeTypeFromExtension(extension: String): String {
    return when (extension.lowercase(Locale.ROOT)) {
      "pdf" -> "application/pdf"
      "jpg", "jpeg" -> "image/jpeg"
      "png" -> "image/png"
      "txt" -> "text/plain"
      "zip" -> "application/zip"
      "mp3" -> "audio/mpeg"
      "mp4" -> "video/mp4"
      else -> "*/*"
    }
  }
}
