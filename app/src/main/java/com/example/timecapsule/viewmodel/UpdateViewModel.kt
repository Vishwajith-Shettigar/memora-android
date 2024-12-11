package com.example.timecapsule.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entity.UpdateDetails
import com.example.domain.usecase.GetUpdateDetailsUseCase
import com.example.domain.usecase.InsertUpdateDetailsUseCase
import com.mapbox.maps.extension.style.expressions.dsl.generated.get
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltViewModel
class UpdateViewModel @Inject constructor(
  private val getUpdateDetailsUseCase: GetUpdateDetailsUseCase,
) : ViewModel() {

  var updateDetails: UpdateDetails? = null

  init {
    viewModelScope.launch(Dispatchers.IO) {
      updateDetails = getUpdateDetailsUseCase()
    }
  }
}
