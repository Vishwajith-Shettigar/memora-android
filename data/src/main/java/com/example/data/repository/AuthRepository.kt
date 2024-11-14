package com.example.data.repository

import com.example.data.remote.AuthRemoteDataSource
import com.example.util.Response
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

interface AuthRepository {
  suspend fun signIn(email: String, password: String): Response<String>
  suspend fun signUp(email: String, password: String): Response<Exception>
  suspend fun deleteUser()
  suspend fun signOut()
  suspend fun getAuth():FirebaseUser?
}

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

  override suspend fun signOut() {
    authRemoteDataSource.signOut()
  }

  override suspend fun getAuth(): FirebaseUser? {
   return authRemoteDataSource.getAuth()
  }
}
