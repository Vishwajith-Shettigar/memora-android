package com.example.timecapsule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.CanResetPasswordCounterUseCase
import com.example.domain.usecase.GetResetPasswordEmailUseCase
import com.example.domain.usecase.GetUserEmailUseCase
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class ResetPasswordState {
  object Idle : ResetPasswordState()
  object Success : ResetPasswordState()
  object Loading : ResetPasswordState()
  class Error(val message: String?) : ResetPasswordState()
}

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
  private val getResetPasswordEmailUseCase: GetResetPasswordEmailUseCase,
  getUserEmailUseCase: GetUserEmailUseCase,
  private val canResetPasswordCounterUseCase: CanResetPasswordCounterUseCase
) : ViewModel() {

  private val _resetPasswordState = MutableStateFlow<ResetPasswordState>(ResetPasswordState.Idle)
  val resetPasswordState: StateFlow<ResetPasswordState> = _resetPasswordState

  var email: Response<String>

  init {
    email = getUserEmailUseCase()
  }

  fun sendPasswordResetEmail() {

    _resetPasswordState.value = ResetPasswordState.Loading
    val canSendResetEmail = canResetPasswordCounterUseCase()
    if (!canSendResetEmail) {
      _resetPasswordState.value =
        ResetPasswordState.Error(message = "Password reset limit exceeded, please try again tomorrow")
      return
    }
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val response = getResetPasswordEmailUseCase()
        _resetPasswordState.value = when (response) {
          is Response.Success -> {
            canResetPasswordCounterUseCase.incrementCounter()
            ResetPasswordState.Success
          }

          is Response.Error -> {
            ResetPasswordState.Error(null)
          }
        }
      }
    }
  }
}
