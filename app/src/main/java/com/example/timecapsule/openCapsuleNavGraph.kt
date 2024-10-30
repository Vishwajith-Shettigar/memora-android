package com.example.timecapsule

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.opencapsule.CapsuleLoadingScreen
import com.example.timecapsule.ui.opencapsule.InstructionsScreen
import com.example.timecapsule.viewmodel.OpenCapsuleViewModel

fun NavGraphBuilder.openCapsuleNavGraph(navController: NavController) {

  navigation(
    route = Screen.OpenCapsuleScreens.route,
    startDestination = Screen.OpenCapsuleLoadingScreen.route
  ) {
    composable(Screen.OpenCapsuleLoadingScreen.route) { backstackentry ->
      val capsuleId = backstackentry.arguments?.getString("id")!!
      Log.e("pokemon", capsuleId)
      val sharedViewModel =
        backstackentry.sharedViewModel<OpenCapsuleViewModel>(navController = navController)
      CapsuleLoadingScreen(sharedViewModel, capsuleId, navigate = { route ->
        navController.navigate(route) {
          popUpTo(Screen.OpenCapsuleLoadingScreen.route) {
            inclusive = true
          }
        }
      }, popBack = { navController.popBackStack() })
    }

    composable(Screen.OpenCapsuleInstructionsScreen.route) { backstackentry ->
      val sharedViewModel =
        backstackentry.sharedViewModel<OpenCapsuleViewModel>(navController = navController)
      InstructionsScreen(sharedViewModel) { route ->
        navController.navigate(route)
      }
    }
  }
}
