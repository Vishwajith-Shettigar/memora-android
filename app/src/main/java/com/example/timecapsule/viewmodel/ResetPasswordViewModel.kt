package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
  object Error : ResetPasswordState()

}

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
  private val getResetPasswordEmailUseCase: GetResetPasswordEmailUseCase,
  private val getUserEmailUseCase: GetUserEmailUseCase
) : ViewModel() {

  private val _resetPasswordState = MutableStateFlow<ResetPasswordState>(ResetPasswordState.Idle)
  val resetPasswordState: StateFlow<ResetPasswordState> = _resetPasswordState

  var email: Response<String>

  init {
    email = getUserEmailUseCase()
  }

  fun sendPasswordResetEmail() {
    viewModelScope.launch {
      _resetPasswordState.value = ResetPasswordState.Loading
      withContext(Dispatchers.IO) {
        val response = getResetPasswordEmailUseCase()
        _resetPasswordState.value = when (response) {
          is Response.Success -> {
            ResetPasswordState.Success
          }

          is Response.Error -> {
            ResetPasswordState.Error
          }
        }
      }
    }
  }
}
