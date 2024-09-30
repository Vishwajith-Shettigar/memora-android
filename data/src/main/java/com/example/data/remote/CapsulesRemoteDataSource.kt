package com.example.data.remote

import com.example.model.CapsuleDetails
import com.example.util.InValidUserException
import com.example.util.Response
import com.example.util.UnspecifiedException
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

class CapsulesRemoteDataSource @Inject constructor(
  val firestore: FirebaseFirestore,
  val authRemoteDataSource: AuthRemoteDataSource
) {
  suspend fun getCapsulesList(): Response<List<CapsuleDetails>> {
    val userId = authRemoteDataSource.getAuth()?.uid
    if (userId != null) {
      return try {
        val capsulesDetailsList = mutableListOf<CapsuleDetails>()
        val doc = firestore.collection("users").document(userId).get().await()
        val capsules = doc.data?.get("capsuleList") as? List<Map<*, *>> ?: emptyList()

        capsules.forEach { capsule ->
          val capsuleDoc =
            firestore.collection("capsules").document(capsule["id"].toString()).get().await().data
          val capsuleDetails = CapsuleDetails(
            id = capsule["id"] as String,
            title = capsuleDoc?.get("title") as String,
            description = capsuleDoc.get("description") as String,
            isDeleted = capsuleDoc.get("isDeleted") as Boolean,
            isOpened = capsuleDoc.get("isOpened") as Boolean,
            modelId = capsuleDoc.get("modelId") as Number,
            time = capsuleDoc.get("time") as Timestamp,
            users = capsuleDoc.get("users") as List<Map<String, Any>>,
            isOwner = capsule["isOwner"] as Boolean
          )
          capsulesDetailsList.add(capsuleDetails)
        }
        Response.Success(capsulesDetailsList)
      } catch (e: Exception) {
        Response.Error(UnspecifiedException())
      }
    }
    return Response.Error(exception = InValidUserException())
  }
}
