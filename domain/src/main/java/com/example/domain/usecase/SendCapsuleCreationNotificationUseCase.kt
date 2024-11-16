package com.example.domain.usecase

import com.example.data.dto.NotificationDto
import com.example.data.repository.NotificationRepository

class SendCapsuleCreationNotificationUseCase
  (
  private val repository: NotificationRepository
) {
  suspend operator fun invoke(notification: NotificationDto) {
    repository.sendCapsuleCreationNotification(notification)
  }
}