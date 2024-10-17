package com.example.timecapsule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.timecapsule.ui.ar.ArScreen
import com.example.timecapsule.ui.capsulecreationconfirmation.CapsuleCreationConfirmation
import com.example.timecapsule.ui.selectcapsule.ViewCapsule
import com.example.timecapsule.ui.theme.TimeCapsuleTheme
import com.mapbox.common.MapboxOptions
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MapboxOptions.accessToken = BuildConfig.MAP_BOX_PUBLIC_ACCESS_TOKEN
      TimeCapsuleTheme {
//          OnboardingScreen(modifier = Modifier.padding(innerPadding))
//          SignUpScreen(modifier = Modifier.padding(innerPadding))
//          SignUpDetailsScreen(modifier = Modifier.padding(innerPadding))
//          LogInScreen(modifier = Modifier.padding(innerPadding))
//          CapsuleCardListScreen()
//        Scaffold { innerPadding->
//          SelectTime(Modifier.padding(innerPadding))
//        SelectTimeScreen()
//        ShareOptionScreen()
//        ShareScreen()
//        SelectLocationOptionScreen()
//        SelectCapsuleScreen()
//        ViewCapsule()
//        UploadFilesScreen()
//        FindCapsuleScreenV1()
//        ReviewScreen()
//        CapsuleDetailsScreen()
//        ArScreen()
//        ProfileScreen()
//        NotificationScreen()
//        LetterScreen()
//        OnBoardingNavGraph()
//        NavGraph()
        CapsuleCreationConfirmation()
      }
    }
  }
}

