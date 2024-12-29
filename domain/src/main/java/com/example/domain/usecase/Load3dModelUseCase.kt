package com.example.domain.usecase

import com.example.data.repository.ThreeDModelRepository
import com.example.util.Response
import javax.inject.Inject

class Load3dModelUseCase @Inject constructor(
  private val repository: ThreeDModelRepository
) {
  suspend operator fun invoke(modelId: String): Response<String> {
    return repository.get3dModelPath(modelId)
  }
}
