package com.example.timecapsule.ui.onboarding

import android.widget.Toast
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timecapsule.R
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.signup.CustomTextField
import com.example.timecapsule.ui.theme.DMSerifText
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.SignUpBackground
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.viewmodel.AuthState
import com.example.timecapsule.viewmodel.OnBoardingDetailsViewModel
import com.example.util.AskDetailsException
import com.example.util.UnspecifiedException
import com.example.util.UnverifiedEmailException
import com.example.util.UsernameAlreadyExistsException
import kotlin.math.ln

@Preview
@Composable
fun OnBoardingDetailsScreen(
  navController: NavController = rememberNavController(),
  modifier: Modifier = Modifier,
  viewModel: OnBoardingDetailsViewModel = hiltViewModel()
) {
  val isTablet = DeviceType.isTablet()

  val context = LocalContext.current

  // Observe the authState from the ViewModel
  val authState by viewModel.authState.collectAsState()


  val isLoading = authState is AuthState.Loading

  // State variables for user input
  var userName by remember { mutableStateOf("") }
  var fName by remember { mutableStateOf("") }
  var lName by remember { mutableStateOf("") }



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
        val message = (authState as AuthState.Error).message

        if ((authState as AuthState.Error).exception is UsernameAlreadyExistsException) {
          Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        } else if ((authState as AuthState.Error).exception is UnspecifiedException) {
          Toast.makeText(context, message, Toast.LENGTH_LONG).show()
          viewModel.signOut()
          navController.navigate(Screen.OnboardingScreens.route) {
            popUpTo(Screen.AskDetails.route) {
              inclusive = true
            }
          }

        } else {
          Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
      }

      else -> Unit
    }
  }


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
        CustomTextField(
          value = userName,
          hint = "User name",
          icon = R.drawable.ic_username,
          isTablet = isTablet
        ) { value ->
          userName = value

        }
        CustomTextField(
          value = fName,
          hint = "First name",
          icon = R.drawable.ic_username,
          isTablet = isTablet
        ) { value ->
          fName = value

        }
        CustomTextField(
          value = lName,
          hint = "Last name",
          icon = R.drawable.ic_person,
          isTablet = isTablet
        ) { value ->
          lName = value
        }
        Spacer(modifier = Modifier.height(20.dp))
        LetsGoButton(isTablet, isLoading) {
          viewModel.saveDetails(userName, fName, lName)
        }
      }
    }
  }
}

@Composable
fun LetsGoButton(isTablet: Boolean = false, isLoading: Boolean = false, onBtnClicked: () -> Unit) {
  val horizonalArrangement = if (isTablet) Arrangement.Center
  else
    Arrangement.End
  Row(
    horizontalArrangement = horizonalArrangement, modifier =
    Modifier.fillMaxWidth()
  ) {
    Button(
      onClick = { onBtnClicked() },
      shape = RoundedCornerShape(20.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = Color.Transparent
      ),
      modifier = Modifier
        .wrapContentWidth()
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
      if (isLoading)
        CircularProgressIndicator(
          modifier = Modifier.size(24.dp),
          color = Color.White,
          strokeWidth = 2.dp
        )
      else
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
      .background(LightBlue.copy(alpha = 0.6f))

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
