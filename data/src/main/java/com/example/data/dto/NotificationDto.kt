package com.example.data.dto

data class NotificationDto(
  val userIds: List<String>,
  val title: String,
  val body: String,
  val capsuleId: String?,
  val userImageUrl:String,
  val username:String
)
