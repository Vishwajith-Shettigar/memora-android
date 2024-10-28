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
import com.example.timecapsule.R
import com.example.timecapsule.ui.util.DeviceType

@Preview
@Composable
fun ShowLetterScreen() {
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
        Letter()
      }
    }
    Box(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .align(Alignment.BottomCenter)
    ) {
      OpenCapsuleNextButtonRow()
    }
  }
}

@Composable
fun Letter() {
  val loremIpsum =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
      "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
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
