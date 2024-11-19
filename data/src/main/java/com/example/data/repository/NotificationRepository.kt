package com.example.data.repository

import com.example.data.dto.NotificationDto
import com.example.data.remote.NotificationDataSource
import com.example.data.retrofilApi.NotificationApi
import com.example.model.NotificationDetails
import com.example.util.Response
import javax.inject.Inject

interface NotificationRepository {
  suspend fun getNotifications(): Response<List<NotificationDetails>>
  suspend fun sendCapsuleCreationNotification(notification: NotificationDto)

}

class NotificationRepositoryImpl @Inject constructor(
  private val notificationDataSource: NotificationDataSource,
  private val api: NotificationApi
) : NotificationRepository {
  override suspend fun getNotifications(): Response<List<NotificationDetails>> {
    return notificationDataSource.getNotifications()
  }

  override suspend fun sendCapsuleCreationNotification(notification: NotificationDto) {
    api.sendCapsuleCreationNotifications(notification)
  }
}
