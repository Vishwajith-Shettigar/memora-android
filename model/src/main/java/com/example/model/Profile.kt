package com.example.model

import android.net.Uri

data class Profile(
  val userId: String,
  val username: String,
  val firstName: String,
  val lastName: String,
  val profileImageUrl: String,
  val coverImageUrl: String,
  val aboutMe: String
)

data class UpdateProfile(
  val firstName: String,
  val lastName: String,
  val oldProfileImageUrl: String,
  val oldCoverImageUrl: String,
  val profileImageUri: Uri?,
  val coverImageUri: Uri?,
  val aboutMe: String
)
