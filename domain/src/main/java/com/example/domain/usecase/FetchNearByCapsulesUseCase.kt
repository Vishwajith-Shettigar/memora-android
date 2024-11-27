package com.example.domain.usecase

import android.location.Location
import com.example.data.repository.NearByCapsulesRepository
import com.example.model.NearByCapsule
import javax.inject.Inject
import com.google.type.LatLng

class FetchNearByCapsulesUseCase @Inject constructor(
  private val nearByCapsules: NearByCapsulesRepository
) {
  operator fun invoke(
    location: LatLng,
    radius: Double,
    onComplete: (List<NearByCapsule>) -> Unit
  ) {
    nearByCapsules.fetchNearByCapsules(location, radius, onComplete)
  }
}
