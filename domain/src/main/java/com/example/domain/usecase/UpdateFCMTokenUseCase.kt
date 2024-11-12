package com.example.domain.usecase

import com.example.data.remote.UserRemoteDataSource
import javax.inject.Inject

class UpdateFCMTokenUseCase @Inject constructor(
  private val userRemoteDataSource: UserRemoteDataSource
) {
  suspend operator fun invoke(){
    userRemoteDataSource.saveTokenToFirestore()
  }
}
