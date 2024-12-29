package com.example.data.repository

import com.example.data.cache.ThreeModelsCache
import com.example.data.remote.ThreeDModelsDataSource
import com.example.util.Response
import javax.inject.Inject

interface ThreeDModelRepository {
  suspend fun get3dModelPath(modelId: String): Response<String>
}

class ThreeDModelRepositoryImpl @Inject constructor(
  private val threeDModelsDataSource: ThreeDModelsDataSource,
  private val threeModelsCache: ThreeModelsCache
) : ThreeDModelRepository {
  override suspend fun get3dModelPath(modelId: String): Response<String> {
    // Decide whether to fetch from cache or download from Firebase
    val cachedModelPath = threeModelsCache.getModelFromCache("model_$modelId.glb")
    return if (cachedModelPath != null) {
      Response.Success(cachedModelPath)
    } else {
      threeDModelsDataSource.downloadModelToCache(modelId)
    }
  }
}
