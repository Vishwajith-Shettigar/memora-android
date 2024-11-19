package com.example.data.retrofilApi

import com.example.data.dto.NotificationDto
import retrofit2.http.Body
import retrofit2.http.POST

interface NotificationApi {
  @POST("/capsule-creation-send-notification")
  suspend fun sendCapsuleCreationNotifications(@Body notificationDto: NotificationDto)
}
