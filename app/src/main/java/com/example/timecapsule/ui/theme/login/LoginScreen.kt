package com.example.timecapsule.ui.theme.login

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.timecapsule.ui.theme.SignUpBackground
import com.example.timecapsule.ui.theme.signup.RegisterButton
import com.example.timecapsule.ui.theme.util.Device
import com.example.timecapsule.ui.theme.util.DeviceType

enum class ButtonName {
  LOG_IN,
  SIGN_UP
}

@Composable
fun LogInScreen(
  navController: NavController = rememberNavController(),
  modifier: Modifier = Modifier
) {
  if (DeviceType.getDeviceType() == Device.TABLET)
    LogInScreenTablet(modifier) { buttonName ->
      when (buttonName) {
        ButtonName.SIGN_UP -> navController.navigate(
          Screen.Signup.route
        ) {
          popUpTo(navController.graph.startDestinationId)
        }

        ButtonName.LOG_IN -> navController.navigate(
          Screen.MainScreens.route
        ) {
          popUpTo(navController.graph.startDestinationId) {
            inclusive = true
          }
        }
      }
    }
  else
    LogInScreenMobile(modifier) { buttonName ->
      when (buttonName) {
        ButtonName.SIGN_UP -> navController.navigate(
          Screen.Signup.route
        ) {
          popUpTo(navController.graph.startDestinationId)
        }

        ButtonName.LOG_IN -> navController.navigate(
          Screen.MainScreens.route
        ) {
          popUpTo(navController.graph.startDestinationId) {
            inclusive = true
          }
        }
      }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
  hint: String,
  icon: Int,
  modifier: Modifier = Modifier,
  isTablet: Boolean = false,
) {
  TextField(
    value = "",
    onValueChange = { /* Handle input */ },
    placeholder = {
      Text(
        text = hint,
        fontSize = 16.sp,
        color = Color.White.copy(alpha = 0.8f)
      )
    },
    leadingIcon = {
      Image(
        painter = painterResource(id = icon),
        contentDescription = null
      )
    },
    shape = RoundedCornerShape(12.dp),
    colors = TextFieldDefaults.textFieldColors(
      containerColor = Color(0xFF60A5FA).copy(alpha = 0.2f),
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent,
      disabledIndicatorColor = Color.Transparent,
      focusedTextColor = Color.White
    ),
    modifier =

    if (!isTablet)
      modifier
        .fillMaxWidth()
        .padding(vertical = 3.dp)
    else
      modifier
        .width(600.dp)
        .padding(vertical = 3.dp)
  )
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
  LogInScreenMobile()
}

@Composable
fun LogInScreenMobile(modifier: Modifier = Modifier, buttonClicked: (ButtonName) -> Unit = {}) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween,
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          SignUpBackground
        )
      )
      .padding(horizontal = 16.dp)
  ) {

    Spacer(modifier = Modifier.height(5.dp))

    // Top Decorative Image
    TopImage()

    Spacer(modifier = Modifier.height(15.dp))
    BodyPart(buttonClicked = buttonClicked)

  }
}


@Composable
fun BodyPart(isTablet: Boolean = false, buttonClicked: (ButtonName) -> Unit = {}) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement =
    if (isTablet) Arrangement.Center
    else
      Arrangement.Top,
    modifier = Modifier.fillMaxSize()
  ) {
    // Title
    Text(
      text = stringResource(id = R.string.login_headline),
      fontSize = 32.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White,
      textAlign = TextAlign.Center
    )


    Spacer(modifier = Modifier.height(35.dp))

    // Input Fields
    CustomTextField(hint = stringResource(id = R.string.email_hint), icon = R.drawable.ic_email)
    CustomTextField(
      hint = stringResource(id = R.string.password_hint),
      icon = R.drawable.ic_password
    )
    Spacer(modifier = Modifier.height(16.dp))
    LogInButton(buttonClicked)
    Spacer(modifier = Modifier.height(5.dp))
  }
}

@Composable
fun LogInButton(buttonClicked: (ButtonName) -> Unit = {}) {
  Button(
    onClick = { buttonClicked(ButtonName.LOG_IN) },
    shape = RoundedCornerShape(20.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = Color.Transparent
    ),
    modifier = Modifier
      .width(270.dp)
      .height(46.dp)
      .shadow(
        elevation = 10.dp, // Shadow size
        shape = RoundedCornerShape(20.dp)
      )
      .background(
        brush = Brush.horizontalGradient(
          colors = listOf(Color(0xFF4F46E5), Color(0xFF3B82F6))
        ),
        shape = RoundedCornerShape(20.dp)
      )
  ) {
    Text(
      text = stringResource(id = R.string.login_button),
      fontSize = 16.sp,
      color = Color.White,
      fontWeight = FontWeight.Bold
    )
  }

  Spacer(modifier = Modifier.height(10.dp))

  // Bottom Text
  Row(
    horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = stringResource(id = R.string.signup_sentence),
      fontSize = 14.sp,
      color = Color.White.copy(alpha = 0.8f)
    )
    Text(
      text = stringResource(id = R.string.signup),
      style = MaterialTheme.typography.labelMedium.copy(
        color = Color.White.copy(alpha = 0.8f)
      ),
      modifier = Modifier
        .padding(horizontal = 3.dp)
        .clickable(onClick = { buttonClicked(ButtonName.SIGN_UP) }),
    )
  }
}

@Composable
fun LogInScreenTablet(modifier: Modifier = Modifier, buttonClicked: (ButtonName) -> Unit = {}) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.horizontalGradient(
          SignUpBackground
        )
      )
      .padding(horizontal = 16.dp)
  ) {

    Spacer(modifier = Modifier.width(5.dp))

    // Top Decorative Image
    TopImage(true)

    Spacer(modifier = Modifier.height(5.dp))
    BodyPart(true, buttonClicked = buttonClicked)

  }
}

@Composable
fun TopImage(isTablet: Boolean = false) {
  Image(
    painter = painterResource(id = R.drawable.testimg),
    contentDescription = null,
    modifier = if (isTablet)
      Modifier.size(500.dp)
    else
      Modifier.size(200.dp)
  )
}
