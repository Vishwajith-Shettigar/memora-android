package com.example.data.repository

import com.example.data.remote.CapsulesRemoteDataSource
import com.example.data.remote.UserRemoteDataSource
import com.example.model.CapsuleDetails
import com.example.util.Response
import javax.inject.Inject


interface CapsulesRepository {
  suspend fun getCapsulesList(): Response<List<CapsuleDetails>>
}

class CapsulesRepositoryImpl @Inject constructor(
  val capsulesRemoteDataSource: CapsulesRemoteDataSource
) : CapsulesRepository {
  override suspend fun getCapsulesList(): Response<List<CapsuleDetails>> {
    return capsulesRemoteDataSource.getCapsulesList()
  }

}
