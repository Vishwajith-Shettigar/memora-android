package com.example.model

import android.location.Location
import com.google.firebase.firestore.GeoPoint

data class NearByCapsule(
  val capsuleId: String,
  val modelId: String,
  val capsuleTitle: String,
  val description: String,
  val capsuleImageUrl: String,
  val location: GeoPoint
)