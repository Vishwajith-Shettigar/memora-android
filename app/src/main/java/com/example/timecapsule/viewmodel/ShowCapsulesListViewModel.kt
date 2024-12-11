package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCapsuleListUseCase
import com.example.domain.usecase.GetRemoteAppUpdateDetailsUseCase
import com.example.domain.usecase.InsertUpdateDetailsUseCase
import com.example.model.CapsuleDetails
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.Exception
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


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
  private val getCapsuleListUseCase: GetCapsuleListUseCase,
  private val getRemoteAppUpdateDetailsUseCase: GetRemoteAppUpdateDetailsUseCase,
  private val insertUpdateDetailsUseCase: InsertUpdateDetailsUseCase
) : ViewModel() {
  private val _capsuleListState =
    MutableStateFlow<CapsuleListScreenState>(CapsuleListScreenState.Loading)
  val capsuleListState: StateFlow<CapsuleListScreenState> = _capsuleListState

  init {
    viewModelScope.launch(Dispatchers.IO) {
      getCapsulesList()
      syncLocalDBUpdateDetailsWithRemote()
    }
  }

  suspend fun getCapsulesList() {
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

  fun syncLocalDBUpdateDetailsWithRemote() {
    try {
      viewModelScope.launch(Dispatchers.IO) {
        val res = getRemoteAppUpdateDetailsUseCase()
        if (res is Response.Success) {
          insertUpdateDetailsUseCase(res.data!!)
        }
      }
    } catch (_: Exception) {
    }
  }
}
