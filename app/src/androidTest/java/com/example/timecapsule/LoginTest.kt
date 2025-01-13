package com.example.timecapsule

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.domain.usecase.OnBoardingDataUseCase
import com.example.domain.usecase.SignInUseCase
import com.example.timecapsule.ui.login.LogInScreen
import com.example.timecapsule.viewmodel.LogInViewModel
import com.example.util.Response
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LoginTest {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @get:Rule
  val composeTestRule = createComposeRule()
  lateinit var loginViewMode: LogInViewModel

  @Mock
  lateinit var onBoardingDataUseCase: OnBoardingDataUseCase

  @Mock
  lateinit var signInUseCase: SignInUseCase

  @Before
  fun setUp() {
    MockitoAnnotations.openMocks(this)
    hiltRule.inject()
    loginViewMode = LogInViewModel(signInUseCase, onBoardingDataUseCase)
  }


  // Todo: fix mockito final class error.
  @Test
  fun testLoginError() = runTest {
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

    // Wait for UI to recombine and reflect state changes (error message)
    composeTestRule.waitForIdle()
  }
}
