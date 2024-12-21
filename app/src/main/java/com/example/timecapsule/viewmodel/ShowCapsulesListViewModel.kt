package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.DoneSegment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.GetCapsuleListUseCase
import com.example.domain.usecase.GetReceiveNotificationUseCase
import com.example.domain.usecase.GetRemoteAppUpdateDetailsUseCase
import com.example.domain.usecase.GetShareCapsulesUseCase
import com.example.domain.usecase.InsertUpdateDetailsUseCase
import com.example.domain.usecase.Load3dModelUseCase
import com.example.domain.usecase.SetReceiveNotificationCacheUseCase
import com.example.domain.usecase.SetShareCapsulesCacheUseCase
import com.example.model.CapsuleDetails
import com.example.util.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import kotlin.Exception
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
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
  private val insertUpdateDetailsUseCase: InsertUpdateDetailsUseCase,
  private val getReceiveNotificationUseCase: GetReceiveNotificationUseCase,
  private val getShareCapsulesUseCase: GetShareCapsulesUseCase,
  private val setShareCapsulesCacheUseCase: SetShareCapsulesCacheUseCase,
  private val setReceiveNotificationCacheUseCase: SetReceiveNotificationCacheUseCase,
  private val load3dModelUseCase: Load3dModelUseCase
) : ViewModel() {
  private val _capsuleListState =
    MutableStateFlow<CapsuleListScreenState>(CapsuleListScreenState.Loading)
  val capsuleListState: StateFlow<CapsuleListScreenState> = _capsuleListState

  init {
    load3DModel()
    getCapsulesList()
    syncLocalSettingsChecksOptionsWithRemote()
    syncLocalDBUpdateDetailsWithRemote()
  }

  fun load3DModel() {
    viewModelScope.launch(Dispatchers.IO) {
      val response = load3dModelUseCase("200")
      Log.e("pokemon", response.toString())

    }
  }

  fun getCapsulesList() {
    _capsuleListState.value = CapsuleListScreenState.Loading
    viewModelScope.launch(Dispatchers.IO) {
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

  fun syncLocalSettingsChecksOptionsWithRemote() {
    viewModelScope.launch(Dispatchers.IO) {
      try {
        val job1 = async {
          if (isActive) {
            val res = getReceiveNotificationUseCase()
            if (res is Response.Success) {
              setReceiveNotificationCacheUseCase(res.data ?: true)
            }
          }
        }

        val job2 = async {
          if (isActive) {
            val res = getShareCapsulesUseCase()
            if (res is Response.Success) {
              setShareCapsulesCacheUseCase(res.data ?: true)
            }
          }
        }

        job1.await()
        job2.await()
      } catch (_: Exception) {
      }
    }
  }

}
