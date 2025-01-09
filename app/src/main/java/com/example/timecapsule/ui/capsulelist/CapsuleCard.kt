package com.example.timecapsule.ui.capsulelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.CapsuleDetails
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.util.DeviceType
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.delay

@Composable
fun CapsuleCard(
  rowItemSize: Int = 2,
  capsuleDetails: CapsuleDetails? = null,
  modifier: Modifier = Modifier,
  onCapsuleClicked: (id: String) -> Unit = {}, openCapule: (id: String) -> Unit = {}
) {
  // State to toggle the visibility of the pane
  var isPaneVisible by remember { mutableStateOf(false) }

  var isReadyToOpen by remember {
    mutableStateOf(false)
  }

  // Card UI
  Card(
    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
    modifier = modifier
        .wrapContentHeight()
        .fillMaxWidth(),
    elevation = CardDefaults.cardElevation(10.dp),
    shape = RoundedCornerShape(6.dp),
    onClick = { onCapsuleClicked(capsuleDetails!!.id) }
  ) {
    Column(
      modifier =
      if (rowItemSize == 1) Modifier
          .padding(16.dp)
          .background(MaterialTheme.colorScheme.primaryContainer)
      else Modifier
          .padding(5.dp)
          .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
      ResponsiveRowColumn(
        title = capsuleDetails!!.title,
        timerValue = capsuleDetails.time,
        rowItemSize = rowItemSize,
        setReadyToOpen = {
          isReadyToOpen = true
        }
      )

      if (rowItemSize == 1) {
        Spacer(modifier = Modifier.height(8.dp))
      }

      AsyncImage(
        modifier = Modifier
            .size(200.dp)
            .align(Alignment.CenterHorizontally),
        model = capsuleDetails.imageUrl, contentDescription = "capsule image",
      )
      if (isReadyToOpen) {
        Row(
          modifier = Modifier
              .fillMaxWidth()
              .wrapContentHeight(),
          horizontalArrangement = Arrangement.Center
        )
        {
          Button(
            onClick = { openCapule(capsuleDetails.id) },
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
          ) {
            Text(text = "Open", color = Color.LightGray)
          }
        }
      }

      // Icon Button at the bottom right
      Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.End,
      ) {

        Box(
          modifier = Modifier
              .size(30.dp)
              .clip(shape = RoundedCornerShape(100))
              .background(Color.Gray.copy(alpha = 0.2F))
              .align(Alignment.CenterVertically)
        )
        {
          IconButton(
            onClick = { isPaneVisible = !isPaneVisible },
          ) {
            Icon(
              painter =
              if (isPaneVisible)
                painterResource(id = R.drawable.ic_drop_down)
              else
                painterResource(id = R.drawable.ic_drop_up),
              contentDescription = "More Options",
              tint = Color.Black
            )
          }
        }
      }
      AnimatedVisibility(
        visible = isPaneVisible,
      ) {
        SmallPane(
          isOwner = capsuleDetails.isOwner,
          ownerUserName = capsuleDetails.ownerUserName,
          createdDate = capsuleDetails.time,
          description = capsuleDetails.description,
        )
      }
    }
  }
}

@Composable
fun ResponsiveRowColumn(
  modifier: Modifier = Modifier,
  title: String,
  timerValue: Timestamp,
  rowItemSize: Int,
  setReadyToOpen: (Boolean) -> Unit = {}
) {
  val smallerTextsize: Boolean = if (rowItemSize == 2 && !DeviceType.isTablet()) {
    true
  } else
    false
  if (rowItemSize == 2) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = if (smallerTextsize)
            17.sp
          else
            35.sp
        )
      )
      Spacer(modifier = Modifier.height(8.dp))
      TimerPlaceholder(timerValue = timerValue, smallerTextsize, setReadyToOpen = setReadyToOpen)
    }
  } else {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
      )
      TimerPlaceholder(timerValue = timerValue, setReadyToOpen = setReadyToOpen)
    }
  }
}

@Composable
fun TimerPlaceholder(
  timerValue: Timestamp, // The target time to countdown to
  isSmallSize: Boolean = false,
  setReadyToOpen: (Boolean) -> Unit = {}
) {
  var remainingTime by remember { mutableStateOf("") }


  // Logic to update the timer every second
  LaunchedEffect(timerValue) {
    while (true) {
      val currentTime = Timestamp.now()
      val diffInMillis = timerValue.toDate().time - currentTime.toDate().time

      if (diffInMillis > 0) {
        val daysLeft = TimeUnit.MILLISECONDS.toDays(diffInMillis)
        val hoursLeft = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
        val minutesLeft = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60
        val secondsLeft = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) % 60

        remainingTime = when {
          daysLeft > 0 -> "${daysLeft}D" // If more than 1 day is left, show in days
          hoursLeft > 0 -> "${hoursLeft}H" // If more than 1 hour is left, show in hours
          minutesLeft > 0 -> "${minutesLeft}m ${secondsLeft}s" // Less than an hour, show minutes and seconds
          else -> "${secondsLeft}S" // Less than a minute, show in seconds
        }
      } else {
        remainingTime = "00000"
        setReadyToOpen(true)
        break
      }
      delay(1000) // Update every second
    }
  }

  // Display the remaining time
  Row {
    remainingTime.forEach { digit ->
      Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 1.dp)
            .clip(RoundedCornerShape(10))
            .background(Color.Black)
            .padding(horizontal = 4.dp)
      ) {
        Text(
          text = digit.toString(),
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            fontSize = if (isSmallSize) 14.sp else 25.sp
          ),
          color = Color.Gray
        )
      }
    }
  }
}

@Composable
fun SmallPane(
  isOwner: Boolean = false,
  ownerUserName: String = "",
  createdDate: Timestamp = Timestamp.now(),
  description: String = "",
) {
  Card(
    modifier = Modifier
      .fillMaxWidth(),
    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
  ) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
    {
      Text(
        text =
        if (isOwner)
          "Created by you"
        else
          "Created by ${ownerUserName}",
        style = MaterialTheme.typography.bodySmall
      )
      Text(
        text = formatTimestamp(timestamp = createdDate),
        modifier = Modifier.padding(vertical = 5.dp),
        style = MaterialTheme.typography.bodySmall
      )
      Text(
        text = description,
        modifier = Modifier.padding(vertical = 5.dp),
        style = MaterialTheme.typography.bodySmall
      )
    }
  }
}

fun formatTimestamp(timestamp: Timestamp): String {
  val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
  return dateFormat.format(timestamp.toDate())
}

@Preview(showBackground = true)
@Composable
fun CustomCardPreview() {
  CapsuleCard()
}
