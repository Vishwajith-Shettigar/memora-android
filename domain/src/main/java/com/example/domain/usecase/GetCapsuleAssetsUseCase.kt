package com.example.domain.usecase

import com.example.data.repository.CapsulesRepository
import com.example.model.CapsuleAsset
import com.example.util.Response
import javax.inject.Inject

class GetCapsuleAssetsUseCase @Inject constructor(
  private val capsulesRepository: CapsulesRepository
) {
  suspend operator fun invoke(): Response<List<CapsuleAsset>> {
    return capsulesRepository.getCapsuleAssets()
  }
}
