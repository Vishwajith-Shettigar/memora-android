package com.example.timecapsule.util

import android.content.Context
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.data.sharedpreference.ThemePreferences
import com.example.domain.usecase.OnBoardingDataUseCase
import com.example.domain.usecase.SignInUseCase
import com.example.timecapsule.ui.login.LogInScreen
import com.example.timecapsule.viewmodel.LogInViewModel
import com.example.util.Response
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import java.lang.Exception
import javax.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
  application = HiltTestApplication::class,
  instrumentedPackages = ["androidx.loader.content"],
  sdk = [29]
)
class LogInUnitTest {

  @get:Rule
  val hiltRule = HiltAndroidRule(this)

  @get:Rule
  val composeTestRule = createComposeRule()

  lateinit var loginViewMode: LogInViewModel

  @Mock
  lateinit var signInUseCase: SignInUseCase

  @Mock
  lateinit var onBoardingDataUseCase: OnBoardingDataUseCase

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)
    hiltRule.inject()
    loginViewMode = LogInViewModel(signInUseCase, onBoardingDataUseCase)
  }

  @Test
  fun testLoginError() = runTest {
    // Set up mock response for the signInUseCase
    `when`(signInUseCase.invoke("invalidUser", "wrongPassword"))
      .thenReturn(Response.Error(data = null, exception = Exception("Password Doesn't match.")))

    // Set the Compose UI content
    composeTestRule.setContent {
      LogInScreen(viewModel = loginViewMode)
    }

    // Type in invalid username and password using testTags
    composeTestRule.onNodeWithTag("email_field").performTextInput("invalidUser")
    composeTestRule.onNodeWithTag("password_field").performTextInput("wrongPassword")

    // Click the Login button
    composeTestRule.onNodeWithText("LOG IN").performClick()

    composeTestRule.waitForIdle()
//     Check if the Toast message is shown
    val toastMessage = ShadowToast.getTextOfLatestToast()
    assertTrue(toastMessage.contains("Password Doesn't match."))
  }
}
