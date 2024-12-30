package com.example.timecapsule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.data.sharedpreference.ThemePreferences
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.service.CAPSULE_SHARED_NOTIFICATION
import com.example.timecapsule.ui.CapsuleCreationSaving.CapsuleCreationSavingScreen
import com.example.timecapsule.ui.CapsuleNameAndDescription.CapsuleNameAndDescription
import com.example.timecapsule.ui.capsuledetails.CapsuleDetailsScreenv1
import com.example.timecapsule.ui.capsulelist.v2.CapsuleCard
import com.example.timecapsule.ui.capsulelist.v2.CapsuleCardListScreen
import com.example.timecapsule.ui.editprofile.EditProfileScreen
import com.example.timecapsule.ui.onboarding.SlideOne
import com.example.timecapsule.ui.onboarding.SlideThree
import com.example.timecapsule.ui.onboarding.SlideTwo
import com.example.timecapsule.ui.onboarding.SlidersScreen
import com.example.timecapsule.ui.onboarding.WelcomeScreen
import com.example.timecapsule.ui.opencapsule.CapsuleLoadingScreen
import com.example.timecapsule.ui.opencapsule.InstructionsScreen
import com.example.timecapsule.ui.opencapsule.MapInstructionsScreen
import com.example.timecapsule.ui.opencapsule.ShowContentScreen
import com.example.timecapsule.ui.opencapsule.ShowLetterScreen
import com.example.timecapsule.ui.setting.SettingScreen
import com.example.timecapsule.ui.setting.options.ChangeLanguageScreen
import com.example.timecapsule.ui.setting.options.ChangePasswordScreen
import com.example.timecapsule.ui.setting.options.ContactUsScreen
import com.example.timecapsule.ui.setting.options.PrivacyPolicyScreen
import com.example.timecapsule.ui.setting.options.PrivacyScreen
import com.example.timecapsule.ui.setting.options.TermsAndServiceScreen
import com.example.timecapsule.ui.setting.options.UpdateScreen
import com.example.timecapsule.ui.splash.SplashScreen
import com.example.timecapsule.ui.theme.TimeCapsuleTheme
import com.example.timecapsule.ui.viewprofile.ViewProfileScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mapbox.common.MapboxOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    lifecycleScope.launch {
      ThemeManager.initializeTheme(this@MainActivity)
    }
    enableEdgeToEdge()
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      var isDarkMode by remember { mutableStateOf(ThemePreferences.isDarkMode(this)) }

      LaunchedEffect(true) {
        ThemeManager.themeFlow.collect { newTheme ->
          isDarkMode = newTheme
        }
      }

      val systemUiController = rememberSystemUiController()
      LaunchedEffect(isDarkMode) {
        systemUiController.setSystemBarsColor(
          color =
          Color.Transparent,
          darkIcons = !isDarkMode
        )
      }

      MapboxOptions.accessToken = BuildConfig.MAP_BOX_PUBLIC_ACCESS_TOKEN
      TimeCapsuleTheme(isDarkMode) {
        val navController = rememberNavController()


//          OnboardingScreen(modifier = Modifier.padding(innerPadding))
//          SignUpScreen(modifier = Modifier.padding(innerPadding))
//          SignUpDetailsScreen(modifier = Modifier.padding(innerPadding))
//          LogInScreen(modifier = Modifier.padding(innerPadding))
//          CapsuleCardListScreen()
//        Scaffold { innerPadding->
//          SelectTime(Modifier.padding(innerPadding))
//        SelectTimeScreen()
//        ShareOptionScreen()
//        ShareScreen()
//        SelectLocationOptionScreen()
//        SelectCapsuleScreen()
//        ViewCapsule()
//        UploadFilesScreen()
//        FindCapsuleScreenV1()
//        ReviewScreen()
//        CapsuleDetailsScreen()
//        ArScreen()
//        ProfileScreen()
//        NotificationScreen()
//        WriteLetterScreen()
//        OnBoardingNavGraph()

        NavGraph(navController)

//        WelcomeScreen()

//        SplashScreen(navController = navController)

//        SlideOne()

//        SlideTwo()

//        SlideThree()

//        SlidersScreen()

//        CapsuleDetailsScreenv1()

//        CapsuleCard()


//        CapsuleCardListScreen()
//        ContactUsScreen {
//
//        }

//        PrivacyScreen(onBackClick = {}, onTermsAndServicesClicked = {}, onPrivacyPolicyClicked = {})

//        PrivacyPolicyScreen {
//
//        }

//        TermsAndServiceScreen {
//
//        }

//        EditProfileScreen()
//        ViewProfileScreen()
//        SettingScreen()

//        ChangePasswordScreen()
//        ChangeLanguageScreen()

//        ReviewScreen()

//        UpdateScreen()

        val capsuleId = intent.getStringExtra("capsuleId")
        val notificationType = intent.getStringExtra("notificationType")
        if (notificationType == CAPSULE_SHARED_NOTIFICATION) {
          capsuleId?.let {
            LaunchedEffect(it) {
              navController.navigate(Screen.CapsuleDetails.createRoute(capsuleId))
            }
          }
        }

//        CapsuleCreationSavingScreen()
//        InstructionsScreen()
//        MapInstructionsScreen()
//        ShowLetterScreen()
//        CapsuleLoadingScreen()
//        CapsuleNameAndDescription {
//
//        }
//        FileInfoDisplay()
//        ShowContentScreen()
      }
    }
  }
}
