package com.example.domain.usecase

import com.example.data.repository.CapsulesRepository
import com.example.util.Response
import javax.inject.Inject

class SetCapsuleOpenedUseCase @Inject constructor(
  private val capsulesRepository: CapsulesRepository
) {
  suspend operator fun invoke(capsuleId: String): Response<Unit> {
    return capsulesRepository.setCapsuleOpened(capsuleId)
  }
}
