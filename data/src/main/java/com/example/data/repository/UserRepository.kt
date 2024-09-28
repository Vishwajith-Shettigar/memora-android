package com.example.data.repository

import com.example.data.remote.UserRemoteDataSource
import com.example.model.UserDetails
import com.example.util.Response
import com.google.firebase.firestore.auth.User
import javax.inject.Inject

interface UserRepository {
  suspend fun saveUserDetails(userName: String, fName: String, lName: String): Response<Unit>
  suspend fun getUserDetails(userId: String): Response<UserDetails>
  suspend fun checkUserNameDoesntExists(userName: String): Response<Exception>
  suspend fun checkUserRecordExists(userId: String):Response<Any>
}

class UserRepositoryImpl @Inject constructor(
  val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
  override suspend fun saveUserDetails(userName: String, fName: String, lName: String): Response<Unit> {
    return userRemoteDataSource.saveUserDetails(userName,fName,lName)
  }

  override suspend fun getUserDetails(userId: String): Response<UserDetails> {
    return Response.Success()
  }

  override suspend fun checkUserNameDoesntExists(userName: String): Response<Exception> {
    return userRemoteDataSource.checkUserNameDoesntExist(userName)
  }

  override suspend fun checkUserRecordExists(userId: String): Response<Any> {
   return userRemoteDataSource.checkUserRecordExists(userId)
  }
}
