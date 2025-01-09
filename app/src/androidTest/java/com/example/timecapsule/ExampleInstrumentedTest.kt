package com.example.timecapsule

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.timecapsule.util.Device
import com.example.timecapsule.util.DeviceType.getDeviceType
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
  @get:Rule
  val composeTestRule = createComposeRule()

  @Test
  fun getDeviceType_ReturnsDeviceTypeTablet_WhenScreenWidthIsGreaterThan600Dp() {
    composeTestRule.setContent {
      val mockConfiguration = LocalConfiguration.current
      mockConfiguration.screenWidthDp = 700

      CompositionLocalProvider(LocalConfiguration provides mockConfiguration) {
        val deviceType = getDeviceType()
        assertEquals(Device.TABLET, deviceType)
      }
    }
  }

  @Test
  fun getDeviceType_ReturnsDeviceTypeMobile_WhenScreenWidthIsLessThan600Dp() {
    composeTestRule.setContent {
      val mockConfiguration = LocalConfiguration.current
      mockConfiguration.screenWidthDp = 500

      CompositionLocalProvider(LocalConfiguration provides mockConfiguration) {
        val deviceType = getDeviceType()
        assertEquals(Device.MOBILE, deviceType)
      }
    }
  }
}
