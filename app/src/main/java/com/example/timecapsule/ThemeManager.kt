package com.example.timecapsule

import android.content.Context
import com.example.data.sharedpreference.ThemePreferences
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Singleton to manage theme state
object ThemeManager {
  private val _themeFlow = MutableSharedFlow<Boolean>(replay = 1)
  val themeFlow: SharedFlow<Boolean> = _themeFlow.asSharedFlow()

  suspend fun initializeTheme(context: Context) {
    val isDarkMode = ThemePreferences.isDarkMode(context)
    _themeFlow.emit(isDarkMode)
  }

  suspend fun toggleTheme(context: Context,newTheme:Boolean) {
    _themeFlow.emit(newTheme)
    withContext(Dispatchers.IO) {
      ThemePreferences.saveThemePreference(context = context,newTheme)
    }
  }
}
