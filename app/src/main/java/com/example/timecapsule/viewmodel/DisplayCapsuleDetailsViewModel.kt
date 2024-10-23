package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.GetCapsuleDetailsUseCase
import com.example.model.CapsuleDetails
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import androidx.lifecycle.viewModelScope
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


sealed class DisplayCapsuleDetailsState {
  class Success(val calsuleDetails: CapsuleDetails) : DisplayCapsuleDetailsState()
  class Error(e: Exception) : DisplayCapsuleDetailsState()
  object Loading : DisplayCapsuleDetailsState()
}

@HiltViewModel
class DisplayCapsuleDetailsViewModel @Inject constructor(
  private val getCapsuleDetailsUseCase: GetCapsuleDetailsUseCase
) :ViewModel(){

  private val _capsuleDetailsState =
    MutableStateFlow<DisplayCapsuleDetailsState>(DisplayCapsuleDetailsState.Loading)
  val capsuleDetailsState: StateFlow<DisplayCapsuleDetailsState> = _capsuleDetailsState

  fun getCapsuleDetails(capsuleId: String) {

    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        val response = getCapsuleDetailsUseCase(capsuleId)

        when (response) {
          is Response.Success -> {
            _capsuleDetailsState.value =
              DisplayCapsuleDetailsState.Success(response.data!!)
          }

          is Response.Error -> {
            _capsuleDetailsState.value =
              DisplayCapsuleDetailsState.Error(response.exception)
          }
        }
      }

    }
  }

}