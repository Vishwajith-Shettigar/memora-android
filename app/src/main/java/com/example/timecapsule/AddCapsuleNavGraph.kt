package com.example.timecapsule

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.theme.review.ReviewScreen
import com.example.timecapsule.ui.theme.review.SharedPeople
import com.example.timecapsule.ui.theme.selectcapsule.SelectCapsuleScreen
import com.example.timecapsule.ui.theme.selectlocation.LocationOptions
import com.example.timecapsule.ui.theme.selectlocation.SelectLocationOptionScreen
import com.example.timecapsule.ui.theme.selectlocation.SelectLocationScreen
import com.example.timecapsule.ui.theme.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.theme.selecttime.SelectTimeScreen
import com.example.timecapsule.ui.theme.sharewithpeople.SearchPeople
import com.example.timecapsule.ui.theme.sharewithpeople.ShareOptionScreen
import com.example.timecapsule.ui.theme.sharewithpeople.SharePeopleOptions
import com.example.timecapsule.ui.theme.sharewithpeople.ShareScreen
import com.example.timecapsule.ui.theme.uploadfiles.UploadFilesScreen

// Helper method for Add Capsule Flow
fun NavGraphBuilder.addCapsuleNavGraph(navController: NavController, activity: Activity) {
  navigation(
    startDestination = Screen.SelectTime.route,
    route = Screen.AddCapsuleScreens.route // Separate route for Add Capsule
  ) {
    composable(route = Screen.SelectTime.route) {
      SelectTimeScreen { navigationFlow ->
        handleNavigation(
          activity = activity,
          navController = navController,
          navigationFlow = navigationFlow,
          navigateToScreenRouteBack = null,
          navigateToScreenRouteNext = Screen.ShareWithPeopleOptions,
          popScreenRoute = Screen.SelectTime
        )
      }
    }

    composable(route = Screen.ShareWithPeopleOptions.route) {
      ShareOptionScreen { navigationFlow, sharePeopleOption ->
        when (navigationFlow) {
          NavigationAddCapsule.BACK -> {
            navController.navigate(Screen.SelectTime.route) {
              popUpTo(route = Screen.ShareWithPeopleOptions.route) {
                inclusive = true
                saveState = true
              }
              restoreState = true
              launchSingleTop = true
            }
          }

          NavigationAddCapsule.NEXT -> {
            if (sharePeopleOption == SharePeopleOptions.SELECTED_PEOPLE) {
              navController.navigate(Screen.ShareWithPeople.route)
              {
                popUpTo(route = Screen.ShareWithPeopleOptions.route) {
                  inclusive = true
                  saveState = true
                }
                restoreState = true
                launchSingleTop = true
              }
            } else {
              navController.navigate(Screen.LocationSelectionOptions.route) {
                popUpTo(route = Screen.ShareWithPeopleOptions.route) {
                  inclusive = true
                  saveState = true
                }
                restoreState = true
                launchSingleTop = true
              }
            }
          }
        }
      }
    }
    composable(route = Screen.ShareWithPeople.route) {
      ShareScreen() { navigationFlow ->
        handleNavigation(
          activity = activity,
          navController = navController,
          navigationFlow = navigationFlow,
          navigateToScreenRouteBack = Screen.ShareWithPeopleOptions,
          navigateToScreenRouteNext = Screen.LocationSelectionOptions,
          popScreenRoute = Screen.ShareWithPeople
        )
      }
    }
    composable(route = Screen.LocationSelectionOptions.route) {
      SelectLocationOptionScreen() { navigationFlow, selectionOption ->
        when (navigationFlow) {
          NavigationAddCapsule.BACK -> {
            // Todo: Decide previous route based on ShareWithPeopleOptions selection
            val previousRoute = Screen.ShareWithPeople.route
            navController.navigate(previousRoute) {
              popUpTo(route = Screen.LocationSelectionOptions.route) {
                inclusive = true
                saveState = true
              }
              restoreState = true
              launchSingleTop = true
            }
          }

          NavigationAddCapsule.NEXT -> {
            if (selectionOption == LocationOptions.SELECTED) {
              navController.navigate(Screen.SelectLocation.route) {
                popUpTo(route = Screen.LocationSelectionOptions.route) {
                  inclusive = true
                  saveState = true
                }
                restoreState = true
                launchSingleTop = true
              }
            } else {
              navController.navigate(Screen.ChooseCapsuleModel.route) {
                popUpTo(route = Screen.LocationSelectionOptions.route) {
                  inclusive = true
                  saveState = true
                }
                restoreState = true
                launchSingleTop = true
              }
            }
          }
        }
      }
    }
    composable(route = Screen.SelectLocation.route) {
      SelectLocationScreen() { navigationFlow ->
        handleNavigation(
          activity = activity,
          navController = navController,
          navigationFlow = navigationFlow,
          navigateToScreenRouteBack = Screen.LocationSelectionOptions,
          navigateToScreenRouteNext = Screen.ChooseCapsuleModel,
          popScreenRoute = Screen.SelectLocation
        )
      }
    }
    composable(route = Screen.ChooseCapsuleModel.route) {
      // Todo: Decide previous route based on LocationSelectionOptions selection
      val previousRoute = Screen.SelectLocation
      SelectCapsuleScreen() { navigationFlow ->

        handleNavigation(
          activity = activity,
          navController = navController,
          navigationFlow = navigationFlow,
          navigateToScreenRouteBack = previousRoute,
          navigateToScreenRouteNext = Screen.UploadContent,
          popScreenRoute = Screen.ChooseCapsuleModel
        )
      }
    }
    composable(route = Screen.UploadContent.route) {
      UploadFilesScreen() { navigationFlow ->

        handleNavigation(
          activity = activity,
          navController = navController,
          navigationFlow = navigationFlow,
          navigateToScreenRouteBack = Screen.ChooseCapsuleModel,
          navigateToScreenRouteNext = Screen.ReviewContent,
          popScreenRoute = Screen.UploadContent
        )
      }
    }
    composable(route = Screen.ReviewContent.route) {
      ReviewScreen { navigationFlow ->
        handleNavigation(
          activity = activity,
          navController = navController,
          navigationFlow = navigationFlow,
          navigateToScreenRouteBack = Screen.UploadContent,
          navigateToScreenRouteNext = null,
          popScreenRoute = Screen.ReviewContent
        )
      }
    }
  }
}

fun handleNavigation(
  activity: Activity,
  navController: NavController,
  navigationFlow: NavigationAddCapsule, navigateToScreenRouteBack: Screen?,
  navigateToScreenRouteNext: Screen?,
  popScreenRoute: Screen
) {
  when (navigationFlow) {
    NavigationAddCapsule.BACK -> {
      if (navigateToScreenRouteBack == null) {
        activity.onBackPressed()
      } else {
        navController.navigate(navigateToScreenRouteBack.route) {
          popUpTo(route = popScreenRoute.route) {
            inclusive = true
            saveState = true
          }
          restoreState = true
          launchSingleTop = true
        }
      }
    }

    NavigationAddCapsule.NEXT -> {
      if (navigateToScreenRouteNext == null) {
        activity.onBackPressed()
      } else {
        navController.navigate(navigateToScreenRouteNext.route) {
          popUpTo(route = popScreenRoute.route) {
            inclusive = true
            saveState = true
          }
          restoreState = true
          launchSingleTop = true
        }
      }
    }
  }
}
