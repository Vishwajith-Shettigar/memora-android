package com.example.timecapsule

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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

@Composable
fun AddCapsuleNavGraph() {
  val navController = rememberNavController()
  val activity = (LocalContext.current as? Activity)

  NavHost(navController = navController, startDestination = Screen.SelectTime.route) {

    composable(route = Screen.SelectTime.route) {
      SelectTimeScreen { navigationFlow ->
        when (navigationFlow) {
          NavigationAddCapsule.BACK -> {
            // Todo: Deprecated, replace with alternative.
            activity?.onBackPressed()
          }

          NavigationAddCapsule.NEXT -> {
            navController.navigate(Screen.ShareWithPeopleOptions.route) {
              popUpTo(route = Screen.SelectTime.route) {
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
        when (navigationFlow) {
          NavigationAddCapsule.BACK -> {
            navController.navigate(Screen.ShareWithPeopleOptions.route) {
              popUpTo(route = Screen.ShareWithPeople.route) {
                inclusive = true
                saveState = true
              }
              restoreState = true
              launchSingleTop = true
            }
          }

          NavigationAddCapsule.NEXT -> {
            navController.navigate(Screen.LocationSelectionOptions.route) {
              popUpTo(route = Screen.ShareWithPeople.route) {
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
        when (navigationFlow) {
          NavigationAddCapsule.BACK -> {
            navController.navigate(Screen.LocationSelectionOptions.route) {
              popUpTo(route = Screen.SelectLocation.route) {
                inclusive = true
                saveState = true
              }
              restoreState = true
              launchSingleTop = true
            }
          }

          NavigationAddCapsule.NEXT -> {
            navController.navigate(Screen.ChooseCapsuleModel.route) {
              popUpTo(route = Screen.SelectLocation.route) {
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
    composable(route = Screen.ChooseCapsuleModel.route) {
      SelectCapsuleScreen() { navigationFlow ->

        when (navigationFlow) {
          NavigationAddCapsule.BACK -> {
            // Todo: Decide previous route based on LocationSelectionOptions selection
            val previousRoute = Screen.SelectLocation.route
            navController.navigate(previousRoute) {
              popUpTo(route = Screen.ChooseCapsuleModel.route) {
                inclusive = true
                saveState = true
              }
              restoreState = true
              launchSingleTop = true
            }
          }

          NavigationAddCapsule.NEXT -> {
            navController.navigate(Screen.UploadContent.route) {
              popUpTo(route = Screen.ChooseCapsuleModel.route) {
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
    composable(route = Screen.UploadContent.route) {
      UploadFilesScreen() { navigationFlow ->
        when (navigationFlow) {
          NavigationAddCapsule.BACK -> {
            navController.navigate(Screen.ChooseCapsuleModel.route) {
              popUpTo(route = Screen.UploadContent.route) {
                inclusive = true
                saveState = true
              }
              restoreState = true
              launchSingleTop = true
            }
          }

          NavigationAddCapsule.NEXT -> {
            navController.navigate(Screen.ReviewContent.route) {
              popUpTo(route = Screen.UploadContent.route) {
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
    composable(route = Screen.ReviewContent.route) {
      ReviewScreen() { navigationFlow ->
        when (navigationFlow) {
          NavigationAddCapsule.BACK -> {
            navController.navigate(Screen.UploadContent.route) {
              popUpTo(route = Screen.ReviewContent.route) {
                inclusive = true
                saveState = true
              }
              restoreState = true
              launchSingleTop = true
            }
          }

          NavigationAddCapsule.NEXT -> {
            activity?.onBackPressed()
          }
        }
      }
    }
  }
}
