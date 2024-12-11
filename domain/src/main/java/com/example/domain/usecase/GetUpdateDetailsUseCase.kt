package com.example.domain.usecase

import com.example.data.local.entity.UpdateDetails
import com.example.data.repository.UpdateDetailsRepository
import javax.inject.Inject

class GetUpdateDetailsUseCase @Inject constructor(
  private val repository: UpdateDetailsRepository
) {
  suspend operator fun invoke(): UpdateDetails? {
    return repository.getUpdateDetails()
  }
}
