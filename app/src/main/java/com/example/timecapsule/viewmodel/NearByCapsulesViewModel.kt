package com.example.timecapsule.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import com.example.domain.usecase.FetchNearByCapsulesUseCase
import com.example.model.NearByCapsule
import com.google.type.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class NearByCapsulesViewModel @Inject constructor(
  private val fetchNearByCapsulesUseCase: FetchNearByCapsulesUseCase
) : ViewModel() {

  fun fetchNearByCapsules(
    location: LatLng,
    radius: Double,
    onComplete: (List<NearByCapsule>) -> Unit

  ) {
    fetchNearByCapsulesUseCase(location, radius, onComplete)
  }
}