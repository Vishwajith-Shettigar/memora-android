package com.example.timecapsule.viewmodel

import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.FetchNearByCapsulesUseCase
import com.example.domain.usecase.Load3dModelUseCase
import com.example.model.NearByCapsule
import com.example.util.Response
import com.google.type.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


@HiltViewModel
class NearByCapsulesViewModel @Inject constructor(
  private val fetchNearByCapsulesUseCase: FetchNearByCapsulesUseCase,
  private val load3dModelUseCase: Load3dModelUseCase
) : ViewModel() {

  private val _loading3dModelState = MutableStateFlow<Load3dModelState>(Load3dModelState.Idle)
  val loadingLoad3dModelState: StateFlow<Load3dModelState> = _loading3dModelState

  var selectedCapsule by mutableStateOf<NearByCapsule?>(null)

  var isCapsuleSelected by
  mutableStateOf(false)

  fun setModelLoadingStateIdle() {
    _loading3dModelState.value = Load3dModelState.Idle
  }

  fun fetchNearByCapsules(
    location: LatLng,
    radius: Double,
    onComplete: (List<NearByCapsule>) -> Unit

  ) {
    fetchNearByCapsulesUseCase(location, radius, onComplete)
  }

  fun loadModel() {
    _loading3dModelState.value = Load3dModelState.Loading
    viewModelScope.launch(Dispatchers.IO) {
      val res = selectedCapsule?.let { load3dModelUseCase(it.modelId) }
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