package com.example.timecapsule.service

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.domain.usecase.UpdateFCMTokenUseCase
import com.example.timecapsule.MainActivity
import com.example.timecapsule.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

const val CAPSULE_SHARED_NOTIFICATION = "CAPSULE_SHARED_NOTIFICATION"

class TimeCapsuleFirebaseMessagingService : FirebaseMessagingService() {

  val channelId = "capsule_notifications"
  val channelName = "Capsule Notifications"

  @Inject
  lateinit var updateFCMTokenUseCase: UpdateFCMTokenUseCase

  private val serviceJob = SupervisorJob()
  private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

  override fun onCreate() {
    super.onCreate()
    createNotificationChannel()
  }

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    serviceScope.launch {
      updateFCMTokenUseCase()
    }
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)
    val notificationType = remoteMessage.data["notificationType"]

    remoteMessage.notification?.let {
      val title = it.title ?: "Default Title"
      val body = it.body ?: "Default Body"

      val notificationBuilder = NotificationCompat.Builder(this, channelId)
        .setContentTitle(title)
        .setContentText(body)

      if (notificationType == CAPSULE_SHARED_NOTIFICATION) {
        val capsuleId = remoteMessage.data["capsuleId"]

        val intent = Intent(this, MainActivity::class.java).apply {
          putExtra("capsuleId", capsuleId)
          putExtra("notificationType", CAPSULE_SHARED_NOTIFICATION)
          flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
          this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        notificationBuilder.setContentIntent(pendingIntent)
      }

      val notification = notificationBuilder
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

      if (ActivityCompat.checkSelfPermission(
          this,
          Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
      ) {
        return
      }
      NotificationManagerCompat.from(this).notify(0, notification)
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    serviceJob.cancel()
  }

  private fun createNotificationChannel() {
    val importance = NotificationManager.IMPORTANCE_HIGH
    val channel = NotificationChannel(channelId, channelName, importance)
    channel.description = "Notifications for capsule updates and messages"

    val notificationManager = getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(channel)
  }
}
