package com.example.timecapsule.ui.setting.options

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.setting.SettingOptionsTab
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.util.DeviceType

@Composable
fun PrivacyScreen(
  onBackClick: () -> Unit,
  onPrivacyPolicyClicked: () -> Unit,
  onTermsAndServicesClicked: () -> Unit
) {
  val isTablet = DeviceType.isTablet()

  Column(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
        .padding(top = 20.dp),
    verticalArrangement = Arrangement.Top
  ) {
    BackRow() {
      onBackClick()
    }
    Column(
      modifier = Modifier
          .padding(20.dp)
          .fillMaxWidth()
          .fillMaxHeight(),
      horizontalAlignment =
      if (isTablet)
        Alignment.CenterHorizontally
      else
        Alignment.Start
    ) {
      Text(
        modifier = Modifier.padding(vertical = 10.dp),
        text = "Privacy",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 30.sp,
          fontWeight = FontWeight.ExtraBold
        ),
        color = LightBlue
      )

      Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 40.dp, bottom = 10.dp)
      ) {
        SettingOptionsTab(settingTitle = "Privacy Policy") {
          onPrivacyPolicyClicked()
        }

        com.example.timecapsule.ui.setting.Divider(color = Color.Gray)

        SettingOptionsTab(settingTitle = "Terms & Services") {
          onTermsAndServicesClicked()
        }
      }
    }
  }
}
