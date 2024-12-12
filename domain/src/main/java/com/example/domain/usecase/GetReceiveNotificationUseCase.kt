package com.example.domain.usecase

import com.example.data.repository.UserRepository
import com.example.util.Response
import javax.inject.Inject

class GetReceiveNotificationUseCase @Inject constructor(
  private val userRepository: UserRepository
) {

  suspend operator fun invoke(): Response<Boolean> {
    return userRepository.getReceiveNotification()
  }
}
