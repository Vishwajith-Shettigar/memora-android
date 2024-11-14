package com.example.timecapsule.ui.CapsuleCreationSaving

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.timecapsule.R
import com.example.timecapsule.ui.selecttime.NavigationAddCapsule
import com.example.timecapsule.viewmodel.CapsuleCreationState
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel
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
