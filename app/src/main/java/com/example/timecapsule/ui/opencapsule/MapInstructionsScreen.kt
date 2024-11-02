package com.example.timecapsule.ui.opencapsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.timecapsule.R
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.viewmodel.OpenCapsuleViewModel

@Preview
@Composable
fun MapInstructionsScreen(
  viewModel: OpenCapsuleViewModel = hiltViewModel(),
  onNavigate: (String) -> Unit = {}
) {
  LaunchedEffect(Unit) {
    viewModel.saveScreenCheckPoint(Screen.OpenCapsuleMapInstructionsScreen.route)
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
        text = "Your capsule is hidden in a specific location. To open this capsule," +
          " you need to visit the location physically. Don't worry, we will guide you through your journey. Happy travels!",
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
          .padding(8.dp)
      )
      Image(
        modifier = Modifier.size(300.dp),
        painter = painterResource(id = R.drawable.mapgraphics),
        contentDescription = "capsule imahge"
      )
    }
    Box(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .align(Alignment.BottomCenter)
    ) {
      OpenCapsuleNextButtonRow(navigateToMap = true, navigate = onNavigate)
    }
  }
}
