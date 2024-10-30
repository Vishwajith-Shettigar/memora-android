package com.example.domain.usecase

import com.example.data.sharedpreference.SharedPreferencesHelper
import javax.inject.Inject

class OpenCapsuleScreenCheckPointUseCase @Inject constructor(private val sharedPreferencesHelper: SharedPreferencesHelper) {
  fun saveCapsuleOpeningLastScreenRoute(route: String, capsuleId: String) {
    sharedPreferencesHelper.saveCapsuleOpeningLastScreenRoute(route, capsuleId)
  }

  fun getCapsuleOpeningLastScreenRoute(capsuleId: String): String? {
    return sharedPreferencesHelper.getCapsuleOpeningLastScreenRoute(capsuleId)
  }

  fun deleteCapsuleOpeningLastScreenRoute(capsuleId: String) {
    sharedPreferencesHelper.deleteCapsuleOpeningLastScreenRoute(capsuleId)
  }
}