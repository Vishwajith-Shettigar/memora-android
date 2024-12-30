package com.example.data.sharedpreference

import android.content.Context
import android.content.SharedPreferences

object ThemePreferences {
  private const val PREFS_NAME = "theme_preferences"
  private const val THEME_KEY = "is_dark_mode"

  // Retrieve the saved theme preference (default is light mode)
  fun isDarkMode(context: Context): Boolean {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return sharedPreferences.getBoolean(THEME_KEY, false) // Default to false (light mode)
  }

  // Save the theme preference (true for dark mode, false for light mode)
  fun saveThemePreference(context: Context, isDarkMode: Boolean) {
    val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    sharedPreferences.edit().putBoolean(THEME_KEY, isDarkMode).apply()
  }
}
