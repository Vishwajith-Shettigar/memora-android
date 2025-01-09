package com.example.timecapsule

import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.util.DeviceType

@Composable
fun NavGraph(navController: NavHostController) {
  val isTablet = DeviceType.isTablet()
  if (isTablet)
    TabletLayoutV1(navController)
  else
    MobileLayoutV1(navController)
}

@Composable
fun MobileLayoutV1(navController: NavHostController) {
  val activity = (LocalContext.current as Activity)

  // List of screens that should display the Bottom Navigation Bar
  val bottomNavScreens = listOf(
    Screen.Home.route,
    Screen.NearByCapsules.route,
    Screen.Notification.route,
    Screen.Profile.route
  )

  Scaffold(
    bottomBar = {
      val currentBackStackEntry = navController.currentBackStackEntryAsState().value
      if (currentBackStackEntry?.destination?.route in bottomNavScreens) {
        BottomNavigationBar(navController)
      }
    }
  ) { paddingValues ->
    NavHost(
      modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
      navController = navController,
      startDestination = Screen.OnboardingScreens.route,
    ) {
      onboardingNavGraph(navController)
      mainNavGraph(navController)
      addCapsuleNavGraph(navController, activity)
      openCapsuleNavGraph(navController = navController)
    }
  }
}

@Composable
fun TabletLayoutV1(navController: NavHostController) {
  val activity = (LocalContext.current as Activity)

  // List of screens that should display the Bottom Navigation Bar
  val bottomNavScreens = listOf(
    Screen.Home.route,
    Screen.NearByCapsules.route,
    Screen.Notification.route,
    Screen.Profile.route
  )

  Scaffold { paddingValues ->
    Row(modifier = Modifier.padding()) {
      val currentBackStackEntry = navController.currentBackStackEntryAsState().value
      if (currentBackStackEntry?.destination?.route in bottomNavScreens) {
        NavigationRail(navController)
      }
      Box(modifier = Modifier.fillMaxSize().padding(bottom = paddingValues.calculateBottomPadding()))
      {
        NavHost(
          navController = navController,
          startDestination = Screen.OnboardingScreens.route,
        ) {
          onboardingNavGraph(navController)
          mainNavGraph(navController)
          addCapsuleNavGraph(navController, activity)
          openCapsuleNavGraph(navController = navController)
        }
      }
    }
  }
}
