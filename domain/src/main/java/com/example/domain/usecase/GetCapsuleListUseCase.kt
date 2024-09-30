package com.example.domain.usecase

import com.example.data.repository.CapsulesRepository
import com.example.model.CapsuleDetails
import com.example.util.Response
import javax.inject.Inject

class GetCapsuleListUseCase @Inject constructor(
  private val capsulesRepository: CapsulesRepository
) {
  suspend operator fun invoke(): Response<List<CapsuleDetails>> {
    return capsulesRepository.getCapsulesList()
  }
}