package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class CapsuleCreationState {

}

enum class ShareWithPeopleOption {
  DONT_SHARE,
  SHARE_ALL,
  SELECTED_PEOPLES
}


@HiltViewModel
class CapsuleCreationViewModel @Inject constructor(
) : ViewModel() {

  override fun onCleared() {
    super.onCleared()
    Log.e("#", "im cleared")
  }

  init {
      Log.e("#","init")
  }

  // store timestamp
   var selectedTimeStamp: Timestamp? = null
  private var shareWithPeopleOption: ShareWithPeopleOption = ShareWithPeopleOption.DONT_SHARE

  fun setTimeStamp(p0: Timestamp) {
    selectedTimeStamp = p0
  }

  fun setShareWithPeople(p0: ShareWithPeopleOption) {
    shareWithPeopleOption = p0
  }

}