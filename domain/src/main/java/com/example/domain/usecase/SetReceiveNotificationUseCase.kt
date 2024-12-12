package com.example.domain.usecase

import com.example.data.repository.UserRepository
import com.example.util.Response
import javax.inject.Inject

class SetReceiveNotificationUseCase @Inject constructor(
  private val userRepository: UserRepository
) {

  suspend operator fun invoke(isEnabled: Boolean): Response<Unit> {
    return userRepository.setReceiveNotification(isEnabled)
  }
}
