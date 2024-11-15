package com.example.data.repository

import com.example.data.remote.NotificationDataSource
import com.example.model.NotificationDetails
import com.example.util.Response
import javax.inject.Inject

interface NotificationRepository {
  suspend fun getNotifications(): Response<List<NotificationDetails>>
}

class NotificationRepositoryImpl @Inject constructor(
  private val notificationDataSource: NotificationDataSource
) : NotificationRepository {
  override suspend fun getNotifications(): Response<List<NotificationDetails>> {
    return notificationDataSource.getNotifications()
  }

}