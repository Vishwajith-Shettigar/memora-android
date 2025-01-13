package com.example.timecapsule.util

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.data.sharedpreference.ThemePreferences
import com.example.domain.usecase.OnBoardingDataUseCase
import com.example.domain.usecase.SignInUseCase
import com.example.timecapsule.ui.theme.model1Color
import com.example.timecapsule.ui.theme.model2Color
import com.example.timecapsule.ui.theme.model3Color
import com.example.timecapsule.ui.theme.model4Color
import com.example.timecapsule.viewmodel.LogInViewModel
import com.google.ar.core.ArCoreApk
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import javax.inject.Inject
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.mockStatic
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
  application = HiltTestApplication::class,
  instrumentedPackages = ["androidx.loader.content"],
  sdk = [29]
)
class UtilUnitTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @Inject
  lateinit var themePreferences: ThemePreferences

  @get:Rule
  val composeTestRule = createComposeRule()

  lateinit var loginViewMode: LogInViewModel

  @Mock
  lateinit var signInUseCase: SignInUseCase

  @Mock
  lateinit var onBoardingDataUseCase: OnBoardingDataUseCase

  @Inject
  @ApplicationContext
  lateinit var context: Context

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)

    hiltRule.inject()


    // Obtain the ViewModel using ViewModelProvider
    loginViewMode = LogInViewModel(signInUseCase, onBoardingDataUseCase)

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

  @Test
  fun getModelColor_ReturnsRightColor_WhenModelIdIsIsInList() {
    assertTrue(getModelColor("100") == model1Color)
    assertTrue(getModelColor("200") == model2Color)
    assertTrue(getModelColor("300") == model3Color)
    assertTrue(getModelColor("400") == model4Color)
  }

  @Test
  fun getModelColor_ReturnsDefaulttColor_WhenModelIdIsNotInList() {
    assertTrue(getModelColor("500") == model1Color)
  }
}
