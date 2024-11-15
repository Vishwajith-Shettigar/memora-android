package com.example.model

import com.google.firebase.Timestamp

data class NotificationDetails(val body: String,val title: String, val capsuleId: String?, val timestamp: Timestamp)
