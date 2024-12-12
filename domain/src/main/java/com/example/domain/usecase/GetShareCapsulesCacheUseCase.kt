package com.example.domain.usecase

import com.example.data.repository.UserRepository
import com.example.data.sharedpreference.SharedPreferencesHelper
import com.example.util.Response
import javax.inject.Inject

class GetShareCapsulesCacheUseCase @Inject constructor(
  private val sharedPreferencesHelper: SharedPreferencesHelper
) {

  operator fun invoke(): Boolean {
    return sharedPreferencesHelper.getCanShareCapsules()
  }
}
