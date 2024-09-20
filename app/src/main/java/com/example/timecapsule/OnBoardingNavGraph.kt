package com.example.timecapsule

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.theme.login.LogInScreen
import com.example.timecapsule.ui.theme.onboarding.OnboardingScreen
import com.example.timecapsule.ui.theme.signup.SignUpScreen

@Composable
fun OnBoardingNavGraph() {
  val navController = rememberNavController()
  NavHost(
    navController = navController,
    enterTransition = {
      slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Start,
        tween(1000)
      )
    },
    popEnterTransition = {
      slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.End,
        tween(1000)
      )
    },
    startDestination = Screen.Onboarding.route,
  ) {
    // Splash, Onboarding, and Authentication Flow
//   composable(Screen.Splash.route) { SplashScreen(navController) }
    composable(Screen.Onboarding.route) { OnboardingScreen(navController) }
    composable(Screen.Login.route) { LogInScreen(navController) }
    composable(Screen.Signup.route) { SignUpScreen(navController) }
    composable(Screen.Main.route) { MainNavGraph() }
  }
}