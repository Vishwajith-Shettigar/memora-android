package com.example.domain.usecase

import com.example.data.repository.CapsulesRepository
import com.example.model.CapsuleAsset
import com.example.model.CapsuleDetails
import com.example.util.Response
import com.google.android.gms.common.internal.safeparcel.SafeParcelable.Constructor
import javax.inject.Inject

class CreateCapsuleUseCase @Inject constructor(
  private val capsulesRepository: CapsulesRepository
) {
  suspend operator fun invoke(capsuleDetails: CapsuleDetails): Response<Unit> {
    return capsulesRepository.createCapsule(capsuleDetails)
  }
}