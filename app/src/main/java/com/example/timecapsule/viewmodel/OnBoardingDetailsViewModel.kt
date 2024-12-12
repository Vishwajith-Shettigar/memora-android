package com.example.timecapsule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.OnBoardingDataUseCase
import com.example.domain.usecase.SaveUserDetailsUseCase
import com.example.domain.usecase.SignOutUseCase
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OnBoardingDetailsViewModel @Inject constructor(
  private val saveUserDetailsUseCase: SaveUserDetailsUseCase,
  private val onBoardingDataUseCase: OnBoardingDataUseCase,
  private val signOutUseCase: SignOutUseCase
) : ViewModel() {

  private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
  val authState: StateFlow<AuthState> = _authState

  fun saveDetails(userName: String, fName: String, lName: String) {
    viewModelScope.launch {
      _authState.value = AuthState.Loading
      val result = saveUserDetailsUseCase(userName, fName, lName)
      _authState.value = when (result) {
        is Response.Success -> {
          onBoardingDataUseCase.setOnBoardingDetailsCompleted(true)
          AuthState.Success
        }

        is Response.Error -> AuthState.Error(result.exception.message.toString(), result.exception)
      }
    }
  }

  fun signOut() {
    viewModelScope.launch {
      signOutUseCase()
    }
  }
}
