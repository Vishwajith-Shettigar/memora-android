package com.example.data.remote

import android.util.Log
import com.example.model.UserDetails
import com.example.util.AskDetailsException
import com.example.util.Response
import com.example.util.UnspecifiedException
import com.example.util.UsernameAlreadyExistsException
import com.example.util.defaultPictures
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject
import kotlin.math.ln
import kotlin.random.Random
import kotlinx.coroutines.tasks.await

class UserRemoteDataSource @Inject constructor(
  val firestore: FirebaseFirestore,
  val authRemoteDataSource: AuthRemoteDataSource
) {

  suspend fun getUserDetails(userId: String): Response<UserDetails> {
    return try {
      val userDoc = firestore.collection("users").document(userId).get().await()
      Response.Success(parseUser(userDoc))
    } catch (e: Exception) {
      Response.Error(exception =  e)
    }
  }

  suspend fun saveUserDetails(userName: String, fName: String, lName: String): Response<Unit> {
    val usersCollection = firestore.collection("users")

    return try {
      val usernameQuery = usersCollection
        .whereEqualTo("userName", userName)
        .get()
        .await()

      // If the username already exists, throw a custom exception
      if (!usernameQuery.isEmpty) {
        throw UsernameAlreadyExistsException()
      }

      val user = authRemoteDataSource.getAuth() ?: throw UnspecifiedException()

      val size = defaultPictures.size
      val defaultImageUrl = defaultPictures.get(Random.nextInt(0, size))
      val newUserDetails = UserDetails(
        userId = user.uid,
        email = user.email.toString(),
        userName = userName,
        firstName = fName,
        lastName = lName,
        imageUrl = defaultImageUrl,
        userNameLowerCase = userName.toLowerCase(),
        firstNameLowerCase = fName.toLowerCase()
      )

      firestore.collection("users").document(newUserDetails.userId)
        .set(newUserDetails).await()
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
      if (documentSnapshot.exists())
        Response.Success()
      else
        Response.Error(AskDetailsException())
    } catch (e: Exception) {
      Response.Error(AskDetailsException())
    }
  }

  suspend fun searchUsers(query: String): Response<List<UserDetails>> {
    return try {
      val lowerCaseQuery = query.lowercase()

      // Query for userNameLower and firstNameLower
      val userNameResult = firestore.collection("users")
        .whereGreaterThanOrEqualTo("userNameLowerCase", lowerCaseQuery)
        .whereLessThanOrEqualTo("userNameLowerCase", lowerCaseQuery + '\uf8ff')
        .get()
        .await()

      val firstNameResult = firestore.collection("users")
        .whereGreaterThanOrEqualTo("firstNameLowerCase", lowerCaseQuery)
        .whereLessThanOrEqualTo("firstNameLowerCase", lowerCaseQuery + '\uf8ff')
        .get()
        .await()

      // Combine both results
      val allResults = userNameResult.documents + firstNameResult.documents

      // Remove duplicates by document ID
      val uniqueResults = allResults.distinctBy { it.id }
      parseUsers((uniqueResults))
    } catch (e: Exception) {
      Response.Error(e)
    }
  }

  private fun parseUsers(snapshot: List<DocumentSnapshot>): Response<List<UserDetails>> {
    val users = mutableListOf<UserDetails>()
    for (document in snapshot) {
      val user = parseUser(document)
      users.add(user)
    }
    return Response.Success(users)
  }


  private fun parseUser(document: DocumentSnapshot): UserDetails {
    val userName = document.get("userName") as String
    val userId = document.get("userId") as String
    val fname = document.get("firstName") as String
    val lname = document.get("lastName") as String
    val imageUrl = document.get("imageUrl") as String

    val user = UserDetails(
      userId = userId,
      userName = userName,
      firstName = fname,
      lastName = lname,
      imageUrl = imageUrl,
      email = "",
      capsuleList = emptyList(),
      userNameLowerCase = "",
      firstNameLowerCase = ""
    )
    return user

  }
}