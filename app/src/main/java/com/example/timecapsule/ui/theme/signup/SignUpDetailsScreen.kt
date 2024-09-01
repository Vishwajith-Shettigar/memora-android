package com.example.timecapsule.ui.theme.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.DMSerifText
import com.example.timecapsule.ui.theme.SignUpBackground
import com.example.timecapsule.ui.theme.util.DeviceType

@Preview
@Composable
fun SignUpDetailsScreen(modifier: Modifier = Modifier) {
  val isTablet = DeviceType.isTablet()
  Box(
    modifier = modifier
      .fillMaxSize()
  ) {
    BackgroundObjects(isTablet)
    // Content of the screen
    Column(
      modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text(
        text = stringResource(id = R.string.details_headline),
        style = MaterialTheme.typography.titleLarge.copy(
          color = Color.White.copy(alpha = 0.8F),
          fontFamily = DMSerifText
        )
      )
      Spacer(Modifier.height(30.dp))
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        CustomTextField(hint = "First name", icon = R.drawable.ic_username, isTablet = isTablet)
        CustomTextField(hint = "Last name", icon = R.drawable.ic_person, isTablet = isTablet)
        Spacer(modifier = Modifier.height(20.dp))
        LetsGoButton(isTablet)
      }
    }
  }
}


@Composable
fun LetsGoButton(isTablet: Boolean = false) {
  val horizonalArrangement = if (isTablet) Arrangement.Center
  else
    Arrangement.End
  Row(
    horizontalArrangement = horizonalArrangement, modifier =
    Modifier.fillMaxWidth()
  ) {
    Button(
      onClick = { /* Handle click */ },
      shape = RoundedCornerShape(20.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent
      ),
      modifier = Modifier
          .width(130.dp)
          .height(46.dp)
          .shadow(
              elevation = 10.dp, // Shadow size
              shape = RoundedCornerShape(5.dp)
          )
          .padding(0.dp)
          .background(
              brush = Brush.horizontalGradient(
                  colors = listOf(Color(0xFF4F46E5), Color(0xFF3B82F6))
              ),
              shape = RoundedCornerShape(5.dp)
          )
    ) {
      Text(
        text = stringResource(id = R.string.letsgo_button),
        fontSize = 16.sp,
        color = Color.White,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(0.dp)
      )
    }
  }
}

@Composable
fun BackgroundObjects(isTablet: Boolean = false) {
  val sizeObject = if (isTablet)
    500.dp
  else
    300.dp

  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(Brush.verticalGradient(SignUpBackground))

  )
  {
    Image(
      painter = painterResource(id = R.drawable.onboarding_image),
      contentDescription = null,
      modifier = Modifier
          .size(sizeObject)
          .background(Color.Transparent)
          .align(Alignment.TopEnd),
      contentScale = ContentScale.Fit,
      alpha = 0.1f

    )
    Image(
      painter = painterResource(id = R.drawable.testimg),
      contentDescription = null,
      modifier = Modifier
          .size(sizeObject - 50.dp)
          .background(Color.Transparent)
          .align(Alignment.BottomStart),
      contentScale = ContentScale.Fit,
      alpha = 0.1f

    )
  }
}
