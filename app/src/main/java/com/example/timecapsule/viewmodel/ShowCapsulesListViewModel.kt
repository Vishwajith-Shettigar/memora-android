package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCapsuleListUseCase
import com.example.model.CapsuleDetails
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class CapsuleListScreenState {
  object Idle : CapsuleListScreenState()
  object Loading : CapsuleListScreenState()
  class Success(
    val capsuleList: List<CapsuleDetails>
  ) : CapsuleListScreenState()

  data class Error(val message: String, val exception: Exception? = null) :
    CapsuleListScreenState()
}


@HiltViewModel
class ShowCapsulesListViewModel @Inject constructor(
  private val getCapsuleListUseCase: GetCapsuleListUseCase
) : ViewModel() {
  private val _capsuleListState =
    MutableStateFlow<CapsuleListScreenState>(CapsuleListScreenState.Loading)
  val capsuleListState: StateFlow<CapsuleListScreenState> = _capsuleListState

  fun getCapsulesList() {
    viewModelScope.launch {
      val result = getCapsuleListUseCase()
      _capsuleListState.value = when (result) {
        is Response.Success -> {
          CapsuleListScreenState.Success(result.data!!)
        }

        is Response.Error -> {
          CapsuleListScreenState.Error(
            result.exception.message.toString(),
            result.exception
          )
        }
      }
    }
  }
}
