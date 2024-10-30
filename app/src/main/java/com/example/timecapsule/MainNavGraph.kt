package com.example.timecapsule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.capsulelist.CapsuleCardListScreen
import com.example.timecapsule.ui.findcapsule.FindCapsuleScreenV1
import com.example.timecapsule.ui.notification.NotificationScreen
import com.example.timecapsule.ui.profile.ProfileScreen
import androidx.compose.material3.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.example.timecapsule.ui.capsuledetails.CapsuleDetailsScreen

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
    modifier = Modifier
        .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
        .background(MaterialTheme.colorScheme.primary)
  ) {
    val currentRoute = navController.currentDestination?.route
    items.forEach { item ->
      BottomNavigationItem(
        modifier = Modifier.align(Alignment.Top),
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
    route = Screen.MainScreens.route
  ) {
    composable(Screen.Home.route) {
      CapsuleCardListScreen(navController, addCapsuleBtnClicked = {
        navController.navigate(Screen.AddCapsuleScreens.route) // Start AddCapsule flow
      }, onCapsuleClicked = { id ->
        navController.navigate(Screen.CapsuleDetails.createRoute(id)) // Capsule details
      }, openCapule = { capsuleId ->
        navController.navigate(Screen.OpenCapsuleLoadingScreen.createRoute(capsuleId))
      })
    }
    composable(Screen.Location.route) { FindCapsuleScreenV1(navController) }
    composable(Screen.Notification.route) { NotificationScreen(navController) }
    composable(Screen.Profile.route) { ProfileScreen(navController) }

    // Capsule Details Screen (inside Main flow)
    composable(Screen.CapsuleDetails.route) { navBackStackEntry ->
      val capsuleId = navBackStackEntry.arguments?.getString("id")
      if (capsuleId != null) {
        CapsuleDetailsScreen(capsuleId) {
          navController.popBackStack() // Return to previous screen
        }
      }
    }
  }
}
