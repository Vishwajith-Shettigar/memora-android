package com.example.timecapsule

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.login.LogInScreen
import com.example.timecapsule.ui.onboarding.OnboardingScreen
import com.example.timecapsule.ui.onboarding.OnBoardingDetailsScreen
import com.example.timecapsule.ui.onboarding.SlidersScreen
import com.example.timecapsule.ui.onboarding.WelcomeScreen
import com.example.timecapsule.ui.signup.SignUpScreen
import com.example.timecapsule.ui.splash.SplashScreen

// Helper method for Onboarding Flow
fun NavGraphBuilder.onboardingNavGraph(navController: NavController) {
  navigation(
    startDestination = Screen.Splash.route,
    route = Screen.OnboardingScreens.route // Optional route for separation
  ) {
    composable(Screen.Splash.route) { SplashScreen(navController) }
    composable(Screen.WelCome.route) {
      WelcomeScreen() {
        navController.navigate(Screen.Sliders.route)
      }
    }
    composable(Screen.Sliders.route) {
      SlidersScreen() {
        navController.navigate(Screen.Onboarding.route)
      }
    }
    composable(Screen.Onboarding.route) { OnboardingScreen(navController) }
    composable(Screen.Login.route) { LogInScreen(navController) }
    composable(Screen.Signup.route) { SignUpScreen(navController) }
    composable(Screen.AskDetails.route) { OnBoardingDetailsScreen(navController) }
  }
}
