package com.example.timecapsule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetUserDetailsUseCase
import com.example.model.UserDetails
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ViewProfileState {
  class Success(val data: UserDetails) : ViewProfileState()
  object Loading : ViewProfileState()
  class Error(excpetion: Exception) : ViewProfileState()
}

@HiltViewModel
class ViewProfileViewModel @Inject constructor(
  private val getUserDetailsUseCase: GetUserDetailsUseCase
) : ViewModel() {

  private val _viewProfileState = MutableStateFlow<ViewProfileState>(ViewProfileState.Loading)
  val viewProfileState: StateFlow<ViewProfileState> = _viewProfileState

  fun loadUserDetails(userId: String) {
    _viewProfileState.value = ViewProfileState.Loading
    viewModelScope.launch(Dispatchers.IO) {

      val res = getUserDetailsUseCase(userId)

      when (res) {
        is Response.Success -> {
          _viewProfileState.value = ViewProfileState.Success(data = res.data!!)
        }

        is Response.Error -> {
          _viewProfileState.value = ViewProfileState.Error(excpetion = res.exception)
        }
      }
    }
  }
}
