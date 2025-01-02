package com.example.timecapsule.ui.capsulelist.v2

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CapsuleDetails
import com.example.timecapsule.R
import com.example.timecapsule.ui.review.SharedWithALlIcon
import com.example.timecapsule.ui.sharewithpeople.Profile
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.fiveStarColor
import com.example.timecapsule.ui.theme.openSansExtraBold
import com.example.timecapsule.ui.util.DeviceType
import com.google.firebase.Timestamp
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.delay


@Composable
fun CapsuleCard(
  capsuleDetails: CapsuleDetails,
  isExpanded: Boolean,
  bgColor: Color,
  onClick: () -> Unit,
  onCapsuleDetailsClicked: (String) -> Unit,
  openCapule: (id: String, isSurPriseCapsule: Boolean) -> Unit = { _, _ -> }
) {

  val isTablet = DeviceType.isTablet()

  var icon by remember {
    mutableStateOf<Int>(com.example.timecapsule.R.drawable.ic_time_range)
  }

  var remainingTime by remember { mutableStateOf("") }
  var isReadyToOpen by remember {
    mutableStateOf(false)
  }

  // Logic to update the timer every second
  LaunchedEffect(capsuleDetails.time) {
    while (true) {
      val currentTime = Timestamp.now()
      val diffInMillis = capsuleDetails.time.toDate().time - currentTime.toDate().time

      if (diffInMillis > 0) {
        val daysLeft = TimeUnit.MILLISECONDS.toDays(diffInMillis)
        val hoursLeft = TimeUnit.MILLISECONDS.toHours(diffInMillis) % 24
        val minutesLeft = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60
        val secondsLeft = TimeUnit.MILLISECONDS.toSeconds(diffInMillis) % 60

        if (isReadyToOpen)
          isReadyToOpen = false

        remainingTime = when {
          daysLeft > 0 -> {
            if (daysLeft > 100) {
              icon = com.example.timecapsule.R.drawable.ic_time_filled
            }
            "${daysLeft}d"
          } // If more than 1 day is left, show in days
          hoursLeft > 0 -> {
            if (hoursLeft < 24) {
              icon = com.example.timecapsule.R.drawable.ic_bolt

            }
            "${hoursLeft}h"
          }// If more than 1 hour is left, show in hours
          minutesLeft > 0 -> "${minutesLeft}m ${secondsLeft}s" // Less than an hour, show minutes and seconds
          else -> "${secondsLeft}s" // Less than a minute, show in seconds
        }
      } else {
        remainingTime = "000:00"
        isReadyToOpen = true
        break
      }
      delay(1000) // Update every second
    }
  }

  Card(
    modifier = Modifier
        .fillMaxWidth()
        .then(
            if (!isExpanded)

                Modifier.defaultMinSize(minHeight = 210.dp)
            else
                Modifier.wrapContentHeight()
        )

        .animateContentSize()
        .clip(RoundedCornerShape(30.dp))
        .clickable(enabled = true) {
            if (!isTablet)
                onClick()
        }
        .background(bgColor)
        .padding(
            horizontal =

            if (isTablet) 15.dp
            else
                2.dp, vertical =
            if (isTablet)
                14.dp
            else
                10.dp
        ),
    colors = CardDefaults.cardColors(containerColor = bgColor),
    content = {
      Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
      ) {
        Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(start = 8.dp, end = 8.dp, top = 9.dp, bottom = 3.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {

          Text(
            modifier = Modifier.weight(0.7F),
            text = capsuleDetails.title,
            style = MaterialTheme.typography.titleLarge.copy(
              fontSize = if (isExpanded) 25.sp else 20.sp,
              color = Color.Black,
              fontWeight = FontWeight.ExtraBold,
            ),
            softWrap = true
          )

          Box(
            modifier = Modifier
              .weight(0.3F)
          ) {
            if (capsuleDetails.isSurpriseCapsule)
              Image(
                painter = painterResource(id = com.example.timecapsule.R.drawable.capsule_creation_confirmation),
                contentDescription = "Official icon",
                modifier = Modifier.size(50.dp)
              )
            else
              IconButton(modifier = Modifier
                  .align(Alignment.Center)
                  .size(25.dp)
                  .border(
                      width = 1.dp,
                      shape = CircleShape,
                      color = MaterialTheme.colorScheme.primaryContainer
                  ), onClick = {
                onCapsuleDetailsClicked(capsuleDetails.id)
              }) {
                Icon(
                  modifier = Modifier.size(25.dp),
                  painter = painterResource(id = com.example.timecapsule.R.drawable.ic_open_in_new),
                  contentDescription = "Open",
                  tint = Color.Black
                )
              }
          }
        }

        Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(top = 20.dp, bottom = 10.dp),
          horizontalArrangement = Arrangement.Start,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            painter = painterResource(id = icon),
            contentDescription = "timer icon",
            tint = Color.Black,
            modifier = Modifier.size(65.dp)
          )

          Text(
            text = remainingTime,
            style = MaterialTheme.typography.titleLarge.copy(
              fontSize = 33.sp,
              color = Color.Black,
              fontWeight = FontWeight.ExtraBold,
              fontFamily = openSansExtraBold
            )
          )
        }

        Row(
          modifier = Modifier
              .fillMaxWidth()
              .padding(top = 2.dp, bottom = 5.dp)
              .padding(horizontal = 10.dp),
          horizontalArrangement = Arrangement.Start,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = capsuleDetails.description,
            style = MaterialTheme.typography.titleLarge.copy(
              fontSize = 15.sp,
              color = Color.Black,
              fontWeight = FontWeight.Bold,
              lineHeight = TextUnit(20F, TextUnitType.Sp),
            ),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis

          )
        }
        if (isExpanded || isTablet)
          Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 5.dp)
                .padding(horizontal = 10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              modifier = Modifier
                .weight(0.7F),
              horizontalArrangement = Arrangement.Start,
              verticalAlignment = Alignment.CenterVertically
            ) {

              if (capsuleDetails.users.size > 3) {

                (capsuleDetails.users).slice(0..2).forEach {
                  Profile(
                    userId = it["userId"] as String,
                    userName = it["userName"] as String,
                    imageUrl = it["imageUrl"] as String,
                    true,
                    isOwner = it["isOwner"] as Boolean,
                    size = 40.dp,
                    hideUserName = true,
                    remove = {}
                  )
                }
                if (((capsuleDetails.users).size - 3) > 0)
                  SharedWithALlIcon(text = ((capsuleDetails.users).size - 3).toString(), fontColor = Color.White)
              } else {
                capsuleDetails.users.forEach {
                  Profile(
                    userId = it["userId"] as String,
                    userName = it["userName"] as String,
                    imageUrl = it["imageUrl"] as String,
                    true,
                    isOwner = it["isOwner"] as Boolean,
                    size = 40.dp,
                    hideUserName = true,
                    remove = {}
                  )
                }
                if (capsuleDetails.isSharedWithAll) {
                  SharedWithALlIcon(textFontSize = 15.sp, fontColor = Color.White)
                }
              }
            }

            if (isReadyToOpen) {
              Row(
                modifier = Modifier.weight(0.3F),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
              )
              {
                Button(
                  onClick = { openCapule(capsuleDetails.id, capsuleDetails.isSurpriseCapsule) },
                  colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
                ) {
                  Text(
                    text = "Open", style = MaterialTheme.typography.titleLarge.copy(
                      fontSize = 18.sp,
                      color = Color.Black,
                      fontWeight = FontWeight.Bold,
                    )
                  )
                }
              }
            }
          }
      }

    }
  )
}

