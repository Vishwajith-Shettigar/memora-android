package com.example.timecapsule.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCapsuleDetailsUseCase
import com.example.domain.usecase.OpenCapsuleScreenCheckPointUseCase
import com.example.model.CapsuleDetails
import com.example.model.DownloadFile
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.service.FileDownloadService
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.ArrayList
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Route

data class CombinedState(
  val checkpoint: String?,
  val capsuleDetailsState: DisplayCapsuleDetailsState
)

@HiltViewModel
class OpenCapsuleViewModel @Inject constructor(
  private val openCapsuleScreenCheckPointUseCase: OpenCapsuleScreenCheckPointUseCase,
  private val getCapsuleDetailsUseCase: GetCapsuleDetailsUseCase,
  @ApplicationContext private val context: Context
) : ViewModel() {

  private var CAPSULE_ID: String? = null

  private val _screenCheckPoint = MutableStateFlow<String?>(null)

  private val _capsuleDetailsState =
    MutableStateFlow<DisplayCapsuleDetailsState?>(null)
  val capsuleDetailsState: StateFlow<DisplayCapsuleDetailsState?> = _capsuleDetailsState

  val combinedState: StateFlow<CombinedState?> = combine(
    _screenCheckPoint,
    _capsuleDetailsState
  ) { checkpoint, capsuleDetailsState ->
    if (checkpoint != null && capsuleDetailsState != null) {
      CombinedState(checkpoint, capsuleDetailsState)
    } else {
      null
    }
  }.filterNotNull()
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      null
    )


  fun getCapsuleDetails(capsuleId: String) {
    CAPSULE_ID = capsuleId
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val response = getCapsuleDetailsUseCase(capsuleId)

        when (response) {
          is Response.Success -> {
            _capsuleDetailsState.value =
              DisplayCapsuleDetailsState.Success(response.data!!)
          }

          is Response.Error -> {
            _capsuleDetailsState.value =
              DisplayCapsuleDetailsState.Error(response.exception)
          }
        }
      }
    }
  }

  fun getScreenCheckPoint(capsuleId: String) {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        delay(2000)
        val route: String? =
          openCapsuleScreenCheckPointUseCase.getCapsuleOpeningLastScreenRoute(capsuleId)
        if (route == null) {
          openCapsuleScreenCheckPointUseCase.saveCapsuleOpeningLastScreenRoute(
            route = Screen.OpenCapsuleInstructionsScreen.route,
            capsuleId = capsuleId
          )
          _screenCheckPoint.value = Screen.OpenCapsuleInstructionsScreen.route
        }
        route?.let {
          _screenCheckPoint.value = it
        }
      }
    }
  }

  fun saveScreenCheckPoint(route: String) {
    openCapsuleScreenCheckPointUseCase.saveCapsuleOpeningLastScreenRoute(
      route,
      capsuleId = CAPSULE_ID!!
    )
  }

  private val _progress = MutableStateFlow(0)
  val progress: StateFlow<Int> get() = _progress

  fun startDownloadService(fileUrls: ArrayList<DownloadFile>) {
    val intent = Intent(context, FileDownloadService::class.java).apply {
      putParcelableArrayListExtra("fileUrls",fileUrls)
    }
    context.startForegroundService(intent)

    context.registerReceiver(DownloadProgressReceiver(), IntentFilter("com.example.timecapsule.DOWNLOAD_COMPLETE"),
      Context.RECEIVER_EXPORTED)
  }

  inner class DownloadProgressReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      Log.e("error","reeueiw")

      val progress = intent?.getIntExtra("progress", 0) ?: 0
      Log.e("error","yoyoyo")
      _progress.value = progress
    }
  }
}
