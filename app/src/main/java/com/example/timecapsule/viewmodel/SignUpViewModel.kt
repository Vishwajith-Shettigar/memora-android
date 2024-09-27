package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AuthRepository
import com.example.domain.usecase.SignUpUseCase
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
  object Idle : AuthState()
  object Loading : AuthState()
  object Success : AuthState()
  data class Error(val message: String) : AuthState()
}

@HiltViewModel
class SignUpViewModel @Inject constructor(
  val signUpUseCase: SignUpUseCase
) : ViewModel() {

  private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
  val authState: StateFlow<AuthState> = _authState

  fun signUp(userName: String, email: String, password: String) {
    viewModelScope.launch {
      _authState.value = AuthState.Loading
      val result = signUpUseCase(userName = userName, email = email, password = password)
      _authState.value = when (result) {
        is Response.Success -> AuthState.Success
        is Response.Error -> AuthState.Error(result.exception.message.toString())
      }
    }
  }
}