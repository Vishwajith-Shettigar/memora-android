package com.example.timecapsule.service

// FileDownloadService.kt
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Parcel
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.domain.usecase.DownloadFilesUseCase
import com.example.model.DownloadFile
import com.example.timecapsule.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class FileDownloadService : Service() {

  @Inject
  lateinit var downloadFilesUseCase: DownloadFilesUseCase

  private val notificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    val urls = intent?.getParcelableArrayListExtra<DownloadFile>("fileUrls") ?: arrayListOf()
    Log.e("error",urls.size.toString())
    startForeground(1, createNotification())

    CoroutineScope(Dispatchers.IO).launch {
      val totalFiles = urls.size
      var completedFiles = 0

      urls.forEach { url ->
        downloadFilesUseCase(url)?.let {
          completedFiles++
          val progress = (completedFiles * 100) / totalFiles
          sendProgressUpdate(progress)
        }
      }

      Log.e("error",completedFiles.toString()+"po")
      stopSelf()
    }

    return START_NOT_STICKY
  }

  private fun sendProgressUpdate(progress: Int) {
    val intent = Intent("com.example.timecapsule.DOWNLOAD_COMPLETE")
      .apply {
      putExtra("progress", progress)
    }
    sendBroadcast(intent)
  }

  private fun createNotification(): Notification {
    val channelId = "file_download_channel"
    val channel = NotificationChannel(channelId, "File Download", NotificationManager.IMPORTANCE_DEFAULT)
    notificationManager.createNotificationChannel(channel)

    return NotificationCompat.Builder(this, channelId)
      .setContentTitle("Downloading Files")
      .setContentText("Please wait...")
      .setSmallIcon(R.drawable.ic_downloading)
      .build()
  }

  override fun onBind(intent: Intent?): IBinder? = null

}
