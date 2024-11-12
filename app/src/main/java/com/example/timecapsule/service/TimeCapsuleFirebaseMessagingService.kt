package com.example.timecapsule.service

import com.example.domain.usecase.UpdateFCMTokenUseCase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class TimeCapsuleFirebaseMessagingService : FirebaseMessagingService() {

  @Inject
  lateinit var updateFCMTokenUseCase: UpdateFCMTokenUseCase

  private val serviceJob = SupervisorJob()
  private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

  override fun onNewToken(token: String) {
    super.onNewToken(token)
    serviceScope.launch {
      updateFCMTokenUseCase()
    }
  }

  override fun onMessageReceived(remoteMessage: RemoteMessage) {
    super.onMessageReceived(remoteMessage)
  }

  override fun onDestroy() {
    super.onDestroy()
    serviceJob.cancel()
  }
}
