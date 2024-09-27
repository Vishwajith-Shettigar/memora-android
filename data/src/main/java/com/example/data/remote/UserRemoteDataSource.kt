package com.example.data.remote

import android.util.Log
import com.example.model.UserDetails
import com.example.util.Response
import com.example.util.UsernameAlreadyExistsException
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class UserRemoteDataSource @Inject constructor(
  val firestore: FirebaseFirestore
) {
  suspend fun saveUserDetails(userDetails: UserDetails): Response<Unit> {
    val usersCollection = firestore.collection("users")

    return try {

      val usernameQuery = usersCollection
        .whereEqualTo("userName", userDetails.userName)
        .get()
        .await()

      // If the username already exists, throw a custom exception
      if (!usernameQuery.isEmpty) {
        throw UsernameAlreadyExistsException()
      }

      firestore.collection("users").document(userDetails.userId)
        .set(userDetails).await()

      Response.Success(Unit)
    } catch (e: Exception) {
      Response.Error(e)
    }
  }
}