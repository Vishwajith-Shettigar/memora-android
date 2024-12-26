package com.example.timecapsule.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.Load3dModelUseCase
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ArViewModel(
  private val load3dModelUseCase: Load3dModelUseCase
) : ViewModel() {
  private val _loading3dModelState = MutableStateFlow<Load3dModelState>(Load3dModelState.Idle)
  val loadingLoad3dModelState: StateFlow<Load3dModelState> = _loading3dModelState

  fun loadModel(modelId: String) {
    _loading3dModelState.value = Load3dModelState.Loading
    viewModelScope.launch(Dispatchers.IO) {
      val res = load3dModelUseCase(modelId)
      when (res) {
        is Response.Success -> {
          _loading3dModelState.value = Load3dModelState.Success(path = res.data!!)
        }

        is Response.Error -> {
          _loading3dModelState.value = Load3dModelState.Error
        }

        null -> {}
      }
    }
  }
}
