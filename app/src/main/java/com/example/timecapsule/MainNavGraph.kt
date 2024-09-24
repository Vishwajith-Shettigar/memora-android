package com.example.timecapsule

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.capsuledetails.CapsuleDetailsScreen
import com.example.timecapsule.ui.theme.util.DeviceType

fun getNavigationItems(): List<NavItem> {
  return listOf(
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
}

@Composable
fun NavigationRail(navController: NavController) {
  val items = getNavigationItems()
  NavigationRail(modifier = Modifier.fillMaxHeight()) {
    val currentRoute = navController.currentDestination?.route
    Spacer(Modifier.weight(1f))
    items.forEach { item ->
      NavigationRailItem(
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
            popUpTo(Screen.Home.route) { saveState = true }
            launchSingleTop = true
            restoreState = true
          }
        }
      )
    }
    Spacer(Modifier.weight(1f))
  }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
  val items = getNavigationItems()
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
            popUpTo(Screen.Home.route) { saveState = true }
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

// Helper method for Main Flow (Mobile or Tablet layouts)
fun NavGraphBuilder.mainNavGraph(navController: NavController) {
  navigation(
    startDestination = Screen.Home.route,
    route = Screen.MainScreens.route // Optional route for separation
  ) {
    composable(Screen.Home.route) {
      CapsuleCardListScreen(navController, addCapsuleBtnClicked = {
        navController.navigate(Screen.AddCapsuleScreens.route) // Start AddCapsule flow
      }, onCapsuleClicked = { id ->
        navController.navigate(Screen.CapsuleDetails.createRoute(id)) // Capsule details
      })
    }
    composable(Screen.Location.route) { FindCapsuleScreenV1(navController) }
    composable(Screen.Notification.route) { NotificationScreen(navController) }
    composable(Screen.Profile.route) { ProfileScreen(navController) }

    // Capsule Details Screen (inside Main flow)
    composable(Screen.CapsuleDetails.route) { navBackStackEntry ->
      val id = navBackStackEntry.arguments?.getString("id")
      CapsuleDetailsScreen {
        navController.popBackStack() // Return to previous screen
      }
    }
  }
}
