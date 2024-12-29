package com.example.data.remote

import android.content.Context
import android.util.Log
import com.example.util.Response
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class ThreeDModelsDataSource @Inject constructor(
  private val context: Context,
  private val firebaseStorage: FirebaseStorage
) {
  suspend fun downloadModelToCache(modelId: String): Response<String> {
    return try {
      val modelName = "model_" + modelId + ".glb"
      val modelPath = "models/" + modelName
      val cacheDir = context.cacheDir
      val localFile = File(cacheDir, modelPath.substringAfterLast('/'))
      firebaseStorage.getReference(modelPath).getFile(localFile).await()
      Response.Success(localFile.absolutePath)
    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }
}
