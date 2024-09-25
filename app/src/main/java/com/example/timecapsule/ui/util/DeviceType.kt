package com.example.timecapsule.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

object DeviceType {
  @Composable
  fun getDeviceType(): Device {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val isTablet = screenWidthDp > 600.dp
    if (isTablet) return Device.TABLET
    return Device.MOBILE
  }

  @Composable
  fun isTablet(): Boolean {
    if (getDeviceType() == Device.TABLET) return true
    return false
  }
}

enum class Device {
  TABLET,
  MOBILE
}
