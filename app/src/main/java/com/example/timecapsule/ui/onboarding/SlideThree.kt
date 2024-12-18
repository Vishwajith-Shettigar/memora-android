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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.example.timecapsule.ui.theme.cardOrange
import com.example.timecapsule.ui.theme.model1Color
import com.example.timecapsule.ui.theme.model3Color
import com.example.timecapsule.ui.theme.overSeer
import com.example.timecapsule.ui.util.DeviceType

@Composable
fun SlideThree() {

  val isTablet = DeviceType.isTablet()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .background(cardOrange)
      .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.Top,
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
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      item {

        Text(
          text = "Immersive AR Experience",
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
          text = "Experience your capsule as if it's physically present, with stunning AR realism.",
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
          modifier = Modifier.size(300.dp),
          painter = painterResource(id = com.example.timecapsule.R.drawable.slide_three_graphic),
          contentDescription = "Logo",
        )

        Button(
          modifier = Modifier
            .padding(vertical = 40.dp)
            .border(3.dp, color = Color.Black, shape = RoundedCornerShape(50.dp)),
          shape = RoundedCornerShape(50.dp),
          onClick = {},
          colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
        ) {
          Text(
            text = "Lets Start",
            style = MaterialTheme.typography.titleLarge.copy(
              color = Color.Black,
              fontSize = 20.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.SansSerif,
            ),
          )
        }
      }
    }
  }
}
