package com.example.data.repository

import com.example.data.remote.UserRemoteDataSource
import com.example.model.UserDetails
import com.example.util.Response
import com.google.firebase.firestore.auth.User
import javax.inject.Inject

interface UserRepository {
  suspend fun saveUserDetails(userDetails: UserDetails): Response<Unit>
  suspend fun getUserDetails(userId: String): Response<UserDetails>
}

class UserRepositoryImpl @Inject constructor(
  val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
  override suspend fun saveUserDetails(userDetails: UserDetails): Response<Unit> {
    return userRemoteDataSource.saveUserDetails(userDetails)
  }

  override suspend fun getUserDetails(userId: String): Response<UserDetails> {
    return Response.Success()
  }
}
