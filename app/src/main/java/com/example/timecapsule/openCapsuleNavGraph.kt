package com.example.timecapsule

import android.content.Intent
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.findcapsule.FindCapsuleScreenV1
import com.example.timecapsule.ui.opencapsule.CapsuleLoadingScreen
import com.example.timecapsule.ui.opencapsule.InstructionsScreen
import com.example.timecapsule.ui.opencapsule.MapInstructionsScreen
import com.example.timecapsule.ui.opencapsule.ShowContentScreen
import com.example.timecapsule.ui.opencapsule.ShowLetterScreen
import com.example.timecapsule.viewmodel.OpenCapsuleViewModel

fun NavGraphBuilder.openCapsuleNavGraph(navController: NavController) {

  navigation(
    route = Screen.OpenCapsuleScreens.route,
    startDestination = Screen.OpenCapsuleLoadingScreen.route
  ) {
    composable(Screen.OpenCapsuleLoadingScreen.route) { backstackentry ->
      val capsuleId = backstackentry.arguments?.getString("id")!!
      val isCapsuleHunt = backstackentry.arguments?.getString("isCapsuleHunt").toBoolean()
      val isSurpriseCapsule = backstackentry.arguments?.getString("isSurpriseCapsule").toBoolean()

      val sharedViewModel =
        backstackentry.sharedViewModel<OpenCapsuleViewModel>(navController = navController)
      CapsuleLoadingScreen(
        sharedViewModel,
        capsuleId,
        isCapsuleHunt = isCapsuleHunt,
        isSurpriseCapsule=isSurpriseCapsule,
        navigate = { route ->
          navController.navigate(route) {
            popUpTo(Screen.OpenCapsuleLoadingScreen.route) {
              inclusive = true
            }
          }
        },
        popBack = { navController.popBackStack() })
    }

    composable(Screen.OpenCapsuleInstructionsScreen.route) { backstackentry ->
      val sharedViewModel =
        backstackentry.sharedViewModel<OpenCapsuleViewModel>(navController = navController)
      InstructionsScreen(sharedViewModel) { route ->
        navController.navigate(route) {
          popUpTo(Screen.OpenCapsuleInstructionsScreen.route) {
            inclusive = true
          }
        }
      }
    }
    composable(Screen.OpenCapsuleLetterScreen.route) { backstackentry ->
      val isCapsuleHunt = backstackentry.arguments?.getString("isCapsuleHunt").toBoolean() ?: false
      val sharedViewModel =
        backstackentry.sharedViewModel<OpenCapsuleViewModel>(navController = navController)
      ShowLetterScreen(viewModel = sharedViewModel, isCapsuleHunt = isCapsuleHunt) { route ->
        navController.navigate(route) {
          popUpTo(Screen.OpenCapsuleLetterScreen.route) {
            inclusive = true
          }
        }
      }
    }
    composable(Screen.OpenCapsuleMapInstructionsScreen.route) { backstackentry ->
      val sharedViewModel =
        backstackentry.sharedViewModel<OpenCapsuleViewModel>(navController = navController)
      MapInstructionsScreen(sharedViewModel) { route ->
        navController.navigate(route) {
          popUpTo(Screen.OpenCapsuleMapInstructionsScreen.route) {
            inclusive = true
          }
        }
      }
    }
    composable(Screen.OpenCapsuleFindCapsuleScreen.route) { backstackentry ->
      val context = LocalContext.current
      val sharedViewModel =
        backstackentry.sharedViewModel<OpenCapsuleViewModel>(navController = navController)
      FindCapsuleScreenV1(viewModel = sharedViewModel, navigate = { route ->
        navController.navigate(route) {
          popUpTo(Screen.OpenCapsuleFindCapsuleScreen.route) {
            inclusive = true
          }
        }
      }, onViewAr = { modelId ->
        val intent = Intent(context, ArActivity::class.java).apply {
          putExtra("modelId", modelId)
        }
        context.startActivity(intent)
      })
    }
    composable(Screen.OpenCapsuleContentScreen.route) { backstackentry ->
      val sharedViewModel =
        backstackentry.sharedViewModel<OpenCapsuleViewModel>(navController = navController)
      ShowContentScreen(viewModel = sharedViewModel)
    }
  }
}
