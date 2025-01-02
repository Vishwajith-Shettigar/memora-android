package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCapsuleDetailsUseCase
import com.example.domain.usecase.Load3dModelUseCase
import com.example.model.CapsuleDetails
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


sealed class DisplayCapsuleDetailsState {
  class Success(val capsuleDetails: CapsuleDetails) : DisplayCapsuleDetailsState()
  class Error(e: Exception) : DisplayCapsuleDetailsState()
  object Loading : DisplayCapsuleDetailsState()
}

@HiltViewModel
class DisplayCapsuleDetailsViewModel @Inject constructor(
  private val getCapsuleDetailsUseCase: GetCapsuleDetailsUseCase,
  private val load3dModelUseCase: Load3dModelUseCase
) : ViewModel() {

  private val _capsuleDetailsState =
    MutableStateFlow<DisplayCapsuleDetailsState>(DisplayCapsuleDetailsState.Loading)
  val capsuleDetailsState: StateFlow<DisplayCapsuleDetailsState> = _capsuleDetailsState

  private val _loading3dModelState = MutableStateFlow<Load3dModelState>(Load3dModelState.Idle)
  val loadingLoad3dModelState: StateFlow<Load3dModelState> = _loading3dModelState

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

  fun set3dModelLoadingStateError(){
    _loading3dModelState.value = Load3dModelState.Error
  }

  fun load3dModel(modelId: String,retry:Boolean=false) {
    _loading3dModelState.value = Load3dModelState.Loading
    viewModelScope.launch(Dispatchers.IO) {
      val res = load3dModelUseCase(modelId = modelId,retry=retry)
      when (res) {
        is Response.Success -> {
          _loading3dModelState.value = Load3dModelState.Success(path = res.data!!)
        }

        is Response.Error -> {
          _loading3dModelState.value = Load3dModelState.Error
        }
      }
    }
  }
}
