package com.example.timecapsule

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.login.LogInScreen
import com.example.timecapsule.ui.onboarding.OnboardingScreen
import com.example.timecapsule.ui.signup.SignUpDetailsScreen
import com.example.timecapsule.ui.signup.SignUpScreen

// Helper method for Onboarding Flow
fun NavGraphBuilder.onboardingNavGraph(navController: NavController) {
  navigation(
    startDestination = Screen.Onboarding.route,
    route = Screen.OnboardingScreens.route // Optional route for separation
  ) {
    composable(Screen.Onboarding.route) { OnboardingScreen(navController) }
    composable(Screen.Login.route) { LogInScreen(navController) }
    composable(Screen.Signup.route) { SignUpScreen(navController) }
    composable(Screen.AskDetails.route) { SignUpDetailsScreen() }
  }
}