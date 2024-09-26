package com.example.data.repository

import com.example.data.remote.AuthRemoteDataSource
import com.example.util.Response
import javax.inject.Inject

interface AuthRepository {
  suspend fun signIn(email: String, password: String): Response<Unit>
  suspend fun signUp(email: String, password: String): Response<Unit>
}

// AuthRepositoryImpl.kt
class AuthRepositoryImpl @Inject constructor(
  private val authRemoteDataSource: AuthRemoteDataSource
) : AuthRepository {
  override suspend fun signIn(email: String, password: String): Response<Unit> {
    return authRemoteDataSource.signIn(email, password)
  }

  override suspend fun signUp(email: String, password: String): Response<Unit> {
    return authRemoteDataSource.signUp(email, password)
  }
}
