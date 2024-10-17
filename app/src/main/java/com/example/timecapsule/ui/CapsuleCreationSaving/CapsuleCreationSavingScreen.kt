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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.R

@Composable
fun CapsuleCreationSavingScreen() {

  Scaffold { innerPading ->
    Column(
      modifier = Modifier
          .fillMaxSize()
          .padding(innerPading),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      RotatingPicture(image = painterResource(id = R.drawable.capsule_image3))

      Text(
        modifier = Modifier.padding(10.dp),
        text = "Please dont close the app or press back",
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
fun RotatingPicture(image: Painter) {
  // Create an infinite transition for the rotation
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

@Preview
@Composable
fun RotatingPicturePreview() {
  RotatingPicture(image = painterResource(id = R.drawable.capsule_image3))
}