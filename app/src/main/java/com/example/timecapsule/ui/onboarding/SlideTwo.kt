package com.example.timecapsule.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.timecapsule.ui.theme.cardYellow
import com.example.timecapsule.util.DeviceType

@Composable
fun SlideTwo() {

  val isTablet = DeviceType.isTablet()

  Column(
    modifier = Modifier
        .fillMaxSize()
        .background(cardYellow)
        .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    LazyColumn(
        Modifier
            .then(
                if (isTablet) Modifier.width(600.dp) else
                    Modifier.fillMaxWidth()
            )
            .fillMaxHeight(),

      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.Start
    ) {
      item {
        Text(
          text = "Share Your Capsule",
          style = MaterialTheme.typography.titleLarge.copy(
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            lineHeight = TextUnit(36.0F, TextUnitType.Sp)
          ),
          modifier = Modifier
              .padding(top = 20.dp)
              .background(Color.Transparent)
              .zIndex(3.0F)
        )

        Text(
          text = "Invite friends, family, or even the entire world to discover your capsule.",
          style = MaterialTheme.typography.titleMedium.copy(
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            fontFamily = FontFamily.SansSerif,
            lineHeight = TextUnit(28.0F, TextUnitType.Sp)
          ),
          modifier = Modifier
              .padding(top = 8.dp)
              .background(Color.Transparent)
              .zIndex(3.0F)
        )

        Image(
          modifier = Modifier.size(500.dp),
          painter = painterResource(id = com.example.timecapsule.R.drawable.slide_two_graphic),
          contentDescription = "Logo",
        )
      }
    }
  }
}
