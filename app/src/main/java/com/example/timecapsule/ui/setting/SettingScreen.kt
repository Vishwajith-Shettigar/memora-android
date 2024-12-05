package com.example.timecapsule.ui.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.ui.theme.LightBlue

@Preview
@Composable
fun SettingScreen() {
  Scaffold() { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
        .padding(innerPadding)
    ) {
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(top = 30.dp)
          .background(MaterialTheme.colorScheme.primary)
          .align(Alignment.TopStart)
          .background(Color.Transparent)
      ) {
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(start = 20.dp, end = 20.dp)
              .height(60.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Settings",
              style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 30.sp,
                color = LightBlue,
                fontWeight = FontWeight.SemiBold
              )
            )
          }
        }
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .wrapContentHeight()
              .padding(top = 40.dp, bottom = 10.dp, start = 20.dp, end = 20.dp)
          ) {
            SettingOptionsTab(settingTitle = "Change Password") {
            }

            com.example.timecapsule.ui.setting.Divider(color = Color.Gray)

            SettingOptionsTab(settingTitle = "Change Language") {
            }

            com.example.timecapsule.ui.setting.Divider(color = Color.Gray)

            SettingOptionsTab(settingTitle = "Rate out app") {
            }

            com.example.timecapsule.ui.setting.Divider(color = Color.Gray)

            SettingOptionsTab(settingTitle = "Updates") {
            }

            com.example.timecapsule.ui.setting.Divider(color = Color.Gray)
          }
        }

        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .wrapContentHeight()
              .padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
              .clip(RoundedCornerShape(20.dp))
              .background(LightBlue.copy(alpha = 0.4F))
              .padding(start = 10.dp, end = 10.dp),

            ) {
            SettingOptionsTabWithSwitch(settingTitle = "Receive Notifications", isChecked = true) {
            }

            com.example.timecapsule.ui.setting.Divider(color = Color.Black)

            SettingOptionsTabWithSwitch(
              settingTitle = "Allow me in others' capsules.",
              isChecked = false
            ) {
            }
          }
        }
      }
    }
  }
}

@Composable
fun Divider(color: Color) {
  Divider(
    color = color,
    modifier = Modifier.padding(horizontal = 4.dp)
  )
}

@Composable
fun SettingOptionsTab(settingTitle: String, onClick: () -> Unit) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .height(60.dp)
      .padding(vertical = 5.dp)
      .clickable(
        enabled = true
      ) {
        onClick()
      },
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = settingTitle,
      style = MaterialTheme.typography.titleLarge.copy(fontSize = 17.sp),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Icon(
      painter = painterResource(id = com.example.timecapsule.R.drawable.ic_double_arrow_right),
      contentDescription = "Arrow",
      tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Composable
fun SettingOptionsTabWithSwitch(
  settingTitle: String,
  isChecked: Boolean,
  onChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(vertical = 20.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {

    Text(
      text = settingTitle,
      style = MaterialTheme.typography.titleLarge.copy(fontSize = 17.sp),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Switch(
      checked = isChecked, onCheckedChange = {
        onChange(it)
      },
      colors = androidx.compose.material3.SwitchDefaults.colors(
        checkedThumbColor = MaterialTheme.colorScheme.primary,
        uncheckedThumbColor = Color.White,
        uncheckedTrackColor = Color.Gray,
        checkedTrackColor = LightBlue
      )
    )
  }
}
