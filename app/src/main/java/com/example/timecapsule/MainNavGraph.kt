package com.example.timecapsule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.composable
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.capsulelist.CapsuleCardListScreen
import com.example.timecapsule.ui.notification.NotificationScreen
import com.example.timecapsule.ui.profile.ProfileScreen
import androidx.compose.material3.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.navigation
import com.example.model.Profile
import com.example.timecapsule.ui.capsuledetails.CapsuleDetailsScreen
import com.example.timecapsule.ui.nearbycapsules.NearbyCapsulesScreen
import com.example.timecapsule.ui.setting.SettingScreen
import com.example.timecapsule.ui.setting.options.ChangeLanguageScreen
import com.example.timecapsule.ui.setting.options.ChangePasswordScreen
import com.example.timecapsule.ui.setting.options.ContactUsScreen
import com.example.timecapsule.ui.setting.options.PrivacyPolicyScreen
import com.example.timecapsule.ui.setting.options.PrivacyScreen
import com.example.timecapsule.ui.setting.options.RateUsScreen
import com.example.timecapsule.ui.setting.options.TermsAndServiceScreen
import com.example.timecapsule.ui.setting.options.UpdateScreen
import com.example.timecapsule.ui.viewprofile.ViewProfileScreen
import com.example.timecapsule.viewmodel.NotificatioViewModel

fun getNavigationItems(): List<NavItem> {
  return listOf(
    NavItem(Screen.Home, R.drawable.ic_home, R.drawable.ic_outline_home, "home"),
    NavItem(
      Screen.NearByCapsules,
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
      com.example.timecapsule.ui.capsulelist.v2.CapsuleCardListScreen(addCapsuleBtnClicked = {
        navController.navigate(Screen.AddCapsuleScreens.route) // Start AddCapsule flow
      }, onCapsuleClicked = { id ->
        navController.navigate(Screen.CapsuleDetails.createRoute(id)) // Capsule details
      }, openCapule = { capsuleId ->
        navController.navigate(Screen.OpenCapsuleLoadingScreen.createRoute(capsuleId, false))
      })
    }
    composable(Screen.NearByCapsules.route) {
      NearbyCapsulesScreen { route ->
        navController.navigate(route)
      }
    }
    composable(Screen.Notification.route) {
      val viewmodel: NotificatioViewModel = hiltViewModel(it)
      NotificationScreen(navController, viewmodel) { capsuleId ->
        navController.navigate(Screen.CapsuleDetails.createRoute(capsuleId))
      }
    }
    composable(Screen.Profile.route) {
      ProfileScreen(onViewProfileClick = { profile ->
        navController.navigate(
          Screen.ViewProfile.createRoute(
            username = profile.username,
            firstName = profile.firstName,
            lastName = profile.lastName,
            aboutMe = profile.aboutMe,
            profileImageUrl = profile.profileImageUrl,
            coverImageUrl = profile.coverImageUrl
          )
        )
      },
        onSettingClick = {
          navController.navigate(Screen.Setting.route)
        },
        onPrivacyClicked = {
          navController.navigate(Screen.Privacy.route)
        },
        onContactUsClicked = {
          navController.navigate(Screen.ContactUs.route)
        })
    }

    composable(Screen.ViewProfile.route) { backStackEntry ->
      val profile = Profile(
        userId = "",
        username = backStackEntry.arguments!!.getString("username")!!,
        firstName = backStackEntry.arguments!!.getString("firstName")!!,
        lastName = backStackEntry.arguments!!.getString("lastName")!!,
        aboutMe = backStackEntry.arguments!!.getString("aboutMe")!!,
        profileImageUrl = (backStackEntry.arguments?.getString("profileImageUrl") ?: ""),
        coverImageUrl = (backStackEntry.arguments?.getString("coverImageUrl") ?: "")
      )
      ViewProfileScreen(profile)
    }

    composable(Screen.Setting.route) { backStackEntry ->
      SettingScreen(onBackClick = {
        navController.popBackStack()
      },
        onChangePasswordClicked = {
          navController.navigate(Screen.ChangePasswordScreen.route)
        },
        onChangeLanguageClicked = {
          navController.navigate(Screen.ChangeLanguageScreen.route)
        },
        onRateAppClicked = {
          navController.navigate(Screen.RateAppScreen.route)
        }, onUpdatesClicked = {
          navController.navigate(Screen.UpdatesScreen.route)
        })
    }

    composable(Screen.Privacy.route) { backStackEntry ->

    }


    composable(Screen.ContactUs.route) { backStackEntry ->
      ContactUsScreen {
        navController.popBackStack()
      }
    }
    composable(Screen.Privacy.route) { backStackEntry ->
      PrivacyScreen(
        onBackClick = { navController.popBackStack() },
        onPrivacyPolicyClicked = { navController.navigate(Screen.PrivacyPolicy.route) },
        onTermsAndServicesClicked = { navController.navigate(Screen.TermsAndServices.route) })
    }

    composable(Screen.PrivacyPolicy.route) { backStackEntry ->
      PrivacyPolicyScreen() {
        navController.popBackStack()
      }
    }

    composable(Screen.TermsAndServices.route) { backStackEntry ->
      TermsAndServiceScreen() {
        navController.popBackStack()
      }
    }

    composable(Screen.ChangePasswordScreen.route) { backStackEntry ->
      ChangePasswordScreen() {
        navController.popBackStack()
      }
    }

    composable(Screen.ChangeLanguageScreen.route) { backStackEntry ->
      ChangeLanguageScreen() {
        navController.popBackStack()
      }
    }

    composable(Screen.RateAppScreen.route) { backStackEntry ->
      RateUsScreen() {
        navController.popBackStack()
      }
    }
    composable(Screen.UpdatesScreen.route) { backStackEntry ->
      UpdateScreen() {
        navController.popBackStack()
      }
    }

    // Capsule Details Screen (inside Main flow)
    composable(Screen.CapsuleDetails.route) { navBackStackEntry ->
      val capsuleId = navBackStackEntry.arguments?.getString("id")
      if (capsuleId != null) {
        CapsuleDetailsScreen(capsuleId) {
          navController.popBackStack()
        }
      }
    }
  }
}
