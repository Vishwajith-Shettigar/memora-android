package com.example.domain.usecase

import com.example.data.repository.UserRepository
import com.example.model.Profile
import com.example.model.UpdateProfile
import com.example.util.Response
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
  private val userRepository: UserRepository
) {
  suspend operator fun invoke(profile: UpdateProfile): Response<Unit> {
    return userRepository.updateProfile(profile)
  }
}
