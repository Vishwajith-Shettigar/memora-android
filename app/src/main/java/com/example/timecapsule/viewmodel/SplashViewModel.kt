package com.example.timecapsule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.usecase.getAuthUseCase
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SplashViewModel @Inject constructor(
  private val authUseCase: getAuthUseCase
) : ViewModel() {
  private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
  val authState: StateFlow<AuthState> = _authState
  fun invoke() {
    viewModelScope.launch {
      delay(1000)
      val result = authUseCase()
      _authState.value = when (result) {
        is Response.Success -> AuthState.Success
        is Response.Error -> AuthState.Error(result.exception.message.toString(), result.exception)
      }
    }
  }
}
