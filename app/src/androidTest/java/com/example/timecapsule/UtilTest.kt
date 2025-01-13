package com.example.timecapsule

import android.content.Context
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.timecapsule.util.Device
import com.example.timecapsule.util.DeviceType.getDeviceType
import com.example.timecapsule.util.searchPlace
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class UtilTest {

  @get:Rule
  var hiltRule = HiltAndroidRule(this)

  @Before
  fun init() {
    hiltRule.inject()
  }

  @Inject
  @ApplicationContext
  lateinit var context: Context

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

  @Test
  fun searchPlace_ReturnCoordinates_WhenQueryPassed() {
    Places.initialize(context, BuildConfig.MAPS_API_KEY)
    val placeClient = Places.createClient(context)
    val query = "New York"

    // Use CountDownLatch to wait for the async response
    val latch = CountDownLatch(1)
    var result: LatLng? = null

    searchPlace(placeClient, query) {
      result = it
      latch.countDown()  // Count down to indicate the result has been received
    }

    // Wait for the latch to finish
    latch.await(10, TimeUnit.SECONDS)

    assertTrue(result != null)
  }
}
