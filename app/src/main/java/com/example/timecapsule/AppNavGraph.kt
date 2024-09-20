package com.example.timecapsule

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.theme.capsulelist.CapsuleCardListScreen
import com.example.timecapsule.ui.theme.findcapsule.FindCapsuleScreenV1
import com.example.timecapsule.ui.theme.notification.NotificationScreen
import com.example.timecapsule.ui.theme.onboarding.OnboardingScreen
import com.example.timecapsule.ui.theme.profile.ProfileScreen
import com.example.timecapsule.ui.theme.selectlocation.SelectLocationOptionScreen
import com.example.timecapsule.ui.theme.selectlocation.SelectLocationScreen
import com.example.timecapsule.ui.theme.selectlocation.SelectionScreen
import com.example.timecapsule.ui.theme.selecttime.SelectTimeScreen
import androidx.compose.material3.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.ui.res.painterResource
import com.example.timecapsule.ui.theme.login.LogInScreen
import com.example.timecapsule.ui.theme.signup.SignUpScreen
import java.util.Locale

@Composable
fun AppNavGraph() {
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
      startDestination = Screen.Onboarding.route,
      modifier = Modifier.padding(paddingValues)
    ) {
      // Splash, Onboarding, and Authentication Flow
//      composable(Screen.Splash.route) { SplashScreen(navController) }
      composable(Screen.Onboarding.route) { OnboardingScreen(navController) }
      composable(Screen.Login.route) { LogInScreen(navController) }
      composable(Screen.Signup.route) { SignUpScreen(navController) }

      // Main Flow with Bottom Nav
      composable(Screen.Home.route) { CapsuleCardListScreen(navController) }
      composable(Screen.Location.route) { FindCapsuleScreenV1(navController) }
      composable(Screen.Notification.route) { NotificationScreen(navController) }
      composable(Screen.Profile.route) { ProfileScreen(navController) }

      // Subscreens in Home (no Bottom Nav)
      composable(Screen.SelectTime.route) { SelectTimeScreen(navController) }
      composable(Screen.SelectLocation.route) { SelectLocationScreen(navController) }
//      composable(Screen.UploadContent.route) { UploadContentScreen(navController) }
//      composable(Screen.ReviewContent.route) { ReviewContentScreen(navController) }

      // Subscreens in Profile (no Bottom Nav)
//      composable(Screen.Settings.route) { SettingsScreen(navController) }
//      composable(Screen.Help.route) { HelpScreen(navController) }
    }
  }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
  val items = listOf(
    NavItem(Screen.Home, R.drawable.ic_home, "home"),
    NavItem(Screen.Location, R.drawable.ic_location_search, "location"),
    NavItem(Screen.Notification, R.drawable.ic_notification, "notification"),
    NavItem(Screen.Profile, R.drawable.ic_person, "profile"),
  )
  BottomNavigation {
    val currentRoute = navController.currentDestination?.route
    items.forEach { item ->
      BottomNavigationItem(
        icon = {
          Icon(
            painter = painterResource(id = item.icon),
            contentDescription = null
          )
        },
        label = { Text(item.screen.route.capitalize(Locale.ROOT)) },
        selected = currentRoute == item.screen.route,
        onClick = {
          navController.navigate(item.screen.route) {
            popUpTo(navController.graph.startDestinationId) { saveState = true }
            launchSingleTop = true
            restoreState = true
          }
        }
      )
    }
  }
}

data class NavItem(val screen: Screen, val icon: Int, val contentDescription: String)

