package com.example.timecapsule

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.model.CapsuleAsset
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.CapsuleCreationSaving.CapsuleCreationSavingScreen
import com.example.timecapsule.ui.review.ReviewScreen
import com.example.timecapsule.ui.review.SharedPeople
import com.example.timecapsule.ui.selectcapsule.SelectCapsuleScreen
import com.example.timecapsule.ui.selectcapsule.ViewCapsule
import com.example.timecapsule.ui.selectlocation.LocationOptions
import com.example.timecapsule.ui.selectlocation.SelectLocationOptionScreen
import com.example.timecapsule.ui.selectlocation.SelectLocationScreen
import com.example.timecapsule.ui.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.selecttime.SelectTimeScreen
import com.example.timecapsule.ui.sharewithpeople.SearchPeople
import com.example.timecapsule.ui.sharewithpeople.ShareOptionScreen
import com.example.timecapsule.ui.sharewithpeople.SharePeopleOptions
import com.example.timecapsule.ui.sharewithpeople.ShareScreen
import com.example.timecapsule.ui.uploadfiles.UploadFilesScreen
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel
import com.example.timecapsule.viewmodel.LocationOption
import com.example.timecapsule.viewmodel.ShareWithPeopleOption
import com.google.firebase.Timestamp

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
  navController: NavController,
): T {
  val navGraphRoute = destination.parent?.route ?: return viewModel()
  val parentEntry = remember(this) {
    navController.getBackStackEntry(navGraphRoute)
  }
  return hiltViewModel(parentEntry)
}

// Helper method for Add Capsule Flow
fun NavGraphBuilder.addCapsuleNavGraph(navController: NavController, activity: Activity) {

  navigation(
    startDestination = Screen.SelectTime.route,
    route = Screen.AddCapsuleScreens.route // Separate route for Add Capsule
  ) {

    composable(route = Screen.SelectTime.route) { backstackentry ->
      val sharedViewModel =
        backstackentry.sharedViewModel<CapsuleCreationViewModel>(navController = navController)

      SelectTimeScreen(viewModel = sharedViewModel) { navigationFlow ->
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

    composable(route = Screen.ShareWithPeopleOptions.route) { backstackentry ->

      val sharedViewModel =
        backstackentry.sharedViewModel<CapsuleCreationViewModel>(navController = navController)
      ShareOptionScreen(sharedViewModel) { navigationFlow, sharePeopleOption ->
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
    composable(route = Screen.ShareWithPeople.route) { backstackentry ->
      val sharedViewModel =
        backstackentry.sharedViewModel<CapsuleCreationViewModel>(navController = navController)

      ShareScreen(sharedViewModel) { navigationFlow ->
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
    composable(route = Screen.LocationSelectionOptions.route) { backstackentry ->
      val sharedViewModel =
        backstackentry.sharedViewModel<CapsuleCreationViewModel>(navController = navController)
      SelectLocationOptionScreen(sharedViewModel) { navigationFlow, selectionOption ->
        when (navigationFlow) {
          NavigationAddCapsule.BACK -> {
            var previousRoute =
              Screen.ShareWithPeopleOptions.route

            // set previous route ShareWithPeople if user shared with selected peoples.
            if (sharedViewModel.shareWithPeopleOption == ShareWithPeopleOption.SELECTED_PEOPLES)
              previousRoute = Screen.ShareWithPeople.route

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
    composable(route = Screen.SelectLocation.route) { backstackentry ->
      val sharedViewModel =
        backstackentry.sharedViewModel<CapsuleCreationViewModel>(navController = navController)
      SelectLocationScreen(sharedViewModel) { navigationFlow ->
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
    composable(route = Screen.ChooseCapsuleModel.route) { backstackentry ->
      val sharedViewModel =
        backstackentry.sharedViewModel<CapsuleCreationViewModel>(navController = navController)
      var previousRoute: Screen = Screen.SelectLocation
      if (sharedViewModel.selectedLocationOption == LocationOption.DONT_SELECT_LOCATION)
        previousRoute = Screen.LocationSelectionOptions

      SelectCapsuleScreen(viewModel = sharedViewModel, onViewCapsuleClick = { capsuleAsset ->

        navController.navigate(
          Screen.ViewCapsuleModel.createRoute(
            capsuleId = capsuleAsset.capsule_id,
            capsuleName = capsuleAsset.capsuleName,
            description = capsuleAsset.description,
            isPaid = capsuleAsset.isPaid,
            storage = capsuleAsset.storage.toInt(),
            cost = capsuleAsset.cost.toInt()
          )
        )
      }, onNavigate = { navigationFlow ->
        handleNavigation(
          activity = activity,
          navController = navController,
          navigationFlow = navigationFlow,
          navigateToScreenRouteBack = previousRoute,
          navigateToScreenRouteNext = Screen.UploadContent,
          popScreenRoute = Screen.ChooseCapsuleModel
        )
      })
    }

    composable(route = Screen.ViewCapsuleModel.route) { backstackentry ->
      val capsuleAsset = CapsuleAsset(
        capsule_id = backstackentry.arguments?.getString("capsuleId")!!,
        capsuleName = backstackentry.arguments?.getString("capsuleName")!!,
        description = backstackentry.arguments?.getString("description")!!,
        isPaid = backstackentry.arguments?.getString("isPaid")!!.toBoolean(),
        storage = backstackentry.arguments?.getString("storage")!!.toInt(),
        imageUrl = "",
        cost = backstackentry.arguments?.getString("cost")!!.toInt()
      )
      ViewCapsule(capsuleAsset) {
        navController.popBackStack()
      }
    }

    composable(route = Screen.UploadContent.route) { backstackentry ->
      val sharedViewModel =
        backstackentry.sharedViewModel<CapsuleCreationViewModel>(navController = navController)
      UploadFilesScreen(sharedViewModel) { navigationFlow ->
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
    composable(route = Screen.ReviewContent.route) {backstackentry->
      val sharedViewModel =
        backstackentry.sharedViewModel<CapsuleCreationViewModel>(navController = navController)
      ReviewScreen(sharedViewModel) { navigationFlow ->
        handleNavigation(
          activity = activity,
          navController = navController,
          navigationFlow = navigationFlow,
          navigateToScreenRouteBack = Screen.UploadContent,
          navigateToScreenRouteNext = Screen.CapsuleCreationSavingScreen,
          popScreenRoute = Screen.ReviewContent
        )
      }
    }

    composable(route = Screen.CapsuleCreationSavingScreen.route) {backstackentry->
      val sharedViewModel =
        backstackentry.sharedViewModel<CapsuleCreationViewModel>(navController = navController)
      CapsuleCreationSavingScreen(sharedViewModel) { navigationFlow ->
       activity.onBackPressed()
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
        navController.popBackStack(Screen.AddCapsuleScreens.route, inclusive = true)
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
