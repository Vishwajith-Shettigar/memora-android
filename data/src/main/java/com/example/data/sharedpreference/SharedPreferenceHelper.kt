package com.example.data.sharedpreference


import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

  companion object {
    private const val PREF_NAME = "user_preferences"
    private const val KEY_IS_DETAILS_COMPLETED = "isDetailsCompleted"
    private const val LAST_PASSWORD_RESET_DATE = "last_password_reset_date"
    private const val RESET_COUNTER = "reset_counter"
    private const val SELECTED_LANGUAGE = "selected_language"
    private const val IS_RECEIVE_NOTIFICATIONS = "is_receive_notification"
    private const val CAN_SHARE_CAPSULES = "can_share_capsules"
    private const val LOCAL_FCM_TOKEN = "local_fcm_token"
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
    return sharedPreferences.getString(capsuleId, null)
  }

  fun deleteCapsuleOpeningLastScreenRoute(capsuleId: String) {
    sharedPreferences.edit().remove(capsuleId).apply()
  }

  fun setLastPasswordResetDate(date: String) {
    sharedPreferences.edit().putString(LAST_PASSWORD_RESET_DATE, date).apply()
  }

  fun getLastPasswordResetDate(): String? {
    return sharedPreferences.getString(LAST_PASSWORD_RESET_DATE, null)
  }

  fun setResetPasswordCounter(counter: Int) {
    sharedPreferences.edit().putInt(RESET_COUNTER, counter).apply()
  }

  fun getResetPasswordCounter(): Int {
    return sharedPreferences.getInt(RESET_COUNTER, 0)
  }

  fun setSelectedLanguageCode(code: String) {
    sharedPreferences.edit().putString(SELECTED_LANGUAGE, code).apply()
  }

  fun getSelectedLanguageCode(): String {
    return sharedPreferences.getString(SELECTED_LANGUAGE, "en") ?: "en"
  }

  fun setReceiveNotificaions(isEnabled: Boolean) {
    sharedPreferences.edit().putBoolean(IS_RECEIVE_NOTIFICATIONS, isEnabled).apply()
  }

  fun setCanShareCapsules(isEnabled: Boolean) {
    sharedPreferences.edit().putBoolean(CAN_SHARE_CAPSULES, isEnabled).apply()
  }

  fun getReceiveNotificaions(): Boolean {
    return sharedPreferences.getBoolean(IS_RECEIVE_NOTIFICATIONS, true)
  }

  fun getCanShareCapsules(): Boolean {
    return sharedPreferences.getBoolean(CAN_SHARE_CAPSULES, true)
  }
}
