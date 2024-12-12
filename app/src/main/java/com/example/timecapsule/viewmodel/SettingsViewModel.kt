package com.example.timecapsule.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.usecase.GetReceiveNotificationCacheUseCase
import com.example.domain.usecase.GetShareCapsulesCacheUseCase
import com.example.domain.usecase.GetShareCapsulesUseCase
import com.example.domain.usecase.SetReceiveNotificationCacheUseCase
import com.example.domain.usecase.SetReceiveNotificationUseCase
import com.example.domain.usecase.SetShareCapsulesCacheUseCase
import com.example.domain.usecase.SetShareCapsulesUseCase
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val getShareCapsulesCacheUseCase: GetShareCapsulesCacheUseCase,
  private val setShareCapsulesCacheUseCase: SetShareCapsulesCacheUseCase,
  private val getReceiveNotificationCacheUseCase: GetReceiveNotificationCacheUseCase,
  private val setReceiveNotificationCacheUseCase: SetReceiveNotificationCacheUseCase,
  private val setShareCapsulesUseCase: SetShareCapsulesUseCase,
  private val setReceiveNotificationUseCase: SetReceiveNotificationUseCase
) : ViewModel() {

  private val _receiveNotifications = MutableStateFlow(true)
  val receiveNotifications: StateFlow<Boolean> = _receiveNotifications

  private val _canSharCapsules = MutableStateFlow(true)
  val canSharCapsules: StateFlow<Boolean> = _canSharCapsules

  init {
    viewModelScope.launch(Dispatchers.IO) {

      async {
        if (isActive) {
          _receiveNotifications.value = getReceiveNotificationCacheUseCase()
        }
      }

      async {
        if (isActive) {
          _canSharCapsules.value = getShareCapsulesCacheUseCase()
        }
      }
    }
  }

  fun changeIsReceiveNotifications(isEnabled: Boolean) {
    _receiveNotifications.value = isEnabled
    viewModelScope.launch(Dispatchers.IO) {
      val res = setReceiveNotificationUseCase(isEnabled)
      if (res is Response.Success)
        setReceiveNotificationCacheUseCase(isEnabled)
      else {
        _receiveNotifications.value = !isEnabled
      }
    }
  }

  fun changeCanShareCapsules(isEnabled: Boolean) {
    _canSharCapsules.value = isEnabled
    viewModelScope.launch(Dispatchers.IO) {
      val res = setShareCapsulesUseCase(isEnabled)
      if (res is Response.Success)
        setShareCapsulesCacheUseCase(isEnabled)
      else {
        _canSharCapsules.value = !isEnabled
      }
    }
  }
}
