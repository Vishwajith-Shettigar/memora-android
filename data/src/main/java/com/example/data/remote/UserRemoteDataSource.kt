package com.example.data.remote

import androidx.room.Update
import com.example.data.local.entity.Review
import com.example.data.local.entity.UpdateDetails
import com.example.model.Profile
import com.example.model.UpdateProfile
import com.example.model.UserDetails
import com.example.util.AskDetailsException
import com.example.util.NoAuthException
import com.example.util.Response
import com.example.util.UnspecifiedException
import com.example.util.UsernameAlreadyExistsException
import com.example.util.defaultCoverImages
import com.example.util.defaultPictures
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import kotlin.random.Random
import kotlinx.coroutines.tasks.await

class UserRemoteDataSource @Inject constructor(
  val firestore: FirebaseFirestore,
  val firebaseAuth: FirebaseAuth,
  private val firebaseMessaging: FirebaseMessaging,
  val authRemoteDataSource: AuthRemoteDataSource,
  private val storage: FirebaseStorage
) {

  suspend fun getUserDetails(userId: String): Response<UserDetails> {
    return try {
      val userDoc = firestore.collection("users").document(userId).get().await()
      Response.Success(parseUser(userDoc))
    } catch (e: Exception) {
      Response.Error(exception = e)
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
      val coverImagesSize = defaultPictures.size
      val defaultCoverImageUrl = defaultCoverImages.get(Random.nextInt(0, coverImagesSize))
      val newUserDetails = UserDetails(
        userId = user.uid,
        email = user.email.toString(),
        userName = userName,
        firstName = fName,
        lastName = lName,
        imageUrl = defaultImageUrl,
        coverImageUrl = defaultCoverImageUrl,
        userNameLowerCase = userName.toLowerCase(),
        firstNameLowerCase = fName.toLowerCase(),
        aboutMe = ""
      )

      firestore.collection("users").document(newUserDetails.userId)
        .set(newUserDetails).await()

      saveTokenToFirestore()

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

  suspend fun saveTokenToFirestore() {
    try {

      val user = authRemoteDataSource.getAuth()

      val token: String = firebaseMessaging.token.await()

      val tokenData = mapOf("fcmToken" to token)

      user?.uid?.let {
        firestore.collection("users").document(it)
          .update(tokenData).await()
      }

    } catch (_: Exception) {
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
    val coverImageUrl = document.get("coverImageUrl") as String
    val aboutMe = document.get("aboutMe") as String

    val user = UserDetails(
      userId = userId,
      userName = userName,
      firstName = fname,
      lastName = lname,
      imageUrl = imageUrl,
      coverImageUrl = coverImageUrl,
      email = "",
      capsuleList = emptyList(),
      userNameLowerCase = "",
      firstNameLowerCase = "",
      aboutMe = aboutMe
    )
    return user
  }

  fun getFileNameFromUrl(fileUrl: String): String {
    // Decode the URL and extract the file name
    val decodedUrl = java.net.URLDecoder.decode(fileUrl, "UTF-8")
    val filePath = decodedUrl.substringBefore("?").substringAfterLast("/")
    return filePath
  }

  suspend fun updateProfile(profile: UpdateProfile): Response<Unit> {
    try {
      val user = authRemoteDataSource.getAuth() ?: throw NoAuthException()

      var newProfileUrl: String? = null
      var newCoverUrl: String? = null

      profile.profileImageUri?.let {
        val storageRef = storage.reference.child("uploads/" + user.uid + "/" + "profileImage/")
        storageRef.putFile(it).await()
        newProfileUrl = storageRef.downloadUrl.await().toString()
      }

      profile.coverImageUri?.let {
        val storageRef = storage.reference.child("uploads/" + user.uid + "/" + "coverImage/")
        storageRef.putFile(it).await()
        newCoverUrl = storageRef.downloadUrl.await().toString()
      }

      if (newCoverUrl == null)
        newCoverUrl = profile.oldCoverImageUrl

      if (newProfileUrl == null)
        newProfileUrl = profile.oldProfileImageUrl

      firestore.collection("users").document(user.uid).update(
        mapOf(
          "imageUrl" to newProfileUrl.toString(),
          "coverImageUrl" to newCoverUrl,
          "firstName" to profile.firstName,
          "lastName" to profile.lastName,
          "aboutMe" to profile.aboutMe
        )
      ).await()

      return Response.Success()

    } catch (e: Exception) {
      return Response.Error(
        exception = e
      )
    }
  }

  suspend fun getProfile(): Response<Profile> {
    try {
      val user = authRemoteDataSource.getAuth() ?: throw NoAuthException()

      val doc = firestore.collection("users").document(user.uid).get().await()

      val profile = Profile(
        userId = doc.id,
        username = doc.getString("userName")!!,
        profileImageUrl = doc.getString("imageUrl")!!,
        coverImageUrl = doc.getString("coverImageUrl")!!,
        firstName = doc.getString("firstName")!!,
        lastName = doc.getString("lastName")!!,
        aboutMe = doc.getString("aboutMe")!!
      )

      return Response.Success(data = profile)

    } catch (e: Exception) {
      return Response.Error(
        exception = e
      )
    }
  }

  fun getUserEmail(): Response<String> {
    return try {
      val auth = authRemoteDataSource.getAuth()
      if (auth != null && auth.isEmailVerified) {
        Response.Success(auth.email)
      } else
        throw NoAuthException()

    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }

  suspend fun sendResetPasswordEmail(): Response<Unit> {
    return try {
      val auth = authRemoteDataSource.getAuth() ?: throw NoAuthException()

      firebaseAuth.sendPasswordResetEmail(auth.email!!).await()
      Response.Success()

    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }

  suspend fun insertOrUpdateUserReview(review: Review) {
    try {
      firestore.collection("reviews").document(firebaseAuth.uid!!).set(review).await()
    } catch (_: Exception) {
    }
  }


  suspend fun getRemoteAppUpdateDetails(): Response<UpdateDetails> {
    return try {
      val querySnapshot = firestore.collection("update_details").get().await()
      val doc = querySnapshot.documents.get(0)

      val updateDetails = UpdateDetails(
        id = 1, versionCode =
        doc.getLong("versionCode")!!.toInt(),
        versionName = doc.getString("versionName")!!,
        details = doc.get("details") as List<String>
      )

      Response.Success(updateDetails)
    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }

  suspend fun setReceiveNotification(isEnabled: Boolean): Response<Unit> {
    return try {
      val auth = authRemoteDataSource.getAuth() ?: throw NoAuthException()
      firestore.collection("users").document(auth.uid).update(
        mapOf(
          "receiveNotification" to isEnabled
        )
      ).await()
      Response.Success()
    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }

  suspend fun getReceiveNotification(): Response<Boolean> {
    return try {
      val auth = authRemoteDataSource.getAuth() ?: throw NoAuthException()
      val doc = firestore.collection("users").document(auth.uid).get().await()
      Response.Success(doc.getBoolean("receiveNotification"))
    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }

  suspend fun setShareCapsules(isEnabled: Boolean): Response<Unit> {
    return try {
      val auth = authRemoteDataSource.getAuth() ?: throw NoAuthException()
      firestore.collection("users").document(auth.uid).update(
        mapOf(
          "shareCapsules" to isEnabled
        )
      ).await()
      Response.Success()
    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }

  suspend fun getShareCapsules(): Response<Boolean> {
    return try {
      val auth = authRemoteDataSource.getAuth() ?: throw NoAuthException()
      val doc = firestore.collection("users").document(auth.uid).get().await()
      Response.Success(doc.getBoolean("shareCapsules"))
    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }
}
