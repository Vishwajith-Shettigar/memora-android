package com.example.timecapsule.ui.setting.options

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.R
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.util.DeviceType

@Composable
fun ContactUsScreen(onBackClick: () -> Unit) {
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
        text = "Contact Us",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 30.sp,
          fontWeight = FontWeight.ExtraBold
        ),
        color = LightBlue
      )

      Column(
        modifier = Modifier
          .padding(10.dp)
          .height(100.dp)
          .then(
            if (isTablet)
              Modifier.width(600.dp)
            else
              Modifier.fillMaxWidth()
          )
          .clip(RoundedCornerShape(20.dp))
          .background(MaterialTheme.colorScheme.primaryContainer)
          .padding(10.dp),
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start
      ) {
        Row(
          modifier = Modifier.wrapContentSize(),
          horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            painter = painterResource(id = com.example.timecapsule.R.drawable.ic_email),
            contentDescription = "email icon",
            tint = LightBlue,
            modifier = Modifier
              .size(25.dp)
              .padding(end = 6.dp)
          )
          Text(
            text = "Email",
            style = MaterialTheme.typography.labelSmall.copy(
              color = LightBlue,
              fontSize = 16.sp
            )
          )
        }
        Text(
          text = "zekromvishwa12345@gmail.com",
          style = MaterialTheme.typography.labelSmall.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 17.sp
          )
        )
      }

    }
  }
}