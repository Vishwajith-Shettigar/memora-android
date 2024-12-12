package com.example.timecapsule.ui.capsulelist.v2

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.R
import com.example.timecapsule.ui.sharewithpeople.Profile
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.fiveStarColor
import com.example.timecapsule.ui.theme.openSansExtraBold
import com.example.timecapsule.viewmodel.ProfileState


@Composable
fun CapsuleCard() {
  Scaffold { innerPadding ->
    Column(modifier = Modifier.padding(vertical = 100.dp, horizontal = 5.dp)) {

      var isExpanded by remember {
        mutableStateOf(false)
      }

      Card(
        modifier = Modifier
            .then(
                if (!isExpanded)
                    Modifier.fillMaxWidth(0.5F)
                else
                    Modifier.fillMaxWidth(1F)
            )
            .fillMaxWidth(1F)
            .wrapContentSize()
            .animateContentSize()
            .clip(RoundedCornerShape(30.dp))
            .clickable(enabled = true) {
                isExpanded = !isExpanded
            }
            .background(fiveStarColor)
            .padding(horizontal = 5.dp, vertical = 10.dp),
        colors = CardDefaults.cardColors(containerColor = fiveStarColor),
        content = {
          Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
          ) {
            Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 8.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "The Family",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontSize = if (isExpanded) 25.sp else 17.sp,
                  color = Color.Black,
                  fontWeight = FontWeight.ExtraBold,
                )
              )

              IconButton(modifier = Modifier
                  .size(30.dp)
                  .border(
                      width = 1.dp,
                      shape = CircleShape,
                      color = MaterialTheme.colorScheme.primaryContainer
                  ), onClick = {
              }) {
                Icon(
                  painter = painterResource(id = com.example.timecapsule.R.drawable.ic_open_in_new),
                  contentDescription = "Open",
                  tint = Color.Black
                )
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
                painter = painterResource(id = com.example.timecapsule.R.drawable.ic_time_range),
                contentDescription = "timer icon",
                tint = Color.Black,
                modifier = Modifier.size(55.dp)
              )

              Text(
                text = "160:10",
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
                text = "Lorem ipsum diotelah oeo minmoto imsush koyo keio hyetya okmoa",
                style = MaterialTheme.typography.titleLarge.copy(
                  fontSize = 14.sp,
                  color = Color.Black,
                  fontWeight = FontWeight.Bold,
                  lineHeight = TextUnit(20F, TextUnitType.Sp)
                )
              )
            }
            if (isExpanded)
              Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 5.dp)
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Profile(
                  userName = "dark6v",
                  "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/default_profile_pictures%2Ftestimg3.jpg?alt=media&token=0f8ad9af-9661-462f-9dfd-d99612109170",
                  true,
                  isOwner = true,
                  size = 40.dp,
                  hideUserName = true,
                  remove = {}
                )
                Profile(
                  userName = "dark6v",
                  "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/default_profile_pictures%2Ftestimg3.jpg?alt=media&token=0f8ad9af-9661-462f-9dfd-d99612109170",
                  true,
                  size = 40.dp,
                  hideUserName = true,
                  remove = {}
                )
                Profile(
                  userName = "dark6v",
                  "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/default_profile_pictures%2Ftestimg3.jpg?alt=media&token=0f8ad9af-9661-462f-9dfd-d99612109170",
                  true,
                  size = 40.dp,
                  hideUserName = true,
                  remove = {}
                )
              }
          }
        }
      )
    }
  }
}

@Preview
@Composable
fun PreviewCapsuleCard() {
  CapsuleCard()
}
