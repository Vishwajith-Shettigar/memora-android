package com.example.timecapsule.ui.util

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient


fun searchPlace(placesClient: PlacesClient, query: String, onPlaceFound: (LatLng?) -> Unit) {
  val request = FindAutocompletePredictionsRequest.builder()
    .setQuery(query)
    .build()

  placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
    if (response.autocompletePredictions.isNotEmpty()) {
      val placeId = response.autocompletePredictions[0].placeId
      placesClient.fetchPlace(
        com.google.android.libraries.places.api.net.FetchPlaceRequest.builder(
          placeId,
          listOf(Place.Field.LAT_LNG)
        ).build()
      ).addOnSuccessListener { fetchPlaceResponse ->
        val latLng = fetchPlaceResponse.place.latLng
        onPlaceFound(latLng)
      }
    } else {
      onPlaceFound(null)
    }
  }.addOnFailureListener {
    onPlaceFound(null)
  }
}