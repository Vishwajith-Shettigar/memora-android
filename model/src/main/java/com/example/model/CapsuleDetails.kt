package com.example.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.type.LatLng

data class CapsuleDetails(
  val id: String,
  val title: String,
  val description: String,
  val time: Timestamp,
  val isDeleted: Boolean,
  val modelId: Number,
  var users: List<Map<String, Any>>,
  val isOwner: Boolean,
  val imageUrl: String,
  var ownerUserName: String,
  val location: GeoPoint? = null,
  var geoHash: String? = null,
  val fileUrls: List<Map<String, String>>,
  val isOpened: Boolean? = null,
  val letter: String? = null,
  val isSharedWithAll: Boolean = false,
  val isSurpriseCapsule:Boolean = false
)

data class CapsuleAsset(
  val capsule_id: String,
  val capsuleName: String,
  val imageUrl: String,
  val isPaid: Boolean,
  val storage: Number,
  val description: String,
  val cost: Number
)