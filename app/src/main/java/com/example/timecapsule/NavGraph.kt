package com.example.timecapsule

import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.theme.capsuledetails.CapsuleDetailsScreen
import com.example.timecapsule.ui.theme.capsulelist.CapsuleCardListScreen
import com.example.timecapsule.ui.theme.findcapsule.FindCapsuleScreenV1
import com.example.timecapsule.ui.theme.login.LogInScreen
import com.example.timecapsule.ui.theme.notification.NotificationScreen
import com.example.timecapsule.ui.theme.onboarding.OnboardingScreen
import com.example.timecapsule.ui.theme.profile.ProfileScreen
import com.example.timecapsule.ui.theme.review.ReviewScreen
import com.example.timecapsule.ui.theme.selectcapsule.SelectCapsuleScreen
import com.example.timecapsule.ui.theme.selectlocation.LocationOptions
import com.example.timecapsule.ui.theme.selectlocation.SelectLocationOptionScreen
import com.example.timecapsule.ui.theme.selectlocation.SelectLocationScreen
import com.example.timecapsule.ui.theme.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.theme.selecttime.SelectTimeScreen
import com.example.timecapsule.ui.theme.sharewithpeople.ShareOptionScreen
import com.example.timecapsule.ui.theme.sharewithpeople.SharePeopleOptions
import com.example.timecapsule.ui.theme.sharewithpeople.ShareScreen
import com.example.timecapsule.ui.theme.signup.SignUpScreen
import com.example.timecapsule.ui.theme.uploadfiles.UploadFilesScreen
import com.example.timecapsule.ui.theme.util.DeviceType

@Composable
fun NavGraph() {
  val isTablet = DeviceType.isTablet()
  if (isTablet)
    TabletLayoutV1()
  else
    MobileLayoutV1()
}

@Composable
fun MobileLayoutV1() {
  val activity = (LocalContext.current as Activity)
  val navController = rememberNavController()

  // List of screens that should display the Bottom Navigation Bar
  val bottomNavScreens = listOf(
    Screen.Home.route,
    Screen.Location.route,
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
      navController = navController,
      modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding()),
      startDestination = Screen.OnboardingScreens.route,
    ) {
      onboardingNavGraph(navController)
      mainNavGraph(navController)
      addCapsuleNavGraph(navController, activity)
    }
  }
}

@Composable
fun TabletLayoutV1() {
  val activity = (LocalContext.current as Activity)
  val navController = rememberNavController()

  // List of screens that should display the Bottom Navigation Bar
  val bottomNavScreens = listOf(
    Screen.Home.route,
    Screen.Location.route,
    Screen.Notification.route,
    Screen.Profile.route
  )

  Scaffold { paddingValues ->
    Row(modifier = Modifier.padding()) {
      val currentBackStackEntry = navController.currentBackStackEntryAsState().value
      if (currentBackStackEntry?.destination?.route in bottomNavScreens) {
        NavigationRail(navController)
      }
      Box(modifier = Modifier.fillMaxSize())
      {
        NavHost(
          navController = navController,
          startDestination = Screen.OnboardingScreens.route,
        ) {
          onboardingNavGraph(navController)
          mainNavGraph(navController)
          addCapsuleNavGraph(navController, activity)
        }
      }
    }
  }
}
