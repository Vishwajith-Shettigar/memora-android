package com.example.domain.usecase

import com.example.data.repository.CapsulesRepository
import com.example.model.CapsuleDetails
import com.example.util.Response
import javax.inject.Inject

class GetCapsuleDetailsUseCase @Inject constructor(
  private val capsulesRepository: CapsulesRepository
) {
  suspend operator fun invoke(capsuleId:String):Response<CapsuleDetails>{
    return capsulesRepository.getCapsuleDetails(capsuleId)
  }
}