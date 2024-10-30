package com.example.domain.usecase

import com.example.data.sharedpreference.SharedPreferencesHelper
import javax.inject.Inject

class OnBoardingDataUseCase @Inject constructor(private val sharedPreferencesHelper: SharedPreferencesHelper) {
  fun setOnBoardingDetailsCompleted(isCompleted: Boolean) {
    sharedPreferencesHelper.setIsOnBoardingDetailsCompleted(isCompleted)
  }

  fun getIsOnBoardingDetailsCompleted(): Boolean {
    return sharedPreferencesHelper.isOnBoardingDetailsCompleted()
  }
}
