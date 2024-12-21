package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.Load3dModelUseCase
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class Load3dModelState {
  class Success(val path: String) : Load3dModelState()
  object Error : Load3dModelState()
  object Idle : Load3dModelState()
  object Loading : Load3dModelState()
}

@HiltViewModel
class ViewCapsuleViewModel @Inject constructor(
  private val load3dModelUseCase: Load3dModelUseCase
) : ViewModel() {

  private val _loading3dModelState = MutableStateFlow<Load3dModelState>(Load3dModelState.Idle)
  val loadingLoad3dModelState: StateFlow<Load3dModelState> = _loading3dModelState

  fun load3dModel(modelId: String) {
    _loading3dModelState.value = Load3dModelState.Loading
    viewModelScope.launch(Dispatchers.IO) {
      val res = load3dModelUseCase(modelId = modelId)
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
