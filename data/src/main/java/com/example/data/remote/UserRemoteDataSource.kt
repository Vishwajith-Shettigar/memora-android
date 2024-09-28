package com.example.data.remote

import android.util.Log
import com.example.model.UserDetails
import com.example.util.AskDetailsException
import com.example.util.Response
import com.example.util.UsernameAlreadyExistsException
import com.google.firebase.firestore.DocumentSnapshot
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

  suspend fun checkUserNameDoesntExist(userName: String): Response<Exception> {
    return try {
      val usersCollection = firestore.collection("users")
      val usernameQuery = usersCollection
        .whereEqualTo("userName", userName)
        .get()
        .await()

      // If the username already exists, throw a custom exception
      if (!usernameQuery.isEmpty) {
        throw UsernameAlreadyExistsException()
      }
      Response.Success()
    } catch (e: Exception) {
      Response.Error(e)
    }
  }

  suspend fun checkUserRecordExists(userId: String): Response<Any> {
    return try {
      val documentReference = firestore.collection("users").document(userId)
      val documentSnapshot: DocumentSnapshot = documentReference.get().await()
      documentSnapshot.exists()
      Response.Success()
    } catch (e: Exception) {
      Response.Error(AskDetailsException())
    }
  }
}
