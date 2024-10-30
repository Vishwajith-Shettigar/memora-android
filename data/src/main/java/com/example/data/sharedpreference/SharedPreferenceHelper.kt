package com.example.data.sharedpreference


import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

  companion object {
    private const val PREF_NAME = "user_preferences"
    private const val KEY_IS_DETAILS_COMPLETED = "isDetailsCompleted"
  }

  private val sharedPreferences: SharedPreferences =
    context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

  fun setIsOnBoardingDetailsCompleted(isCompleted: Boolean) {
    sharedPreferences.edit().putBoolean(KEY_IS_DETAILS_COMPLETED, isCompleted).apply()
  }

  fun isOnBoardingDetailsCompleted(): Boolean {
    return sharedPreferences.getBoolean(KEY_IS_DETAILS_COMPLETED, false)
  }

  fun saveCapsuleOpeningLastScreenRoute(route: String, capsuleId: String) {
    sharedPreferences.edit().putString(capsuleId, route).apply()
  }

  fun getCapsuleOpeningLastScreenRoute(capsuleId: String): String? {
    return sharedPreferences.getString(capsuleId,null)
  }

  fun deleteCapsuleOpeningLastScreenRoute(capsuleId: String){
    sharedPreferences.edit().remove(capsuleId).apply()
  }
}
