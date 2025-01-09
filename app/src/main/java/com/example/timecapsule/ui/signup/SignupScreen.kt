package com.example.timecapsule.ui.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timecapsule.R
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.SignUpBackground
import com.example.timecapsule.ui.theme.openSansExtraBold
import com.example.timecapsule.util.Device
import com.example.timecapsule.util.DeviceType
import com.example.timecapsule.viewmodel.AuthState
import com.example.timecapsule.viewmodel.SignUpViewModel

@Preview(showBackground = true)
@Composable
fun SignUpScreen(
  navController: NavController = rememberNavController(),
  modifier: Modifier = Modifier, viewModel: SignUpViewModel = hiltViewModel()
) {
  if (DeviceType.getDeviceType() == Device.TABLET)
    SignUpScreenTablet(modifier, viewModel, navController)
  else
    SignUpScreenMobile(modifier, viewModel, navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
  value: String,
  hint: String,
  icon: Int,
  modifier: Modifier = Modifier,
  trailingIcon: Int? = null,
  isError: Boolean = false,
  errorText: String? = null,
  isTablet: Boolean = false,
  onTrailingIconClicked: () -> Unit = {},
  onValueChanged: (String) -> Unit,
) {
  TextField(
    value = value,
    onValueChange = { onValueChanged(it) },
    isError = isError,
    placeholder = {
      Text(
        text = hint,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5F),
      )
    },
    leadingIcon = {
      Image(
        painter = painterResource(id = icon),
        contentDescription = null
      )
    },
    trailingIcon = {
      trailingIcon?.let {
        IconButton(onClick = { /*TODO*/ }) {
          Icon(
            painter = painterResource(id = it), contentDescription = "trailing icon",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5F),
          )
        }
      }
    },
    supportingText = {
      if (isError && errorText != null)
        Text(
          text = errorText,
          color = Color.Red
        )
    },
    shape = RoundedCornerShape(12.dp),
    colors = TextFieldDefaults.textFieldColors(
      containerColor = Color(0xFF60A5FA).copy(alpha = 0.2f),
      focusedIndicatorColor = Color.Transparent,
      unfocusedIndicatorColor = Color.Transparent,
      disabledIndicatorColor = Color.Transparent,
      focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
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


@Composable
fun SignUpScreenMobile(
  modifier: Modifier = Modifier,
  viewModel: SignUpViewModel,
  navController: NavController
) {

  val context = LocalContext.current

  // Observe the authState from the ViewModel
  val authState by viewModel.authState.collectAsState()

  // State variables for user input
  var userName by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var cpassword by remember { mutableStateOf("") }

  val isLoading = authState is AuthState.Loading
  LaunchedEffect(key1 = authState) {
    when (authState) {
      is AuthState.Success -> {
        navController.navigate(Screen.Login.route) {
          popUpTo(Screen.Onboarding.route)
        }
      }

      is AuthState.Error -> {
        val message = (authState as AuthState.Error).message
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
      }

      else -> {}

    }
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

    // Top Decorative Image
//    TopImage()

    Spacer(modifier = Modifier.height(5.dp))
    BodyPart(
      userName = userName,
      email = email,
      password = password,
      cPassword = cpassword,
      isLoading = isLoading,
      onUserNameChanged = { userName = it },
      onEmailChanged = { email = it },
      onPasswordChanged = { password = it },
      onCPasswordChanged = { cpassword = it },
      onRegisterClicked = {
        if (password == cpassword)
          viewModel.signUp(userName, email, password)
        else
          Toast.makeText(context, "Passwords dont match", Toast.LENGTH_LONG).show()
      }, routeToLogin = {
        navController.navigate(Screen.Login.route) {
          popUpTo(Screen.Onboarding.route)
        }
      })
  }
}


@Composable
fun SignUpScreenTablet(
  modifier: Modifier = Modifier,
  viewModel: SignUpViewModel,
  navController: NavController
) {
  val context = LocalContext.current

  // Observe the authState from the ViewModel
  val authState by viewModel.authState.collectAsState()

  // State variables for user input
  var userName by remember { mutableStateOf("") }
  var email by remember { mutableStateOf("") }
  var password by remember { mutableStateOf("") }
  var cpassword by remember { mutableStateOf("") }

  val isLoading = authState is AuthState.Loading
  LaunchedEffect(key1 = authState) {
    when (authState) {
      is AuthState.Success -> {
        navController.navigate(Screen.Login.route) {
          popUpTo(Screen.Onboarding.route)
        }
      }

      is AuthState.Error -> {
        val message = (authState as AuthState.Error).message
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
      }

      else -> {}

    }
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
    BodyPart(isTablet = true,
      userName = userName,
      email = email,
      password = password,
      cPassword = cpassword,
      isLoading = isLoading,
      onUserNameChanged = { userName = it },
      onEmailChanged = { email = it },
      onPasswordChanged = { password = it },
      onCPasswordChanged = { cpassword = it },
      onRegisterClicked = {
        if (password == cpassword)
          viewModel.signUp(userName, email, password)
        else
          Toast.makeText(context, "Passwords dont match", Toast.LENGTH_LONG).show()
      }, routeToLogin = {
        navController.navigate(Screen.Login.route) {
          popUpTo(Screen.Onboarding.route)
        }
      })
  }
}

@Composable
fun BodyPart(
  isTablet: Boolean = false,
  userName: String = "",
  email: String = "",
  password: String = "",
  cPassword: String = "",
  isLoading: Boolean = false,
  onUserNameChanged: (String) -> Unit = {},
  onEmailChanged: (String) -> Unit = {},
  onPasswordChanged: (String) -> Unit = {},
  onCPasswordChanged: (String) -> Unit = {},
  onRegisterClicked: () -> Unit = {},
  routeToLogin: () -> Unit = {}
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
      text = stringResource(id = R.string.register_headline),
      modifier = Modifier.fillMaxWidth(),
      fontSize = 62.sp,
      fontWeight = FontWeight.Bold,
      color = Color.White,
      textAlign = TextAlign.Start,
      fontFamily = openSansExtraBold
    )

    Spacer(modifier = Modifier.height(35.dp))

    // Input Fields
    com.example.timecapsule.ui.login.CustomTextField(
      hint = stringResource(id = R.string.email_hint),
      icon = R.drawable.ic_email,
      value = email,
      onValueChanged = onEmailChanged
    )
    com.example.timecapsule.ui.login.CustomTextField(
      hint = stringResource(id = R.string.password_hint),
      icon = R.drawable.ic_password1,
      value = password,
      onValueChanged = onPasswordChanged
    )
    com.example.timecapsule.ui.login.CustomTextField(
      hint = stringResource(id = R.string.confirm_password_hint),
      icon = R.drawable.ic_cnpassword,
      value = cPassword,
      onValueChanged = onCPasswordChanged
    )
    Spacer(modifier = Modifier.height(16.dp))
    RegisterButton(isLoading = isLoading, onRegisterClicked, routeToLogin)
    Spacer(modifier = Modifier.height(5.dp))
  }
}

@Composable
fun RegisterButton(
  isLoading: Boolean = false, onRegisterClicked: () -> Unit = {},
  routeToLogin: () -> Unit = {}
) {
  Button(
    onClick = { onRegisterClicked() },
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
        modifier = Modifier.size(24.dp), // Set size of progress indicator
        color = Color.White, // Change color if needed
        strokeWidth = 2.dp // Adjust thickness
      )
    else
      Text(
        text = stringResource(id = R.string.register_button),
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
      text = stringResource(id = R.string.login_sentence),
      fontSize = 14.sp,
      color = Color.White.copy(alpha = 0.8f)
    )
    Text(
      text = stringResource(id = R.string.login),
      style = MaterialTheme.typography.labelMedium.copy(
        color = Color.White.copy(alpha = 0.8f)
      ),
      modifier = Modifier
          .padding(horizontal = 3.dp)
          .clickable { routeToLogin() }
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
