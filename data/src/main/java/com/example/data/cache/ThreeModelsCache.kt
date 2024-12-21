package com.example.data.cache

import android.content.Context
import java.io.File
import javax.inject.Inject

class ThreeModelsCache @Inject constructor(private val context: Context) {
  fun getModelFromCache(modelName: String): String? {
    val cacheDir = context.cacheDir
    val modelFile = File(cacheDir, modelName)
    return if (modelFile.exists()) modelFile.absolutePath else null
  }
}
