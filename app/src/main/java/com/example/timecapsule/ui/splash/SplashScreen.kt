package com.example.timecapsule.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.viewmodel.AuthState
import com.example.timecapsule.viewmodel.SplashViewModel
import com.example.util.AskDetailsException

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel = hiltViewModel()) {

  val authState by viewModel.authState.collectAsState()
  viewModel.invoke()

  LaunchedEffect(key1 = authState) {
    when (authState) {
      is AuthState.Success -> {
        navController.navigate(Screen.MainScreens.route) {
          popUpTo(Screen.OnboardingScreens.route) {
            inclusive = true
          }
        }
      }

      is AuthState.Error -> {
        if ((authState as AuthState.Error).exception is AskDetailsException) {
          navController.navigate(Screen.AskDetails.route) {
            popUpTo(Screen.OnboardingScreens.route) {
              inclusive = true
            }
          }
        } else {
          navController.navigate(Screen.Onboarding.route) {
            popUpTo(Screen.OnboardingScreens.route)
          }
        }
      }

      else -> {}
    }

  }

  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(Color.Red)
  ) {

  }

}