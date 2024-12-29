package com.example.model

data class UserDetails(
  val userId: String,
  val userName: String,
  val firstName: String? = null,
  val lastName: String? = null,
  val email: String,
  val aboutMe:String,
  val imageUrl: String,
  val coverImageUrl:String,
  val capsuleList: List<Map<String, Any>> = emptyList(),
  val userNameLowerCase: String,
  val firstNameLowerCase: String,
  val isReceiveNotification: Boolean = true,
  val shareCapsules: Boolean = true,
  val notifications:List<*> = listOf<Any>()
)
