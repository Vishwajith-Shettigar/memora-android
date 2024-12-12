package com.example.data.repository

import com.example.data.local.entity.Review
import com.example.data.remote.UserRemoteDataSource
import com.example.model.Profile
import com.example.model.UpdateProfile
import com.example.model.UserDetails
import com.example.util.Response
import javax.inject.Inject

interface UserRepository {
  suspend fun saveUserDetails(userName: String, fName: String, lName: String): Response<Unit>
  suspend fun getUserDetails(userId: String): Response<UserDetails>
  suspend fun checkUserNameDoesntExists(userName: String): Response<Exception>
  suspend fun checkUserRecordExists(userId: String): Response<Any>
  suspend fun searchUsers(query: String): Response<List<UserDetails>>
  suspend fun getProfile(): Response<Profile>
  suspend fun updateProfile(profile: UpdateProfile): Response<Unit>
  fun getUserEmail(): Response<String>
  suspend fun sendResetPasswordEmail(): Response<Unit>
  suspend fun insertOrUpdateUserReview(review: Review)
  suspend fun setReceiveNotification(isEnabled: Boolean): Response<Unit>
  suspend fun setShareCapsules(isEnabled: Boolean): Response<Unit>
  suspend fun getReceiveNotification(): Response<Boolean>
  suspend fun getShareCapsules(): Response<Boolean>
}

class UserRepositoryImpl @Inject constructor(
  val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {
  override suspend fun saveUserDetails(
    userName: String,
    fName: String,
    lName: String
  ): Response<Unit> {
    return userRemoteDataSource.saveUserDetails(userName, fName, lName)
  }

  override suspend fun getUserDetails(userId: String): Response<UserDetails> {
    return userRemoteDataSource.getUserDetails(userId)
  }

  override suspend fun checkUserNameDoesntExists(userName: String): Response<Exception> {
    return userRemoteDataSource.checkUserNameDoesntExist(userName)
  }

  override suspend fun checkUserRecordExists(userId: String): Response<Any> {
    return userRemoteDataSource.checkUserRecordExists(userId)
  }

  override suspend fun searchUsers(query: String): Response<List<UserDetails>> {
    return userRemoteDataSource.searchUsers(query)
  }

  override suspend fun getProfile(): Response<Profile> {
    return userRemoteDataSource.getProfile()
  }

  override suspend fun updateProfile(profile: UpdateProfile): Response<Unit> {
    return userRemoteDataSource.updateProfile(profile)
  }

  override fun getUserEmail(): Response<String> {
    return userRemoteDataSource.getUserEmail()
  }

  override suspend fun sendResetPasswordEmail(): Response<Unit> {
    return userRemoteDataSource.sendResetPasswordEmail()
  }

  override suspend fun insertOrUpdateUserReview(review: Review) {
    userRemoteDataSource.insertOrUpdateUserReview(review)
  }

  override suspend fun setReceiveNotification(isEnabled: Boolean): Response<Unit> {
    return userRemoteDataSource.setReceiveNotification(isEnabled)
  }

  override suspend fun setShareCapsules(isEnabled: Boolean): Response<Unit> {
    return userRemoteDataSource.setShareCapsules(isEnabled)
  }

  override suspend fun getReceiveNotification(): Response<Boolean> {
    return userRemoteDataSource.getReceiveNotification()
  }

  override suspend fun getShareCapsules(): Response<Boolean> {
    return userRemoteDataSource.getShareCapsules()
  }
}
