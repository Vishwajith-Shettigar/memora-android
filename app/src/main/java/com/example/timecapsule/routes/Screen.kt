package com.example.timecapsule.routes

import android.net.Uri
import kotlin.math.cos

sealed class Screen(val route: String) {
  // Auth Flow
  object Splash : Screen("splash")
  object Onboarding : Screen("onboarding")
  object Login : Screen("login")
  object Signup : Screen("signup")
  object AskDetails : Screen("askdetails")

  // Main Flow with Bottom Nav
  object Home : Screen("home")
  object NearByCapsules : Screen("Nearby_Capsules")
  object Notification : Screen("notification")
  object Profile : Screen("profile")
  object CapsuleDetails : Screen("capsule_Details/{id}") {
    fun createRoute(id: String) = "capsule_Details/${id}"
  }

  object ViewProfile :
    Screen("view_profile/{username}/{firstName}/{lastName}/{aboutMe}/{profileImageUrl}/{coverImageUrl}") {
    fun createRoute(
      username: String,
      firstName: String,
      lastName: String,
      aboutMe: String,
      profileImageUrl: String,
      coverImageUrl: String
    ) =
      "view_profile/${username}/${firstName}/${lastName}/${aboutMe}/${Uri.encode(profileImageUrl)}/${
        Uri.encode(
          coverImageUrl
        )
      }"
  }

  // Sub screens in profile
  object Setting : Screen("setting")
  object ContactUs : Screen("contact_us")
  object Privacy : Screen("privacy")

  object PrivacyPolicy : Screen("privacy_policy")
  object TermsAndServices : Screen("terms_and_services")

  // Settings options screen
  object ChangePasswordScreen : Screen("change_password_screen")
  object ChangeLanguageScreen : Screen("change_language_screen")
  object RateAppScreen : Screen("rate_app_screen")
  object UpdatesScreen : Screen("updates_screen")


  // Parent navigations.
  object MainScreens : Screen("main_screns")
  object OnboardingScreens : Screen("onboardingscrens")
  object AddCapsuleScreens : Screen("add_capsule_screns")
  object OpenCapsuleScreens : Screen("open_capsule_screens")

  // Add capsule screens
  object SelectTime : Screen("select_time")
  object SelectLocation : Screen("select_location")
  object UploadContent : Screen("upload_content")
  object ReviewContent : Screen("review_content")
  object ShareWithPeopleOptions : Screen("share_with_people_options")
  object ShareWithPeople : Screen("share_with_people")
  object ChooseCapsuleModel : Screen("choose_capsule_model")
  object ViewCapsuleModel :
    Screen("view_capsule_model/{capsuleId}/{capsuleName}/{description}/{isPaid}/{storage}/{cost}") {
    fun createRoute(
      capsuleId: String,
      capsuleName: String,
      description: String,
      isPaid: Boolean,
      storage: Int,
      cost: Int
    ) =
      "view_capsule_model/${capsuleId}/${capsuleName}/${description}/${isPaid}/${storage}/${cost}"
  }

  object LocationSelectionOptions : Screen("location_selection_options")
  object CapsuleNameAndDescriptionScreen : Screen("capsule_name_description")
  object LetterScreen : Screen("write_letter")
  object CapsuleCreationSavingScreen : Screen("capsule_creation_saving")


  // Open capsule screens.
  object OpenCapsuleLoadingScreen : Screen("open_capsule_loading/{id}/{isCapsuleHunt}") {
    fun createRoute(id: String, isCapsuleHunt: Boolean) =
      "open_capsule_loading/${id}/${isCapsuleHunt}"
  }

  object OpenCapsuleInstructionsScreen : Screen("open_capsule_instructions")
  object OpenCapsuleMapInstructionsScreen : Screen("open_capsule_map_instructions")
  object OpenCapsuleLetterScreen : Screen("open_capsule_letter/{isCapsuleHunt}") {
    fun createRoute(isCapsuleHunt: Boolean) =
      "open_capsule_letter/${isCapsuleHunt}"
  }

  object OpenCapsuleFindCapsuleScreen : Screen("open_capsule_find_capsule_screen")
  object OpenCapsuleContentScreen : Screen("open_capsule_content")
}
