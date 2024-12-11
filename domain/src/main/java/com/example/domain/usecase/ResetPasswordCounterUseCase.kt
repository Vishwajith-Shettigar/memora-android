package com.example.domain.usecase

import android.util.Log
import com.example.data.sharedpreference.SharedPreferencesHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class CanResetPasswordCounterUseCase @Inject constructor(
  private val sharedPreferencesHelper: SharedPreferencesHelper
) {

  companion object {
    private const val MAX_RESET_COUNT = 3
  }

  fun incrementCounter() {
    var resetCount = sharedPreferencesHelper.getResetPasswordCounter()
    if (resetCount < MAX_RESET_COUNT) {
      sharedPreferencesHelper.setResetPasswordCounter(++resetCount)
    }
  }

  operator fun invoke(): Boolean {
    val currentDate = getCurrentDate()
    val lastResetDate = sharedPreferencesHelper.getLastPasswordResetDate() ?: ""
    var resetCount = sharedPreferencesHelper.getResetPasswordCounter()

    // Reset the counter if the date has changed
    if (lastResetDate != currentDate) {
      sharedPreferencesHelper.setResetPasswordCounter(0)
      sharedPreferencesHelper.setLastPasswordResetDate(currentDate)
      return true
    }

    // Allow reset if the limit has not been reached
    if (resetCount < MAX_RESET_COUNT) {
      return true
    }

    return false
  }

  fun getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(Date())
  }
}
