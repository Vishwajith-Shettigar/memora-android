package com.example.model

import com.google.firebase.Timestamp

data class CapsuleDetails(
  val id: String,
  val title: String,
  val description: String,
  val time: Timestamp,
  val isDeleted: Boolean,
  val isOpened: Boolean,
  val modelId: Number,
  val users: List<Map<String, Any>>,
  val isOwner:Boolean,
  val imageUrl:String,
  val ownerUserName:String,
)

data class CapsuleAsset(
  val capsule_id:String,
  val capsuleName:String,
  val imageUrl:String,
  val isPaid:Boolean,
  val storage: Number,
  val description:String,
  val cost: Number
)