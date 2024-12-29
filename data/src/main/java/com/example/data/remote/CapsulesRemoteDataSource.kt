package com.example.data.remote

import android.util.Log
import com.example.model.CapsuleAsset
import com.example.model.CapsuleDetails
import com.example.model.UserDetails
import com.example.util.InValidUserException
import com.example.util.NetWorkException
import com.example.util.NoAuthException
import com.example.util.Response
import com.example.util.UnspecifiedException
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.snapshots
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.Async

class CapsulesRemoteDataSource @Inject constructor(
  val firestore: FirebaseFirestore,
  val authRemoteDataSource: AuthRemoteDataSource,
  val userRemoteDataSource: UserRemoteDataSource,
  val threeDModelsDataSource: ThreeDModelsDataSource
) {

  suspend fun createCapsule(capsuleDetails: CapsuleDetails): Response<Unit> {
    return try {

      val userId = authRemoteDataSource.getAuth()?.uid

      val responseUserName: Response<UserDetails> =
        userRemoteDataSource.getUserDetails(userId!!)

      if (responseUserName is Response.Error) {
        throw responseUserName.exception
      }

      val userName: String = (responseUserName as Response.Success).data!!.userName

      val responseSaveCapsule = saveCapsule(capsuleDetails, userName)

      if (responseSaveCapsule is Response.Error)
        throw responseSaveCapsule.exception

      val addCapsuleToUserResponse =
        addCapsuleToUsersDocument(capsuleDetails.users, userId, capsuleDetails)

      if (addCapsuleToUserResponse is Response.Error)
        throw addCapsuleToUserResponse.exception

      Response.Success()
    } catch (e: Exception) {
      Response.Error(e)
    }

  }

  suspend fun addCapsuleToUsersDocument(
    users: List<Map<String, Any>>,
    userUID: String,
    capsuleDetails: CapsuleDetails
  ): Response<Unit> {

    return try {
      users.forEach { user ->
        val userId = user.get("userId")
        val isOwner = user.get("isOwner")
        val map = mapOf(
          "id" to capsuleDetails.id,
          "isOwner" to isOwner,
          "isOpened" to false
        )
        firestore.collection("users").document(userId.toString())
          .update("capsuleList", FieldValue.arrayUnion(map)).await()
      }
      Response.Success()
    } catch (e: Exception) {
      Response.Error(e)
    }
  }

  suspend fun saveCapsule(capsuleDetails: CapsuleDetails, userName: String): Response<Unit> {
    return try {
      var geohash: String? = null
      capsuleDetails.location?.let {
        geohash = GeoFireUtils.getGeoHashForLocation(GeoLocation(it.latitude, it.longitude))
      }
      capsuleDetails.geoHash = geohash
      val ref = firestore.collection("capsules").document(capsuleDetails.id)
      capsuleDetails.ownerUserName = userName

      val snapShot = ref.get().await()
      var users: List<Map<String, Any>> = listOf()
      if (snapShot.exists()) {
        users = snapShot.data?.get("users") as List<Map<String, Any>>
      }

      val combinedUsers = (capsuleDetails.users + users)
        .distinctBy { it["userId"] }

      capsuleDetails.users = combinedUsers
      ref.set(capsuleDetails).await()
      Response.Success()
    } catch (e: Exception) {
      Response.Error(e)
    }
  }


  @OptIn(ExperimentalCoroutinesApi::class)
  suspend fun getCapsulesList(): Response<List<CapsuleDetails>> {
    val userId =
      authRemoteDataSource.getAuth()?.uid ?: return Response.Error(InValidUserException())

    return try {
      val capsulesDetailsList = mutableListOf<CapsuleDetails>()

      val doc = firestore.collection("users").document(userId).get().await()
      val capsules = doc.data?.get("capsuleList") as? List<Map<*, *>> ?: emptyList()

      withContext(Dispatchers.IO.limitedParallelism(20)) {
        val capsuleDeferreds = capsules.map { capsule ->
          async {
            try {
              val capsuleDoc = firestore.collection("capsules")
                .document(capsule["id"].toString())
                .get()
                .await()
                .data

              if (!capsuleDoc.isNullOrEmpty()) {
                val location = capsuleDoc["location"] as? GeoPoint

                CapsuleDetails(
                  id = capsule["id"] as String,
                  title = capsuleDoc["title"] as? String ?: "",
                  description = capsuleDoc["description"] as? String ?: "",
                  isDeleted = capsuleDoc["deleted"] as? Boolean ?: false,
                  modelId = capsuleDoc["modelId"] as? Number ?: 0,
                  time = capsuleDoc["time"] as? Timestamp ?: Timestamp.now(),
                  users = capsuleDoc["users"] as? List<Map<String, Any>> ?: emptyList(),
                  isOwner = capsule["isOwner"] as? Boolean ?: false,
                  imageUrl = capsuleDoc["imageUrl"] as? String ?: "",
                  ownerUserName = capsuleDoc["ownerUserName"] as? String ?: "",
                  location = location,
                  fileUrls = capsuleDoc["fileUrls"] as? List<Map<String, String>> ?: emptyList(),
                  isOpened = capsule["isOpened"] as? Boolean ?: false,
                  isSharedWithAll = capsuleDoc["sharedWithAll"] as? Boolean ?: false
                )
              } else {
                null // Skip empty or invalid capsules
              }
            } catch (e: Exception) {
              null // Log or handle specific errors if needed
            }
          }
        }

        // Await all tasks and add non-null results to the list
        capsulesDetailsList.addAll(capsuleDeferreds.awaitAll().filterNotNull())

        // Process surprise capsules
        val surpriseCapsuleResponse = async {
          getSurpriseCapsule()
        }

        // Add surprise capsules if successful
        val surpriseCapsuleResult = surpriseCapsuleResponse.await()
        if (surpriseCapsuleResult is Response.Success) {
          surpriseCapsuleResult.data?.let { capsulesDetailsList.addAll(it) }
        }
      }

      Response.Success(capsulesDetailsList)
    } catch (e: FirebaseFirestoreException) {
      Response.Error(NetWorkException())
    } catch (e: Exception) {
      Response.Error(UnspecifiedException())
    }
  }


  suspend fun getCapsuleDetails(capsuleId: String): Response<CapsuleDetails> {
    return try {
      val userId = authRemoteDataSource.getAuth()?.uid
      if (userId == null) {
        throw InValidUserException()
      }

      val doc = firestore.collection("users").document(userId).get().await()
      val capsules: MutableList<Map<*, *>> =
        doc.data?.get("capsuleList") as? MutableList<Map<*, *>> ?: mutableListOf()
      var isOwner = false
      var isOpened = false

      capsules.forEach {
        if (it["id"] == capsuleId) {
          isOwner = it["isOwner"] as Boolean
          isOpened = it["isOpened"] as Boolean
        }
      }

      // clear the list, since no use.
      capsules.clear()

      val capsuleDoc =
        firestore.collection("capsules").document(capsuleId).get().await().data

      if (capsuleDoc.isNullOrEmpty())
        throw UnspecifiedException()

      var location: GeoPoint? = null
      capsuleDoc.get("location")?.let {
        location = it as GeoPoint
      }

      var letter: String? = null
      capsuleDoc.get("letter")?.let {
        letter = it as String
      }

      val capsuleDetails = CapsuleDetails(
        id = capsuleId,
        title = capsuleDoc.get("title") as String,
        description = capsuleDoc.get("description") as String,
        isDeleted = capsuleDoc.get("deleted") as Boolean,
        modelId = capsuleDoc.get("modelId") as Number,
        time = capsuleDoc.get("time") as Timestamp,
        users = capsuleDoc.get("users") as List<Map<String, Any>>,
        isOwner = isOwner,
        imageUrl = capsuleDoc.get("imageUrl") as String,
        ownerUserName = capsuleDoc.get("ownerUserName") as String,
        location = location,
        fileUrls = capsuleDoc.get("fileUrls") as List<Map<String, String>>,
        isOpened = isOpened,
        letter = letter,
        isSharedWithAll = capsuleDoc.get("sharedWithAll") as Boolean
      )
      Response.Success(capsuleDetails)
    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }

  suspend fun getCapsuleAssets(): Response<List<CapsuleAsset>> {
    return try {
      val capsuleAssets = mutableListOf<CapsuleAsset>()
      val docs = firestore.collection("capsule_assets").get().await().documents
      docs.forEach { document ->
        val capsuleAsset = CapsuleAsset(
          capsule_id = document.get("capsule_id").toString(),
          capsuleName = document.get("capsuleName").toString(),
          description = document.get("description").toString(),
          imageUrl = document.get("imageUrl").toString(),
          isPaid = document.get("isPaid") as Boolean,
          storage = document.get("storage") as Number,
          cost = document.get("cost") as Number
        )
        capsuleAssets.add(capsuleAsset)
      }
      return Response.Success(capsuleAssets)
    } catch (e: Exception) {
      Response.Error(UnspecifiedException())
    }
  }

  suspend fun setCapsuleOpened(capsuleId: String): Response<Unit> {
    return try {
      val userId = authRemoteDataSource.getAuth()?.uid ?: throw NoAuthException()
      val docref = firestore.collection("users").document(userId)

      val doc = docref.get().await()
      val capsuleList = doc.get("capsuleList") as? List<Map<String, Any>> ?: emptyList()

      val capsule = capsuleList.find {
        it["id"] == capsuleId
      }

      if (capsule != null && !(capsule.get("isOpened") as Boolean)) {
        val updatedCapsule = capsule.toMutableMap()
        updatedCapsule["isOpened"] = true
        docref.update("capsuleList", FieldValue.arrayRemove(capsule)).await()
        docref.update("capsuleList", FieldValue.arrayUnion(updatedCapsule)).await()
      }

      Response.Success()
    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }

  suspend fun getSurpriseCapsuleDetails(capsuleId: String): Response<CapsuleDetails> {
    return try {
      Log.e("pokemon", capsuleId)
      val snapshot = firestore.collection("surprise_capsules").document(capsuleId).get().await()

      val document = snapshot.data!!
      val capsuleDetails = CapsuleDetails(
        id = snapshot.id,
        title = document.get("title") as String,
        description = document.get("description") as String,
        isDeleted = document.get("deleted") as Boolean,
        modelId = document.get("modelId") as Number,
        time = document.get("time") as Timestamp,
        users = emptyList(),
        isOwner = false,
        imageUrl = "",
        ownerUserName = "",
        location = null,
        fileUrls = document.get("fileUrls") as? List<Map<String, String>>
          ?: emptyList<Map<String, String>>(),
        isOpened = false,
        letter = document.get("letter") as String,
        isSharedWithAll = document.get("sharedWithAll") as Boolean,
        isSurpriseCapsule = true
      )
      Response.Success(data = capsuleDetails)

    } catch (e: Exception) {
      Log.e("pokemon", e.toString())
      Response.Error(exception = e)
    }
  }

  suspend fun getSurpriseCapsule(): Response<List<CapsuleDetails>> {
    return try {
      val surpriseCapsuleList = mutableListOf<CapsuleDetails>()
      val docs = firestore.collection("surprise_capsules").get().await().documents

      docs.forEach { document ->
        val capsuleDetails = CapsuleDetails(
          id = document.id,
          title = document.get("title") as String,
          description = document.get("description") as String,
          isDeleted = document.get("deleted") as Boolean,
          modelId = document.get("modelId") as Number,
          time = document.get("time") as Timestamp,
          users = document.get("users") as List<Map<String, Any>>,
          isOwner = false,
          imageUrl = document.get("imageUrl") as String,
          ownerUserName = "",
          location = null,
          fileUrls = document.get("fileUrls") as List<Map<String, String>>,
          isOpened = false,
          letter = document.get("letter") as String,
          isSharedWithAll = document.get("sharedWithAll") as Boolean,
          isSurpriseCapsule = true
        )

        surpriseCapsuleList.add(capsuleDetails)
      }

      Response.Success(surpriseCapsuleList.toList())
    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }
}
