package com.example.timecapsule.viewmodel

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCapsuleListUseCase
import com.example.model.CapsuleDetails
import com.example.util.Response
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class CapsuleListScreenAuthState {
  object Idle : CapsuleListScreenAuthState()
  object Loading : CapsuleListScreenAuthState()
  class Success(
    val capsuleList: List<CapsuleDetails>
  ) : CapsuleListScreenAuthState()

  data class Error(val message: String, val exception: Exception? = null) :
    CapsuleListScreenAuthState()
}


@HiltViewModel
class ShowCapsulesListViewModel @Inject constructor(
  private val getCapsuleListUseCase: GetCapsuleListUseCase
) : ViewModel() {
  private val _capsuleListState =
    MutableStateFlow<CapsuleListScreenAuthState>(CapsuleListScreenAuthState.Idle)
  val capsuleListState: StateFlow<CapsuleListScreenAuthState> = _capsuleListState

  fun getCapsulesList() {
    viewModelScope.launch {
      val result = getCapsuleListUseCase()
      _capsuleListState.value = when (result) {
        is Response.Success -> {
          CapsuleListScreenAuthState.Success(result.data!!)
        }

        is Response.Error -> CapsuleListScreenAuthState.Error(
          result.exception.message.toString(),
          result.exception
        )
      }
    }
  }
}
