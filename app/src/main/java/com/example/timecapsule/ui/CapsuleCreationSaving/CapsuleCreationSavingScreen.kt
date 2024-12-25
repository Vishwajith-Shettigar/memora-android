package com.example.timecapsule.ui.CapsuleCreationSaving

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.timecapsule.R
import com.example.timecapsule.ui.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.openSansExtraBold
import com.example.timecapsule.viewmodel.CapsuleCreationState
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel
import com.example.timecapsule.viewmodel.CapsuleListScreenState
import com.example.util.NetWorkException
import kotlinx.coroutines.delay

@Preview
@Composable
fun CapsuleCreationSavingScreen(
  viewModel: CapsuleCreationViewModel = hiltViewModel(),
  onNavigate: (NavigationAddCapsule) -> Unit = {}
) {

  val capsuleCreationState by viewModel.capsuleCreationState.collectAsState()

  LaunchedEffect(Unit) {
    viewModel.saveCapsule()
  }

  Scaffold { innerPading ->
    Column(
      modifier = Modifier
          .fillMaxSize()
          .padding(innerPading),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      if (capsuleCreationState is CapsuleCreationState.Loading)
        LoadingScreen()
      if (capsuleCreationState is CapsuleCreationState.Success)
        SuccessScreen(onNavigate)
      if (capsuleCreationState is CapsuleCreationState.Error) {
        ErrorScreen {
          viewModel.saveCapsule()
        }
      }
    }
  }
}

@Composable
fun ErrorScreen(onRetryClick: () -> Unit) {
  Box(
    modifier = Modifier
        .fillMaxSize()
        .zIndex(5.0F)
  ) {
    Column(
      modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight()
          .align(Alignment.Center)
          .padding(top = 20.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(vertical = 20.dp)
            .size(220.dp)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.Red,
                        Color.Red,
                        Color.LightGray.copy(0.1f)
                    ),
                    center = Offset.Unspecified,
                    radius = 220f
                ),
                shape = CircleShape
            )
      ) {
        Image(
          painter = painterResource(id = com.example.timecapsule.R.drawable.nonetwork_graphic),
          contentDescription = "No network",
          modifier = Modifier.size(200.dp)
        )
      }

      Text(
        "Something went wrong!",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 24.sp,
          fontWeight = FontWeight.Light,
          fontFamily = openSansExtraBold
        )
      )

      androidx.compose.material3.Button(
        modifier = Modifier.padding(vertical = 5.dp),
        onClick = {
          onRetryClick()

        },
        colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
      ) {
        Text(text = "Retry", color = Color.LightGray)
      }
    }
  }
}

@Composable
fun SuccessScreen(onNavigate: (NavigationAddCapsule) -> Unit = {}) {

  var timer by remember {
    mutableStateOf(5)
  }

  LaunchedEffect(timer) {
    if (timer == 0) {
      onNavigate(NavigationAddCapsule.NEXT)
    }
  }

  LaunchedEffect(Unit) {
    while (true) {
      delay(1000)
      timer -= 1
    }
  }

  Image(
    painter = painterResource(id = R.drawable.capsule_creation_confirmation),
    contentDescription = null,
    contentScale = ContentScale.Crop,
    modifier = Modifier
        .size(200.dp) // Image size, adjust as needed
        .padding(16.dp)
  )

  Text(
    modifier = Modifier.padding(10.dp),
    text = "Capsule has been created. You can close now",
    style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )

  Text(
    modifier = Modifier.padding(10.dp),
    text = "Screen will be closed in ${timer} seconds",
    style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )
}

@Composable
fun LoadingScreen() {
  RotatingPicture(image = painterResource(id = R.drawable.capsule_image3))
  Text(
    modifier = Modifier.padding(10.dp),
    text = "Please dont close the app or press back.",
    style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
    color = MaterialTheme.colorScheme.onSurfaceVariant
  )
}

@Composable
fun RotatingPicture(image: Painter) {

  val infiniteTransition = rememberInfiniteTransition()

  // Define the animated float state for the rotation
  val rotationAngle by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(2000, easing = LinearEasing), // 1 second for a full rotation
      repeatMode = RepeatMode.Restart
    ), label = "Loading"
  )

  // Apply the rotation to the image
  Image(
    painter = image,
    contentDescription = null,
    contentScale = ContentScale.Crop,
    modifier = Modifier
        .size(200.dp) // Image size, adjust as needed
        .rotate(rotationAngle) // Apply the rotating animation
        .padding(16.dp)
  )
}
