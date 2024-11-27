package com.example.data.repository

import android.location.Location
import com.example.data.remote.NearByCapsulesDataSource
import com.example.model.NearByCapsule
import com.google.type.LatLng
import javax.inject.Inject

interface NearByCapsulesRepository {
  fun fetchNearByCapsules(
    location: LatLng, radius: Double,
    onComplete: (List<NearByCapsule>) -> Unit
  )
}

class NearByCapsulesRepositoryImpl @Inject constructor(
  private val nearByCapsulesDataSource: NearByCapsulesDataSource
) : NearByCapsulesRepository {
  override fun fetchNearByCapsules(
    location: LatLng,
    radius: Double,
    onComplete: (List<NearByCapsule>) -> Unit
  ) {
    nearByCapsulesDataSource.fetchNearbyCapsules(location = location, radius = radius, onComplete)
  }
}
