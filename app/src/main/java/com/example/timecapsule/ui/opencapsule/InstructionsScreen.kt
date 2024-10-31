package com.example.timecapsule.ui.opencapsule

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.model.CapsuleDetails
import com.example.timecapsule.ui.theme.SubTitleFontColor
import com.example.timecapsule.R
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.viewmodel.DisplayCapsuleDetailsState
import com.example.timecapsule.viewmodel.OpenCapsuleViewModel

@Preview
@Composable
fun InstructionsScreen(
  viewModel: OpenCapsuleViewModel = hiltViewModel(),
  navigate: (String) -> Unit = {}
) {

  val capsuleDetailsState by viewModel.capsuleDetailsState.collectAsState()

  val isLetterScreen by remember {
    mutableStateOf(false)
  }

  var isLocationScreen by remember {
    mutableStateOf(false)
  }

  LaunchedEffect(key1 = capsuleDetailsState) {
    if (capsuleDetailsState is DisplayCapsuleDetailsState.Success) {
      if ((capsuleDetailsState as DisplayCapsuleDetailsState.Success).calsuleDetails.location != null) {
        isLocationScreen = true
      }
    } else {
      Unit
    }
  }

  LaunchedEffect(Unit) {
    viewModel.saveScreenCheckPoint(Screen.OpenCapsuleInstructionsScreen.route)
  }
  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
  ) {
    Column(
      modifier = Modifier
          .align(Alignment.TopCenter)
          .fillMaxSize(),
      verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Hi! We will help you open your time capsule. There’s also a letter included," +
          " which you can read on the next screen. ",
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
          .padding(8.dp)
      )
      Image(
        modifier = Modifier.size(400.dp),
        painter = painterResource(id = R.drawable.capsuleimage),
        contentDescription = "capsule imahge"
      )
    }
    Box(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .align(Alignment.BottomCenter)
    ) {
      OpenCapsuleNextButtonRow(isLocationScreen,navigate)
    }
  }
}

@Composable
fun OpenCapsuleNextButtonRow(isLocationScreen: Boolean=false, navigate: (String) -> Unit = {}) {
  Row(
      Modifier
          .fillMaxWidth()
          .padding(bottom = 30.dp, end = 10.dp),
    horizontalArrangement = Arrangement.Absolute.Right,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "Please press next to move forward.",
      color = SubTitleFontColor,
      modifier = Modifier.padding(horizontal = 10.dp)
    )
    Button(onClick = {
      if (isLocationScreen) {
        navigate(Screen.OpenCapsuleFindCapsuleScreen.route)
      }
    }) {
      Text(text = "next")
    }
  }
}
