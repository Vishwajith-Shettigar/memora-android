package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.SearchUsersUseCase
import com.example.model.UserDetails
import com.example.timecapsule.routes.Screen
import com.example.util.Response
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class SearchPeopleState {
  object Idle : SearchPeopleState()
  data class Success(val data: List<UserDetails>) : SearchPeopleState()
  data class Error(val message: String, val exception: Exception? = null) : SearchPeopleState()
}

enum class ShareWithPeopleOption {
  DONT_SHARE,
  SHARE_ALL,
  SELECTED_PEOPLES
}

enum class LocationOption {
  SELECT_LOCATION,
  DONT_SELECT_LOCATION
}

@HiltViewModel
class CapsuleCreationViewModel @Inject constructor(
  private val searchUsersUseCase: SearchUsersUseCase
) : ViewModel() {

  private val _searchPeopleState = MutableStateFlow<SearchPeopleState>(SearchPeopleState.Idle)
  val searchPeopleState: StateFlow<SearchPeopleState> = _searchPeopleState

  val selectedPeoples = mutableStateListOf<UserDetails>()

  val isSataliteView =
    mutableStateOf(false)


  var latLang =
    LatLng(
      1.3521,
      103.8198
    )


  // Store timestamp
  var selectedTimeStamp: Timestamp? = null

  // Store share with people option
  var shareWithPeopleOption: ShareWithPeopleOption = ShareWithPeopleOption.DONT_SHARE

  // Store location option
  var selectedLocationOption: LocationOption = LocationOption.DONT_SELECT_LOCATION

  fun setTimeStamp(p0: Timestamp) {
    selectedTimeStamp = p0
  }

  fun setShareWithPeople(p0: ShareWithPeopleOption) {
    shareWithPeopleOption = p0
  }

  fun setLocationOption(p0: LocationOption) {
    selectedLocationOption = p0
  }

  fun searchUsers(query: String) {
    viewModelScope.launch {
      val result = searchUsersUseCase(query)
      _searchPeopleState.value = when (result) {
        is Response.Success -> {
          SearchPeopleState.Success(result.data!!)
        }

        is Response.Error -> {
          SearchPeopleState.Error(
            exception = result.exception,
            message = result.exception.message!!
          )
        }
      }
    }
  }

}
