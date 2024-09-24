package com.example.timecapsule

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.theme.login.LogInScreen
import com.example.timecapsule.ui.theme.onboarding.OnboardingScreen
import com.example.timecapsule.ui.theme.signup.SignUpScreen

// Helper method for Onboarding Flow
fun NavGraphBuilder.onboardingNavGraph(navController: NavController) {
  navigation(
    startDestination = Screen.Onboarding.route,
    route = Screen.OnboardingScreens.route // Optional route for separation
  ) {
    composable(Screen.Onboarding.route) { OnboardingScreen(navController) }
    composable(Screen.Login.route) { LogInScreen(navController) }
    composable(Screen.Signup.route) { SignUpScreen(navController) }
  }
}