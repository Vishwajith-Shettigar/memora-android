package com.example.timecapsule.routes

sealed class Screen(val route: String) {
  // Auth Flow
  object Splash : Screen("splash")
  object Onboarding : Screen("onboarding")
  object Login : Screen("login")
  object Signup : Screen("signup")

  // Main Flow with Bottom Nav
  object Home : Screen("home")
  object Location : Screen("location")
  object Notification : Screen("notification")
  object Profile : Screen("profile")

  // Subscreens in Home
  object SelectTime : Screen("select_time")
  object SelectLocation : Screen("select_location")
  object UploadContent : Screen("upload_content")
  object ReviewContent : Screen("review_content")

  // Subscreens in Profile
  object Settings : Screen("settings")
  object Help : Screen("help")
}
