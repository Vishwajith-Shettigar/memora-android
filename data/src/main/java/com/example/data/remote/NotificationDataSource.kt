package com.example.data.remote

import com.example.model.NotificationDetails
import com.example.util.InValidUserException
import com.example.util.Response
import com.example.util.UsernameAlreadyExistsException
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class NotificationDataSource @Inject constructor(
  private val firestore: FirebaseFirestore,
  private val authRemoteDataSource: AuthRemoteDataSource
) {

  suspend fun getNotifications(): Response<List<NotificationDetails>> {
    return try {
      val user = authRemoteDataSource.getAuth()
      if (user != null) {
        val docSnapshot = firestore.collection("users").document(user.uid).get().await()
        val notificationListMap: List<Map<String, Any>> =
          docSnapshot.data?.get("notifications") as List<Map<String, Any>>

        val notificationList: List<NotificationDetails> = notificationListMap.map {
          NotificationDetails(
            body = it["body"].toString(),
            capsuleId = it["capsuleId"]?.toString(),
            timestamp = it["timestamp"] as Timestamp,
            imageUrl = it["imageurl"].toString(),
            username = it["username"].toString()
          )
        }

        Response.Success(notificationList)
      } else {
        throw InValidUserException()
      }
    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }
}
