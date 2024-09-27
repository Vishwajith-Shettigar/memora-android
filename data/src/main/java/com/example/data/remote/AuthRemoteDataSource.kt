package com.example.data.remote

import com.example.util.Response
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

// AuthRemoteDataSource.kt
class AuthRemoteDataSource @Inject constructor(
  private val firebaseAuth: FirebaseAuth
) {
  suspend fun signIn(email: String, password: String): Response<Unit> {
    return try {
      firebaseAuth.signInWithEmailAndPassword(email, password).await()
      Response.Success(Unit)
    } catch (e: Exception) {
      Response.Error(e)
    }
  }

  suspend fun signUp(email: String, password: String): Response<String> {
    return try {
      val userId = firebaseAuth.createUserWithEmailAndPassword(email, password).await().user?.uid
      Response.Success(userId)
    } catch (e: Exception) {
      Response.Error(e)
    }
  }

   suspend fun deleteUser() {
    try {
      firebaseAuth.currentUser?.delete()?.await()
    } catch (_: Exception) {
    }
  }
}

