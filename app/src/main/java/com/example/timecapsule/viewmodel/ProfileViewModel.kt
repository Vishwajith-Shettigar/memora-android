package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetProfileUseCase
import com.example.domain.usecase.UpdateProfileUseCase
import com.example.model.Profile
import com.example.model.UpdateProfile
import com.example.model.UserDetails
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class ProfileState {
  object Loading : ProfileState()
  data class Success(val data: Profile) : ProfileState()
  data class Error(val message: String? = null, val exception: Exception? = null) : ProfileState()
}

sealed class EditProfileState {
  object Idle : EditProfileState()
  object Loading : EditProfileState()
  object Success : EditProfileState()
  data class Error(val message: String? = null, val exception: Exception? = null) :
    EditProfileState()
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
  private val getProfileUseCase: GetProfileUseCase,
  private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {
  private val _profile = MutableStateFlow<ProfileState>(ProfileState.Loading)
  val profile: StateFlow<ProfileState> = _profile

  private val _editProfileState = MutableStateFlow<EditProfileState>(EditProfileState.Idle)
  val editProfileState: StateFlow<EditProfileState> = _editProfileState

  fun resetEditProfileState() {
    _editProfileState.value = EditProfileState.Idle
  }

  fun updateProfle(updateProfile: UpdateProfile) {
    _editProfileState.value = EditProfileState.Loading
    viewModelScope.launch(Dispatchers.IO) {
      val response = updateProfileUseCase(updateProfile)

      withContext(Dispatchers.Main)
      {
        when (response) {
          is Response.Success -> {
            _editProfileState.value = EditProfileState.Success
          }

          is Response.Error -> {
            _editProfileState.value = EditProfileState.Error(
              message = "Something went wrong !",
              exception = response.exception
            )
          }
        }
      }
    }
  }

  fun getProfile() {
    viewModelScope.launch(Dispatchers.IO) {
      val response = getProfileUseCase()
Log.e("pokemon",response.toString())
      withContext(Dispatchers.Main) {
        when (response) {
          is Response.Success -> {
            _profile.value = ProfileState.Success(data = response.data!!)
          }

          is Response.Error -> {
            _profile.value =
              ProfileState.Error(
                exception = response.exception,
                message =
                response.exception.message
              )
          }
        }
      }
    }
  }
}
