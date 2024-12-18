package com.example.timecapsule.ui.splash

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.timecapsule.R
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.openSansExtraBold
import com.example.timecapsule.viewmodel.AuthState
import com.example.timecapsule.viewmodel.CapsuleListScreenState
import com.example.timecapsule.viewmodel.SplashViewModel
import com.example.util.AskDetailsException
import com.example.util.NetWorkException

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashViewModel = hiltViewModel()) {

  val authState by viewModel.authState.collectAsState()

  LaunchedEffect(Unit) {
    viewModel.invoke()
  }

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
        .background(MaterialTheme.colorScheme.primary)
  ) {
    Column(
      modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight()
          .align(Alignment.Center),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = 20.dp)
            .size(220.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Red,
                        Color.Red,
                        Color.LightGray.copy(0.1f)
                    ),
                    center = Offset.Unspecified,
                    radius = 220f
                ),
                shape = CircleShape
            )
      ) {
        Image(
          painter = painterResource(id = com.example.timecapsule.R.drawable.onboarding_image),
          contentDescription = "Logo",
          modifier = Modifier.size(200.dp)
        )
      }

      Text(
        "Time Capsule",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 24.sp,
          fontWeight = FontWeight.Light,
          fontFamily = openSansExtraBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    }
  }
}
