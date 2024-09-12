package com.example.timecapsule.ui.theme.profile

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.palette.graphics.Palette
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.SubTitleFontColor
import com.example.timecapsule.ui.theme.util.DeviceType

@Preview
@Composable
fun ProfileScreen() {

  val defaultColor = MaterialTheme.colorScheme.primary
  // State to hold the background color
  var backgroundColor by remember { mutableStateOf(defaultColor) }  // default color

  val context = LocalContext.current
  // Load image and get dominant color
  LaunchedEffect(Unit) {
    val myBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.testimg1)
    val palette = Palette.from(myBitmap).generate()
    backgroundColor = Color(palette.getVibrantColor(android.graphics.Color.RED))
    Log.e("pokemon", backgroundColor.toString())
  }
  Scaffold { innerPadding ->
    Log.e("pokemon", backgroundColor.toString())
    Column(
      modifier = Modifier
          .padding(bottom = innerPadding.calculateBottomPadding())
          .fillMaxSize()
          .background((backgroundColor))
    ) {
      Image(
        modifier = Modifier
            .fillMaxWidth()
            .height(1000.dp)
            .weight(0.4f),
        painter = painterResource(id = R.drawable.testimg1), contentDescription = "cover image",
        contentScale = ContentScale.Crop,
      )

      Column(
        modifier =

        Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
            )
            .padding(top = 10.dp)
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {

        // Profile Picture
        Image(
          painter = painterResource(id = R.drawable.testimg6),
          contentDescription = "Profile Picture",
          modifier = Modifier
              .size(110.dp)
              .clip(CircleShape),
          contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Username and full name
        Text(
          "DarkX12", style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 20.sp
          )
        )
        Text(
          "John doe", style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = SubTitleFontColor,
            fontSize = 15.sp
          )
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsOption(icon = R.drawable.ic_darkmode, text = "Dark Mode", true)
        SettingsOption(icon = R.drawable.ic_setting, text = "Setting")
        SettingsOption(icon = R.drawable.ic_contactus, text = "Contact Us")
        SettingsOption(icon = R.drawable.ic_privacy, text = "Privacy Policy")
        SettingsOption(icon = R.drawable.ic_signout, text = "Sign Out")
      }
    }
  }
}

@Composable
fun SettingsOption(icon: Int, text: String, isDarkModeOption: Boolean = false) {
  val interactionSource = remember { MutableInteractionSource() }
  val isTablet = DeviceType.isTablet()
  Row(
    modifier =
    if (isTablet)
        Modifier
            .width(600.dp)
            .height(55.dp)
            .clickable(
                onClick = {},
                interactionSource = interactionSource,
                indication = rememberRipple()
            )
            .padding(horizontal = 16.dp)
    else
        Modifier
            .fillMaxWidth()
            .height(55.dp)
            .clickable(
                onClick = {},
                interactionSource = interactionSource,
                indication = rememberRipple()
            )
            .padding(horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      modifier = Modifier
          .wrapContentWidth()
          .fillMaxHeight(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Image(
        painterResource(id = icon),
        contentDescription = text,
        modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.width(16.dp))
      Text(
        text, style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 16.sp
        )
      )
    }
    if (isDarkModeOption)
      Switch(checked = true, onCheckedChange = {})
    else
      Icon(
        painter = painterResource(id = R.drawable.ic_forward), contentDescription = "open option",
        tint = Color.LightGray
      )
  }
}
