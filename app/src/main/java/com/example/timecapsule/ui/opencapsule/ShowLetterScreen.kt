package com.example.timecapsule.ui.opencapsule

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.timecapsule.R
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.viewmodel.DisplayCapsuleDetailsState
import com.example.timecapsule.viewmodel.OpenCapsuleViewModel

@Preview
@Composable
fun ShowLetterScreen(
  isCapsuleHunt: Boolean = false,
  viewModel: OpenCapsuleViewModel = hiltViewModel(),
  onNavigate: (String) -> Unit = {}
) {
  val capsuleDetailsState by viewModel.capsuleDetailsState.collectAsState()

  val capsuleDetails by remember()
  {
    mutableStateOf((capsuleDetailsState as DisplayCapsuleDetailsState.Success).capsuleDetails)
  }

  val isLocationScreen by remember {
    mutableStateOf(capsuleDetails.location != null)
  }

  LaunchedEffect(Unit) {
    viewModel.saveScreenCheckPoint(Screen.OpenCapsuleLetterScreen.route)
  }

  Box(modifier = Modifier.fillMaxSize()) {
    Column(
        Modifier
            .fillMaxSize()
            .align(Alignment.CenterStart),
      horizontalAlignment = Alignment.Start,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = "We have found a letter for you!",
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
          .padding(horizontal = 8.dp, vertical = 20.dp)
      )
      Column(
          Modifier
              .fillMaxWidth()
              .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Letter(capsuleDetails.letter!!)
      }
    }
    Box(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .align(Alignment.BottomCenter)
    ) {
      OpenCapsuleNextButtonRow(
        isLocationScreen = isLocationScreen,
        isCapsuleHunt = isCapsuleHunt,
        navigate = onNavigate
      )
    }
  }
}

@Composable
fun Letter(letterText: String) {
  val loremIpsum =
    letterText
  val isTablet = DeviceType.isTablet()
  val fontSize = 20.sp
  val maxHeight = 500.dp
  val lineHeight = 28.sp

  val boxModifier = if (isTablet) {
      Modifier
          .width(500.dp)
          .height(500.dp)
  } else {
      Modifier
          .fillMaxWidth(0.9f)
          .height(500.dp)
  }

  val letterImage: Painter = painterResource(id = R.drawable.letterimage)
  Box(
    modifier = boxModifier,
  ) {
    Text(
      style = TextStyle(
        color = Color.Black,
        fontSize = fontSize,
        lineHeight = lineHeight,
        fontFamily = FontFamily.Cursive
      ),
      text = loremIpsum,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      fontWeight = FontWeight.Bold,
      modifier = Modifier
          .align(Alignment.TopStart)
          .fillMaxSize()
          .padding(16.dp)
          .height(maxHeight)
          .zIndex(10F),
    )
    Image(
      painter = letterImage,
      contentDescription = "Letter Background",
      modifier = Modifier
          .fillMaxSize()
          .align(Alignment.Center),
      contentScale = ContentScale.Crop
    )
  }
}
