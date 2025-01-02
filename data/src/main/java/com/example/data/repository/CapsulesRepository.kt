package com.example.data.repository

import com.example.data.dto.EmailSharingCapsuleDto
import com.example.data.remote.CapsulesRemoteDataSource
import com.example.data.remote.UserRemoteDataSource
import com.example.data.retrofilApi.EmailSharingCapsuleApi
import com.example.model.CapsuleAsset
import com.example.model.CapsuleDetails
import com.example.util.Response
import javax.inject.Inject


interface CapsulesRepository {
  suspend fun getCapsulesList(): Response<List<CapsuleDetails>>
  suspend fun getCapsuleAssets(): Response<List<CapsuleAsset>>
  suspend fun createCapsule(capsuleDetails: CapsuleDetails): Response<Unit>
  suspend fun getCapsuleDetails(capsuleId: String): Response<CapsuleDetails>
  suspend fun setCapsuleOpened(capsuleId: String): Response<Unit>
  suspend fun sendEmailCaspuleSharing(emailSharingCapsuleDto: EmailSharingCapsuleDto): Response<Unit>
  suspend fun getSurpriseCapsuleDetails(capsuleId: String):Response<CapsuleDetails>
}

class CapsulesRepositoryImpl @Inject constructor(
  val capsulesRemoteDataSource: CapsulesRemoteDataSource,
  val emailSharingCapsuleApi: EmailSharingCapsuleApi
) : CapsulesRepository {
  override suspend fun getCapsulesList(): Response<List<CapsuleDetails>> {
    return capsulesRemoteDataSource.getCapsulesList()
  }

  override suspend fun getCapsuleAssets(): Response<List<CapsuleAsset>> {
    return capsulesRemoteDataSource.getCapsuleAssets()
  }

  override suspend fun createCapsule(capsuleDetails: CapsuleDetails): Response<Unit> {
    return capsulesRemoteDataSource.createCapsule(capsuleDetails)
  }

  override suspend fun getCapsuleDetails(capsuleId: String): Response<CapsuleDetails> {
    return capsulesRemoteDataSource.getCapsuleDetails(capsuleId)
  }

  override suspend fun setCapsuleOpened(capsuleId: String): Response<Unit> {
    return capsulesRemoteDataSource.setCapsuleOpened(capsuleId)
  }

  override suspend fun sendEmailCaspuleSharing(emailSharingCapsuleDto: EmailSharingCapsuleDto): Response<Unit> {
    return try {
      emailSharingCapsuleApi.shareCapsuleWithEmails(emailSharingCapsuleDto)
      Response.Success()
    } catch (e: Exception) {
      Response.Error(exception = e)
    }
  }

  override suspend fun getSurpriseCapsuleDetails(capsuleId: String): Response<CapsuleDetails> {
    return capsulesRemoteDataSource.getSurpriseCapsuleDetails(capsuleId)
  }
}
