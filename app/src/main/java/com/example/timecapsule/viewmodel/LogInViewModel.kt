package com.example.timecapsule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.SignInUseCase
import com.example.domain.usecase.SignUpUseCase
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LogInViewModel @Inject constructor(
 private val signInUseCase: SignInUseCase
) : ViewModel() {

  private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
  val authState: StateFlow<AuthState> = _authState

  fun signIn( email: String, password: String) {
    viewModelScope.launch {
      _authState.value = AuthState.Loading
      val result = signInUseCase(email = email, password = password)
      _authState.value = when (result) {
        is Response.Success -> AuthState.Success
        is Response.Error -> AuthState.Error(result.exception.message.toString(), result.exception)
      }
    }
  }
}