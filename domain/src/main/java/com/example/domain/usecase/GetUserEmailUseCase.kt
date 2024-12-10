package com.example.domain.usecase

import com.example.data.repository.UserRepository
import com.example.util.Response
import javax.inject.Inject

class GetUserEmailUseCase @Inject constructor(
  private val userRepository: UserRepository
) {
   operator fun invoke(): Response<String> {
    return userRepository.getUserEmail()
  }
}
