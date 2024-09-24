package com.example.timecapsule.routes

sealed class Screen(val route: String) {
  // Auth Flow
  object Splash : Screen("splash")
  object Onboarding : Screen("onboarding")
  object Login : Screen("login")
  object Signup : Screen("signup")

  //Main Screen

  object MainScreens : Screen("main_screens")


  // Main Flow with Bottom Nav
  object Home : Screen("home")
  object Location : Screen("location")
  object Notification : Screen("notification")
  object Profile : Screen("profile")
  object CapsuleDetails : Screen("capsule_Details/{id}") {
    fun createRoute(id: String) = "capsule_Details/${id}"
  }


  object AddCapsuleScreens : Screen("add_Capsule_screns")


  // Subscreens in Home
  object SelectTime : Screen("select_time")
  object SelectLocation : Screen("select_location")
  object UploadContent : Screen("upload_content")
  object ReviewContent : Screen("review_content")
  object ShareWithPeopleOptions : Screen("share_with_people_options")
  object ShareWithPeople : Screen("share_with_people")
  object ChooseCapsuleModel : Screen("choose_capsule_model")
  object LocationSelectionOptions : Screen("location_selection_options")


  // Subscreens in Profile
  object Settings : Screen("settings")
  object Help : Screen("help")
}
