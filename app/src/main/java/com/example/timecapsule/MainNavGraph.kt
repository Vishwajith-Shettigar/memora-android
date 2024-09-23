package com.example.timecapsule

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import com.example.timecapsule.ui.theme.profile.ProfileScreen
import com.example.timecapsule.ui.theme.selectlocation.SelectLocationScreen
import com.example.timecapsule.ui.theme.selecttime.SelectTimeScreen
import androidx.compose.material3.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.ui.res.painterResource
import com.example.timecapsule.ui.theme.LightBlue

@Composable
fun MainNavGraph() {
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
      startDestination = Screen.Home.route,
      modifier = Modifier.padding(bottom = paddingValues.calculateBottomPadding())
    ) {

      // Main Flow with Bottom Nav
      composable(Screen.Home.route) {
        CapsuleCardListScreen(navController) {
          navController.navigate(Screen.AddCapsuleScreens.route)
        }
      }
      composable(Screen.Location.route) { FindCapsuleScreenV1(navController) }
      composable(Screen.Notification.route) { NotificationScreen(navController) }
      composable(Screen.Profile.route) { ProfileScreen(navController) }

      composable(Screen.AddCapsuleScreens.route) { AddCapsuleNavGraph() }


      // Subscreens in Home (no Bottom Nav)
//      composable(Screen.SelectTime.route) { SelectTimeScreen(navController) }
//      composable(Screen.SelectLocation.route) { SelectLocationScreen(navController) }
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
    NavItem(Screen.Home, R.drawable.ic_home, R.drawable.ic_outline_home, "home"),
    NavItem(
      Screen.Location,
      R.drawable.ic_location_outlined,
      R.drawable.ic_location_search,
      "location"
    ),
    NavItem(
      Screen.Notification,
      R.drawable.ic_notification,
      R.drawable.ic_outline_notifications,
      "notification"
    ),
    NavItem(Screen.Profile, R.drawable.ic_person, R.drawable.ic_outline_person, "profile"),
  )
  BottomNavigation(
    backgroundColor = MaterialTheme.colorScheme.primary,
    modifier = Modifier.background(MaterialTheme.colorScheme.primary)
  ) {
    val currentRoute = navController.currentDestination?.route
    items.forEach { item ->
      BottomNavigationItem(
        icon = {
          val icon: Int =
            if (currentRoute == item.screen.route)
              item.selectedIcon
            else
              item.icon
          Icon(
            painter = painterResource(id = icon),
            contentDescription = null
          )
        },
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

data class NavItem(
  val screen: Screen,
  val selectedIcon: Int,
  val icon: Int,
  val contentDescription: String
)

