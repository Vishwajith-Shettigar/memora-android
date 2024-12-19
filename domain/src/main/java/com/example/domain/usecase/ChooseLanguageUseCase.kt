package com.example.domain.usecase

import com.example.data.sharedpreference.SharedPreferencesHelper
import javax.inject.Inject

class ChooseLanguageUseCase @Inject constructor(
  private val sharedPreferencesHelper: SharedPreferencesHelper
) {

  fun setSelectedLanguageCode(code: String) {
    sharedPreferencesHelper.setSelectedLanguageCode(code)
  }

  fun getSelectedLanguageCode(): String {
    return sharedPreferencesHelper.getSelectedLanguageCode()
  }
}
