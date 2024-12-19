package com.example.domain.usecase

import com.example.data.repository.UserRepository
import com.example.util.Response
import javax.inject.Inject


class GetResetPasswordEmailUseCase @Inject constructor(
  private val userRepository: UserRepository
) {
  suspend operator fun invoke(): Response<Unit> {
    return userRepository.sendResetPasswordEmail()
  }
}
