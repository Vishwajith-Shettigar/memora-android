package com.example.domain.usecase

import com.example.data.repository.UserRepository
import com.example.util.Response
import javax.inject.Inject

class GetShareCapsulesUseCase @Inject constructor(
  private val userRepository: UserRepository
) {

  suspend operator fun invoke(): Response<Boolean> {
    return userRepository.getShareCapsules()
  }
}
