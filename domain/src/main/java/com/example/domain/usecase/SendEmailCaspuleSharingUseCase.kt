package com.example.domain.usecase

import com.example.data.dto.EmailSharingCapsuleDto
import com.example.data.repository.CapsulesRepository
import com.example.util.Response
import javax.inject.Inject

class SendEmailCaspuleSharingUseCase @Inject constructor(
  private val capsulesRepository: CapsulesRepository
) {
  suspend operator fun invoke(emailSharingCapsuleDto: EmailSharingCapsuleDto): Response<Unit> {
    return capsulesRepository.sendEmailCaspuleSharing(emailSharingCapsuleDto)
  }
}
