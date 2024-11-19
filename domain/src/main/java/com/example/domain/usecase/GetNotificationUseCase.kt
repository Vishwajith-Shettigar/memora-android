package com.example.domain.usecase

import com.example.data.repository.NotificationRepository
import com.example.model.NotificationDetails
import com.example.util.Response
import javax.inject.Inject

class GetNotificationUseCase @Inject constructor(
  private val notificationRepository: NotificationRepository
) {
  suspend operator fun invoke(): Response<List<NotificationDetails>> {
    return notificationRepository.getNotifications()
  }
}
