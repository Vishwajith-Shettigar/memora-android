package com.example.timecapsule.util

import android.content.Context
import com.example.data.sharedpreference.ThemePreferences
import com.google.ar.core.ArCoreApk
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import javax.inject.Inject
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@Config(application = HiltTestApplication::class)
@RunWith(RobolectricTestRunner::class)
class UtilUnitTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var themePreferences: ThemePreferences

  @Inject
  @ApplicationContext
  lateinit var context: Context

  @Before
  fun setUp() {
    hiltRule.inject()
  }

  @Test
  fun isDarkMode_ReturnsFalse_ForFirstTimeUser() {
    assertFalse(themePreferences.isDarkMode(context = context))
  }

  @Test
  fun isDarkMode_ReturnsTrue_WhenDarkModeIsEnabled() {
    assertFalse(themePreferences.isDarkMode(context = context))
    themePreferences.saveThemePreference(context, true)
    assertTrue(themePreferences.isDarkMode(context = context))
  }

  @Test
  fun arCheckAvailability_ReturnsTrue_WhenDeviceSupports() {
    val mockArCoreApk = mock(ArCoreApk::class.java)
    `when`(mockArCoreApk.checkAvailability(context))
      .thenReturn(ArCoreApk.Availability.SUPPORTED_INSTALLED)

    mockStatic(ArCoreApk::class.java).use { mockedStatic ->
      mockedStatic.`when`<ArCoreApk> { ArCoreApk.getInstance() }.thenReturn(mockArCoreApk)
      assertTrue(checkARCoreAvailability(context))
    }
  }

  @Test
  fun `arCheckAvailability_ReturnsFalse_WhenDeviceDoesn'tSupports`() {
    val mockArCoreApk = mock(ArCoreApk::class.java)
    `when`(mockArCoreApk.checkAvailability(context))
      .thenReturn(ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE)

    mockStatic(ArCoreApk::class.java).use { mockedStatic ->
      mockedStatic.`when`<ArCoreApk> { ArCoreApk.getInstance() }.thenReturn(mockArCoreApk)
      assertFalse(checkARCoreAvailability(context))
    }
  }
}
