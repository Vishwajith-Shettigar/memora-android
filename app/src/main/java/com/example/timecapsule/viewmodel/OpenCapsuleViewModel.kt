package com.example.timecapsule.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCapsuleDetailsUseCase
import com.example.domain.usecase.OpenCapsuleScreenCheckPointUseCase
import com.example.model.CapsuleDetails
import com.example.timecapsule.routes.Screen
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Route

data class CombinedState(
  val checkpoint: String?,
  val capsuleDetailsState: DisplayCapsuleDetailsState
)

@HiltViewModel
class OpenCapsuleViewModel @Inject constructor(
  private val openCapsuleScreenCheckPointUseCase: OpenCapsuleScreenCheckPointUseCase,
  private val getCapsuleDetailsUseCase: GetCapsuleDetailsUseCase
) : ViewModel() {

  private var CAPSULE_ID: String? = null

  private val _screenCheckPoint = MutableStateFlow<String?>(null)
  val screenCheckPoint: StateFlow<String?> = _screenCheckPoint

  private val _capsuleDetailsState =
    MutableStateFlow<DisplayCapsuleDetailsState?>(null)
  val capsuleDetailsState: StateFlow<DisplayCapsuleDetailsState?> = _capsuleDetailsState

  val combinedState: StateFlow<CombinedState?> = combine(
    _screenCheckPoint,
    _capsuleDetailsState
  ) { checkpoint, capsuleDetailsState ->
    if (checkpoint != null && capsuleDetailsState != null) {
      CombinedState(checkpoint, capsuleDetailsState)
    } else {
      null
    }
  }.filterNotNull()
    .stateIn(
      viewModelScope,
      SharingStarted.WhileSubscribed(5000),
      null
    )


  fun getCapsuleDetails(capsuleId: String) {
    CAPSULE_ID = capsuleId
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

  fun getScreenCheckPoint(capsuleId: String) {
    viewModelScope.launch {
      withContext(Dispatchers.IO) {
        delay(2000)
        val route: String? =
          openCapsuleScreenCheckPointUseCase.getCapsuleOpeningLastScreenRoute(capsuleId)
        if (route == null) {
          openCapsuleScreenCheckPointUseCase.saveCapsuleOpeningLastScreenRoute(
            route = Screen.OpenCapsuleInstructionsScreen.route,
            capsuleId = capsuleId
          )
          _screenCheckPoint.value = Screen.OpenCapsuleInstructionsScreen.route
        }
        route?.let {
          _screenCheckPoint.value = it
        }
      }
    }
  }

  fun saveScreenCheckPoint(route: String) {
    openCapsuleScreenCheckPointUseCase.saveCapsuleOpeningLastScreenRoute(
      route,
      capsuleId = CAPSULE_ID!!
    )
  }
}
