package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetNotificationUseCase
import com.example.model.CapsuleDetails
import com.example.model.NotificationDetails
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class NotificationScreenState {
  object Idle : NotificationScreenState()
  object Loading : NotificationScreenState()
  class Success(
    val notificationList: List<NotificationDetails>
  ) : NotificationScreenState()

  data class Error(val message: String, val exception: Exception? = null) :
    NotificationScreenState()
}

@HiltViewModel
class NotificatioViewModel @Inject constructor(
  private val getNotificationUseCase: GetNotificationUseCase
) : ViewModel() {
  private val _notificationListState =
    MutableStateFlow<NotificationScreenState>(NotificationScreenState.Loading)
  val notificationListState: StateFlow<NotificationScreenState> = _notificationListState

  fun getNotifications() {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val result = getNotificationUseCase()
        _notificationListState.value = when (result) {
          is Response.Success -> {
            NotificationScreenState.Success(result.data!!)
          }

          is Response.Error -> {
            NotificationScreenState.Error(
              result.exception.message.toString(),
              result.exception
            )
          }
        }
      }
    }
  }
}
