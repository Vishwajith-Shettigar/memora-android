package com.example.timecapsule.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
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
import com.example.timecapsule.ui.theme.Inter
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.cardViolet
import com.example.timecapsule.ui.theme.model1Color
import com.example.timecapsule.ui.theme.overSeer
import com.example.timecapsule.ui.util.DeviceType

@Composable
fun SlideOne() {

  val isTablet = DeviceType.isTablet()

  Column(
    modifier = Modifier
        .fillMaxSize()
        .background(cardViolet)
        .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Column(
        Modifier
            .then(
                if (isTablet) Modifier.width(600.dp) else
                    Modifier.fillMaxWidth()
            )
            .fillMaxHeight(),

      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        text = "Hide Your Capsule at Any Place",
        style = MaterialTheme.typography.titleLarge.copy(
          color = Color.White,
          fontSize = 32.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.SansSerif,
          lineHeight = TextUnit(36.0F, TextUnitType.Sp)
        ),
        modifier = Modifier.padding(top = 20.dp)
      )

      Text(
        text = "Place it anywhere on Earth, and no one will find it until time runs out.",
        style = MaterialTheme.typography.titleMedium.copy(
          color = Color.White,
          fontSize = 20.sp,
          fontWeight = FontWeight.Light,
          fontFamily = FontFamily.SansSerif,
          lineHeight = TextUnit(28.0F, TextUnitType.Sp)
        ),
        modifier = Modifier.padding(top = 8.dp)
      )
      Image(
        modifier = Modifier.size(350.dp),
        painter = painterResource(id = com.example.timecapsule.R.drawable.slide_one_graphic),
        contentDescription = "Logo",
        contentScale = ContentScale.Crop
      )
    }
  }
}
