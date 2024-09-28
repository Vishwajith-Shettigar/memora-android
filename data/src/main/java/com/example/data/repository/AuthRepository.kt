package com.example.data.repository

import com.example.data.remote.AuthRemoteDataSource
import com.example.util.Response
import javax.inject.Inject

interface AuthRepository {
  suspend fun signIn(email: String, password: String): Response<String>
  suspend fun signUp(email: String, password: String): Response<Exception>
  suspend fun deleteUser()
}

// AuthRepositoryImpl.kt
class AuthRepositoryImpl @Inject constructor(
  private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
  override suspend fun signIn(email: String, password: String): Response<String> {
    return authRemoteDataSource.signIn(email, password)
  }

  override suspend fun signUp(email: String, password: String): Response<Exception> {
    return authRemoteDataSource.signUp(email, password)
  }

  override suspend fun deleteUser() {
    authRemoteDataSource.deleteUser()
  }
}
