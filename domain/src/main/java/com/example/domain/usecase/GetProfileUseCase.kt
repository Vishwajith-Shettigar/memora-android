package com.example.domain.usecase

import com.example.data.repository.UserRepository
import com.example.model.Profile
import com.example.util.Response
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
  private val userRepository: UserRepository
) {
  suspend operator fun invoke(): Response<Profile> {
    return userRepository.getProfile()
  }
}
