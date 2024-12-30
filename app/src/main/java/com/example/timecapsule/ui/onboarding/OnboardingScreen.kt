package com.example.timecapsule.ui.onboarding

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timecapsule.R
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.theme.BottomOnboarding
import com.example.timecapsule.ui.theme.DMSerifText
import com.example.timecapsule.ui.theme.LoginBtnLeft
import com.example.timecapsule.ui.theme.LoginBtnRight
import com.example.timecapsule.ui.theme.TopOnboarding
import com.example.timecapsule.ui.util.Device
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.ui.theme.white

enum class ButtonName {
  LOG_IN,
  SIGN_UP
}

@Composable
fun OnboardingScreen(
  navController: NavController = rememberNavController(),
  modifier: Modifier = Modifier
) {

  if (DeviceType.getDeviceType() == Device.TABLET) {
    OnboardingScreenTablet(modifier) { buttonName ->
      when (buttonName) {
        ButtonName.SIGN_UP -> navController.navigate(Screen.Signup.route)
        ButtonName.LOG_IN -> navController.navigate(Screen.Login.route)
      }
    }
  } else {
    OnboardingScreenMobile(modifier) { buttonName ->
      when (buttonName) {
        ButtonName.SIGN_UP -> navController.navigate(Screen.Signup.route)
        ButtonName.LOG_IN -> navController.navigate(Screen.Login.route)
      }
    }
  }
}

@Composable
fun OnboardingScreenMobile(
  modifier: Modifier = Modifier,
  buttonClicked: (ButtonName) -> Unit = {}
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween,
    modifier = modifier
        .fillMaxSize()
        .background(
            Brush.verticalGradient(
                listOf(
                    Color(TopOnboarding.value),
                    Color(BottomOnboarding.value)
                )
            )
        )
        .systemBarsPadding()
  ) {
    TopImage()

    Spacer(modifier = Modifier.height(16.dp))

    TitleAndSubtitle()

    Spacer(modifier = Modifier.height(32.dp))

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.height(200.dp)
    ) {
      LoginButton(buttonClicked = buttonClicked)

      Spacer(modifier = Modifier.height(16.dp))

      SignupButton(buttonClicked = buttonClicked)
    }
  }
}

@Preview
@Composable
fun OnboardingScreenTablet(
  modifier: Modifier = Modifier,
  buttonClicked: (ButtonName) -> Unit = {}
) {
  Row(
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically,
    modifier = modifier
        .fillMaxSize()
        .background(
            Brush.horizontalGradient(
                listOf(
                    Color(BottomOnboarding.value),
                    Color(TopOnboarding.value)
                )
            )
        )
        .padding(horizontal = 32.dp)
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
          .fillMaxHeight()
          .weight(1f)
    ) {
      TitleAndSubtitle()

      Spacer(modifier = Modifier.height(32.dp))

      LoginButton(buttonClicked = buttonClicked)

      Spacer(modifier = Modifier.height(16.dp))

      SignupButton(buttonClicked = buttonClicked)
    }

    TopImage(modifier = Modifier.weight(1f))
  }
}

@Composable
fun TopImage(modifier: Modifier = Modifier) {
  Box(
    modifier = modifier
        .padding(4.dp)
        .wrapContentSize()
        .clip(CircleShape)
        .background(Color.Transparent),
    contentAlignment = Alignment.Center
  ) {
    Image(
      painter = painterResource(id = R.drawable.onboarding_image),
      contentDescription = null,
      modifier = Modifier
          .clip(CircleShape)
          .size(300.dp)
    )
  }
}

@Composable
fun TitleAndSubtitle(modifier: Modifier = Modifier) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center,
    modifier = modifier.height(100.dp)
  ) {
    // Title text
    Text(
      text = stringResource(id = R.string.app_name),
      style = MaterialTheme.typography.titleLarge.copy(
        fontFamily = DMSerifText,
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      ),
    )

    // Subtitle Text
    Text(
      text = stringResource(id = R.string.app_subtitle),
      style = MaterialTheme.typography.labelMedium.copy(
        color = Color.White.copy(alpha = 0.8f)
      ),
      textAlign = TextAlign.Center,
      modifier = Modifier.width(300.dp)
    )
  }
}

@Composable
fun LoginButton(modifier: Modifier = Modifier, buttonClicked: (ButtonName) -> Unit = {}) {
  val normalColors = listOf(Color(LoginBtnLeft.value), Color(LoginBtnRight.value))
  Box(
    modifier = modifier
        .width(270.dp)
        .height(46.dp)
        .shadow(
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp),
            clip = false
        )
        .background(
            brush = Brush.horizontalGradient(
                colors = normalColors
            ),
            shape = RoundedCornerShape(20.dp)
        ),
    contentAlignment = Alignment.Center
  ) {
    Button(
      onClick = {
        buttonClicked(ButtonName.LOG_IN)
      },
      shape = RoundedCornerShape(20.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent
      ),
      contentPadding = PaddingValues(),
      modifier = Modifier.fillMaxSize()
    ) {
      Text(
        text = stringResource(id = R.string.login_btn),
        style = MaterialTheme.typography.labelLarge.copy(
          color = white
        )
      )
    }
  }
}

@Composable
fun SignupButton(modifier: Modifier = Modifier, buttonClicked: (ButtonName) -> Unit = {}) {
  OutlinedButton(
    onClick = { buttonClicked(ButtonName.SIGN_UP) },
    shape = RoundedCornerShape(20.dp),
    border = BorderStroke(1.dp, Color.Black),
    modifier = modifier
        .width(270.dp)
        .height(46.dp)
        .padding(horizontal = 3.dp, vertical = 3.dp)
  ) {
    Text(
      text = stringResource(id = R.string.signup_btn),
      style = MaterialTheme.typography.labelLarge.copy(
        color = Color.White
      )
    )
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnboardingScreen() {
  OnboardingScreen()
}
