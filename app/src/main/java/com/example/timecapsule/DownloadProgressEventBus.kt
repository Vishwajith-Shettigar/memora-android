package com.example.timecapsule

import android.content.Context
import com.example.data.sharedpreference.ThemePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Singleton to manage progress state
object DownloadProgressEventBus {
  val scope = CoroutineScope(Dispatchers.IO)
  private val _progressFlow = MutableSharedFlow<Int>(replay = 0)
  val progressFlow: SharedFlow<Int> = _progressFlow.asSharedFlow()

  fun send(progress: Int) {
    scope.launch {
      _progressFlow.emit(progress)
    }
  }

  fun cancelScope() {
    scope.cancel()
  }
}
