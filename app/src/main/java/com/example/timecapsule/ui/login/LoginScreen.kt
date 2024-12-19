package com.example.timecapsule.ui.login

import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.timecapsule.R
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.SignUpBackground
import com.example.timecapsule.ui.theme.openSansExtraBold
import com.example.timecapsule.ui.util.Device
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.viewmodel.AuthState
import com.example.timecapsule.viewmodel.LogInViewModel
import com.example.timecapsule.viewmodel.SignUpViewModel
import com.example.util.AskDetailsException
import com.example.util.UnverifiedEmailException

enum class ButtonName {
  LOG_IN,
  SIGN_UP
}

@Composable
fun LogInScreen(
  navController: NavController = rememberNavController(),
  modifier: Modifier = Modifier, viewModel: LogInViewModel = hiltViewModel()
) {
  if (DeviceType.getDeviceType() == Device.TABLET)
    LogInScreenTablet(modifier, navController = navController, viewModel = viewModel)
  else
    LogInScreenMobile(modifier, navController = navController, viewModel = viewModel)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
  hint: String,
  icon: Int,
  modifier: Modifier = Modifier,
  isTablet: Boolean = false,
  value: String = "",
  onValueChanged: (String) -> Unit = {}
) {
  TextField(
    value = value,
    onValueChange = { onValueChanged(it) },
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
fun LogInScreenMobile(
  modifier: Modifier = Modifier, navController: NavController = rememberNavController(),
  viewModel: LogInViewModel = hiltViewModel()
) {
  val context = LocalContext.current

  // Observe the authState from the ViewModel
  val authState by viewModel.authState.collectAsState()

  var showDialog by remember {
    mutableStateOf(false)
  }

  val isLoading = authState is AuthState.Loading

  // State variables for user input
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  LaunchedEffect(key1 = authState) {
    when (authState) {
      is AuthState.Success -> {
        navController.navigate(Screen.MainScreens.route) {
          popUpTo(Screen.OnboardingScreens.route) {
            inclusive = true
          }
        }
      }

      is AuthState.Error -> {
        if ((authState as AuthState.Error).exception is AskDetailsException) {
          navController.navigate(Screen.AskDetails.route) {
            popUpTo(Screen.Onboarding.route)
          }
        } else if ((authState as AuthState.Error).exception is UnverifiedEmailException) {
          showDialog = true

        } else {
          val message = (authState as AuthState.Error).message
          Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
      }

      else -> Unit
    }
  }

  if (showDialog)
    TitleSubtitleWithOkayButtonDialog(
      title = "Email Verification Required",
      subtitle = "Please verify your email to log in. Check your inbox and click on the verification link."
    ) {
      showDialog = false
    }

  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween,
    modifier = modifier
      .fillMaxSize()
      .background(
        LightBlue.copy(alpha = 0.6f)
      )
      .padding(horizontal = 16.dp)
      .systemBarsPadding()
  ) {

    Spacer(modifier = Modifier.height(5.dp))

//    // Top Decorative Image
//    TopImage()

    Spacer(modifier = Modifier.height(15.dp))
    BodyPart(
      password = password,
      email = email,
      isLoading = isLoading,
      buttonClicked = { buttonName ->
        when (buttonName) {
          ButtonName.SIGN_UP -> navController.navigate(
            Screen.Signup.route
          ) {
            popUpTo(Screen.Onboarding.route)
          }

          ButtonName.LOG_IN -> {
            viewModel.signIn(email, password)
          }
        }

      },
      onEmailChanged = { email = it },
      onPasswordChanged = { password = it },
    )
  }
}

@Composable
fun TitleSubtitleWithOkayButtonDialog(
  title: String, subtitle: String,
  onDismiss: () -> Unit = {}
) {
  // The dialog content with the message and buttons
  AlertDialog(
    onDismissRequest = onDismiss, // Close the dialog when dismissed
    title = {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    },
    text = {
      Text(
        text = subtitle,
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 16.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    },
    confirmButton = {
    },
    dismissButton = {
      Button(colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
        onClick = {
          onDismiss()
        }
      ) {
        Text(
          "Ok", style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
      }
    }
  )
}

@Composable
fun BodyPart(
  isTablet: Boolean = false, email: String = "",
  password: String = "",
  isLoading: Boolean = false,
  onEmailChanged: (String) -> Unit = {},
  onPasswordChanged: (String) -> Unit = {}, buttonClicked: (ButtonName) -> Unit = {}
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement =
    if (isTablet) Arrangement.Center
    else
      Arrangement.Center,
    modifier = Modifier.fillMaxSize()
  ) {
    // Title
    Text(
      modifier = Modifier.fillMaxWidth(),
      text = stringResource(id = R.string.login_headline),
      fontSize = 62.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White,
      textAlign = TextAlign.Start,
      fontFamily = openSansExtraBold
    )
    Spacer(modifier = Modifier.height(35.dp))
    // Input Fields
    CustomTextField(
      value = email,
      hint = stringResource(id = R.string.email_hint),
      icon = R.drawable.ic_email,
      onValueChanged = onEmailChanged
    )
    CustomTextField(
      value = password,
      hint = stringResource(id = R.string.password_hint),
      icon = R.drawable.ic_password1, onValueChanged = onPasswordChanged
    )
    Spacer(modifier = Modifier.height(16.dp))
    LogInButton(isLoading, buttonClicked)
    Spacer(modifier = Modifier.height(5.dp))
  }
}

@Composable
fun LogInButton(isLoading: Boolean, buttonClicked: (ButtonName) -> Unit = {}) {
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
    if (isLoading)
      CircularProgressIndicator(
        modifier = Modifier.size(24.dp),
        color = Color.White,
        strokeWidth = 2.dp
      )
    else
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
fun LogInScreenTablet(
  modifier: Modifier = Modifier, navController: NavController = rememberNavController(),
  viewModel: LogInViewModel = hiltViewModel()
) {

  val context = LocalContext.current

  // Observe the authState from the ViewModel
  val authState by viewModel.authState.collectAsState()

  var showDialog by remember {
    mutableStateOf(false)
  }

  val isLoading = authState is AuthState.Loading

  // State variables for user input
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }

  LaunchedEffect(key1 = authState) {
    when (authState) {
      is AuthState.Success -> {
        navController.navigate(Screen.MainScreens.route) {
          popUpTo(Screen.OnboardingScreens.route) {
            inclusive = true
          }
        }
      }

      is AuthState.Error -> {
        if ((authState as AuthState.Error).exception is AskDetailsException) {
          navController.navigate(Screen.AskDetails.route) {
            popUpTo(Screen.Onboarding.route)
          }
        } else if ((authState as AuthState.Error).exception is UnverifiedEmailException) {
          showDialog = true

        } else {
          val message = (authState as AuthState.Error).message
          Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
      }

      else -> Unit
    }
  }

  if (showDialog)
    TitleSubtitleWithOkayButtonDialog(
      title = "Email Verification Required",
      subtitle = "Please verify your email to log in. Check your inbox and click on the verification link."
    ) {
      showDialog = false
    }

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
    BodyPart(
      isTablet = true, password = password,
      email = email,
      isLoading = isLoading,
      buttonClicked = { buttonName ->
        when (buttonName) {
          ButtonName.SIGN_UP -> navController.navigate(
            Screen.Signup.route
          ) {
            popUpTo(Screen.Onboarding.route)
          }

          ButtonName.LOG_IN -> {
            viewModel.signIn(email, password)
          }
        }

      },
      onEmailChanged = { email = it },
      onPasswordChanged = { password = it },
    )
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
