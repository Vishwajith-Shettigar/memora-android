package com.example.model

data class UserDetails(
  val userId: String,
  val userName: String,
  val firstName: String? = null,
  val lastName: String? = null,
  val email: String,
  val imageUrl: String,
  val capsuleList: List<Map<String,Any>> = emptyList(),
  val userNameLowerCase:String,
  val firstNameLowerCase:String
)