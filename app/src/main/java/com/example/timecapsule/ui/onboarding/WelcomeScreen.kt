package com.example.timecapsule.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.util.DeviceType

@Composable
fun WelcomeScreen() {

  val isTablet = DeviceType.isTablet()

  Column(
    modifier = Modifier
        .fillMaxSize()
        .background(LightBlue.copy(alpha = 0.8F))
        .padding(20.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Column(
      Modifier.then(
        if (isTablet) Modifier.width(600.dp) else
          Modifier.fillMaxWidth()
      )
    ) {
      Text(
        text = "Relive your moments with Time Capsule",
        style = MaterialTheme.typography.titleLarge.copy(
          color = Color.Black,
          fontSize = 50.sp,
          fontWeight = FontWeight.ExtraBold,
          fontFamily = FontFamily.SansSerif,
          lineHeight = TextUnit(50.0F, TextUnitType.Sp)
        ),
        modifier = Modifier.padding(top = 50.dp),
      )
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
        Image(
          modifier = Modifier.size(60.dp),
          painter = painterResource(id = com.example.timecapsule.R.drawable.onboarding_image),
          contentDescription = "Logo",
          contentScale = ContentScale.Crop
        )
      }

      OutlinedButton(
        modifier = Modifier
            .padding(vertical = 40.dp)
            .border(2.dp, color = Color.Black, shape = RoundedCornerShape(50.dp)),
        shape = RoundedCornerShape(50.dp),
        onClick = {},
      ) {
        Text(
          text = "Explore",
          style = MaterialTheme.typography.titleLarge.copy(
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
          ),
        )
      }
    }
  }
}
