package com.example.domain.usecase

import com.example.data.local.entity.UpdateDetails
import com.example.data.remote.UserRemoteDataSource
import com.example.util.Response
import javax.inject.Inject

class GetRemoteAppUpdateDetailsUseCase @Inject constructor(
  private val userRemoteDataSource: UserRemoteDataSource
) {

  suspend operator fun invoke(): Response<UpdateDetails> {
    return userRemoteDataSource.getRemoteAppUpdateDetails()
  }
}
