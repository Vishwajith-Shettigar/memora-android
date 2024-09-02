package com.example.timecapsule

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.timecapsule.ui.theme.TimeCapsuleTheme
import com.example.timecapsule.ui.theme.selectcapsule.SelectCapsuleScreen
import com.example.timecapsule.ui.theme.selectcapsule.ViewCapsule
import com.example.timecapsule.ui.theme.selectlocation.SelectLocationOptionScreen
import com.example.timecapsule.ui.theme.selectlocation.SelectLocationScreen
import com.example.timecapsule.ui.theme.sharewithpeople.ShareOptionScreen
import com.example.timecapsule.ui.theme.sharewithpeople.ShareScreen

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
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
        ViewCapsule()
      }

    }
  }
}

