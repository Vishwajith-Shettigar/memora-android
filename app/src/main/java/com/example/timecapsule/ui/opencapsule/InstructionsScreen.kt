package com.example.timecapsule.ui.opencapsule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.ui.theme.SubTitleFontColor

@Preview
@Composable
fun InstructionsScreen() {
  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
  ) {
    Text(
      text = "Hi! We will help you open your time capsule. There’s also a letter included," +
        " which you can read on the next screen. ",
      style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      fontWeight = FontWeight.Bold,
      modifier = Modifier
          .padding(8.dp)
          .align(Alignment.Center)
    )
    Box(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .align(Alignment.BottomCenter)
    ) {
      OpenCapsuleNextButtonRow()
    }
  }
}

@Preview
@Composable
fun OpenCapsuleNextButtonRow() {
  Row(
      Modifier
          .fillMaxWidth()
          .padding(bottom = 30.dp, end = 10.dp),
    horizontalArrangement = Arrangement.Absolute.Right,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = "Please press next to move forward.",
      color = SubTitleFontColor,
      modifier = Modifier.padding(horizontal = 10.dp)
    )
    Button(onClick = { /*TODO*/ }) {
      Text(text = "next")
    }
  }
}
