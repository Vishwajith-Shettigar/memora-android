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
import androidx.compose.ui.zIndex
import com.example.timecapsule.ui.theme.Inter
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.overSeer
import com.example.timecapsule.ui.util.DeviceType

@Composable
fun SlideTwo() {

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
        Modifier
            .then(
                if (isTablet) Modifier.width(600.dp) else
                    Modifier.fillMaxWidth()
            )
            .fillMaxHeight(),

      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = "Share the capsule \nwith friends, family, or \nthe entire world.",
        style = MaterialTheme.typography.titleLarge.copy(
          color = Color.Black,
          fontSize = 35.sp,
          fontWeight = FontWeight.SemiBold,
          fontFamily = FontFamily.SansSerif,
          lineHeight = TextUnit(50.0F, TextUnitType.Sp)
        ),
        modifier = Modifier
            .padding(top = 50.dp)
            .background(Color.Transparent)
            .zIndex(3.0F),
      )

      Image(
        modifier = Modifier.size(500.dp),
        painter = painterResource(id = com.example.timecapsule.R.drawable.slide_two_graphic),
        contentDescription = "Logo",
      )
    }
  }
}
