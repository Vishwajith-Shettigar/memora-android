package com.example.model

import com.google.firebase.Timestamp

data class NotificationDetails(
  val body: String, val capsuleId: String?, val timestamp: Timestamp,
  val imageUrl: String, val username:String
)
