package com.example.timecapsule.ui.findcapsule

import CapsuleImage
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.model.NearByCapsule
import com.example.timecapsule.BuildConfig
import com.example.timecapsule.R
import com.example.timecapsule.ui.selectlocation.SearchBar
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.openSansExtraBold
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.ui.util.searchPlace
import com.example.timecapsule.viewmodel.Load3dModelState
import com.example.util.getModelImage
import com.example.util.getModelMapIcon
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.firebase.Timestamp
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.mapbox.maps.extension.style.expressions.dsl.generated.mod
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.delay

// Migrated to mapbox mobile sdk from google maps sdk.

@Composable
fun FindCapsuleScreen() {
  if (!Places.isInitialized()) {
    Places.initialize(LocalContext.current, BuildConfig.MAPS_API_KEY)
  }
  Scaffold { padding ->
    MyMapWithSearch(Modifier.padding(padding))
  }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun MyMapWithSearch(modifier: Modifier = Modifier) {
  val context = LocalContext.current
  val placesClient = remember { Places.createClient(context) }

  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(
      LatLng(13.68933946746576, 74.65930785825766), 17.0f
    )
  }


  val isSatelliteView = remember { mutableStateOf(false) }
  var markerPosition by remember { mutableStateOf(LatLng(13.68933946746576, 74.65930785825766)) }
  var markerState by remember {
    mutableStateOf(
      MarkerState(
        LatLng(
          13.68933946746576,
          74.65930785825766
        )
      )
    )
  }

  val capsuleMarkerList = remember {
    mutableStateListOf<MarkerState>()
  }

  val tempMarkerState = MarkerState(
    LatLng(
      13.68993946746576,
      74.65930785825766
    )
  )

  capsuleMarkerList.add(
    tempMarkerState
  )

  var selectedMarkerInfo by remember {
    mutableStateOf(CapsuleImage("", 0))
  }

  var isMarkerSelected by remember {
    mutableStateOf(false)
  }

// Marker selected dialog.
  AnimatedVisibility(
    visible = isMarkerSelected,
    enter = fadeIn(animationSpec = tween(durationMillis = 500)) + slideInVertically(initialOffsetY = { it / 2 }),
    exit = fadeOut(animationSpec = tween(durationMillis = 500)) + slideOutVertically(targetOffsetY = { it / 2 })
  ) {
    ShowDialog() {
      isMarkerSelected = false
    }
  }

  Box(modifier = modifier.fillMaxSize()) {
    SearchBar(isSatelliteView) { query ->
      searchPlace(placesClient, query) { latLng ->
        if (latLng != null) {
          markerPosition = latLng
          markerState = MarkerState(latLng)

          // Move camera to the new position with the maximum zoom level
          cameraPositionState.move(
            CameraUpdateFactory.newLatLngZoom(latLng, 10.0f)
          )
        }
      }
    }
    GoogleMap(
      modifier = Modifier.fillMaxSize(),
      cameraPositionState = cameraPositionState,
      uiSettings = MapUiSettings(zoomControlsEnabled = true),
      properties = if (isSatelliteView.value) {
        MapProperties(mapType = MapType.HYBRID)
      } else {
        MapProperties(mapType = MapType.TERRAIN)
      }
    ) {
      Marker(
        state = markerState,
        contentDescription = "${markerState.position.latitude}, ${markerState.position.longitude}"
      )

      capsuleMarkerList.forEach { tmarkerState ->
        Marker(
          state = tmarkerState,
          icon = getScaledBitmapDescriptor(
            LocalContext.current,
            R.drawable.capsule_image10,
            200,
            200
          ),
          contentDescription = "${markerState.position.latitude}, ${markerState.position.longitude}",
          onClick = {
            isMarkerSelected = true
            selectedMarkerInfo = CapsuleImage("dhuwhde", 0)
            true
          }
        )
      }
    }
  }
}

fun getScaledBitmapDescriptor(
  context: Context,
  drawableResId: Int,
  width: Int,
  height: Int
): BitmapDescriptor {
  // Load the image from resources
  val bitmap = BitmapFactory.decodeResource(context.resources, drawableResId)

  // Scale the bitmap to the desired size
  val scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)

  // Return a BitmapDescriptor for the scaled bitmap
  return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
}

@Preview
@Composable
fun ShowDialog(
  selectedCapsule: NearByCapsule? = null,
  modelState: Load3dModelState = Load3dModelState.Idle,
  closeDialog: () -> Unit = {},
  openCapsule: () -> Unit = {},
  viewAr: () -> Unit = {}, load3dModel: () -> Unit = {}
) {
  val isTablet = DeviceType.isTablet()

  val context = LocalContext.current

  val path: String = remember {
    getModelMapIcon(selectedCapsule?.modelId)
  }

  val imageBitmap: Bitmap? = remember(path) {
    try {
      val inputStream = context.assets.open(path)
      BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
      null
    }
  }

  var remainingTime by remember { mutableStateOf("") }
  var isReadyToOpen by remember {
    mutableStateOf(false)
  }

  LaunchedEffect(modelState) {
    if (modelState is Load3dModelState.Success)
      viewAr()

  }

  LaunchedEffect(selectedCapsule?.time) {
    while (true) {
      val currentTime = Timestamp.now()
      val diffInMillis = selectedCapsule?.time?.toDate()?.time?.minus(currentTime.toDate().time)

      if (diffInMillis != null) {
        if (diffInMillis > 0) {
          val daysLeft = diffInMillis.let { TimeUnit.MILLISECONDS.toDays(it) }
          val hoursLeft = diffInMillis.let { TimeUnit.MILLISECONDS.toHours(it) } % 24
          val minutesLeft = diffInMillis.let { TimeUnit.MILLISECONDS.toMinutes(it) } % 60
          val secondsLeft = diffInMillis.let { TimeUnit.MILLISECONDS.toSeconds(it) } % 60

          if (isReadyToOpen)
            isReadyToOpen = false

          remainingTime = when {
            daysLeft > 0 -> {
              if (daysLeft > 100) {
              }
              "${daysLeft}d"
            } // If more than 1 day is left, show in days
            hoursLeft > 0 -> {
              if (hoursLeft < 24) {

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
      }
      delay(1000) // Update every second
    }
  }

  Dialog(onDismissRequest = { closeDialog() }) {
    Box(
      modifier = Modifier
          .fillMaxWidth()
          .clip(shape = RoundedCornerShape(30.dp))
          .background(MaterialTheme.colorScheme.primary)
          .padding(16.dp)
          .fillMaxWidth(0.8f)
          .wrapContentHeight()
    ) {
      Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        // Close Button
        IconButton(
          onClick = { closeDialog() },
          modifier = Modifier
              .align(Alignment.End)
              .padding(bottom = 16.dp)
        ) {
          Icon(Icons.Filled.Close, contentDescription = "Close")
        }

        imageBitmap?.asImageBitmap()?.let {
          Image(
            bitmap = it,
            contentDescription = null,
            modifier = if (isTablet) {
                Modifier
                    .fillMaxSize(0.5f)
                    .padding(bottom = 16.dp)
                    .size(200.dp)
            } else {
                Modifier
                    .padding(bottom = 16.dp)
                    .size(200.dp)
            }
          )
        }
        Text(
          text = remainingTime,
          style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 33.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = openSansExtraBold
          )
        )
        Text(
          text = selectedCapsule?.capsuleTitle ?: "No information",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold
          ),
          modifier = Modifier.padding(bottom = 16.dp, top = 10.dp)
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          OutlinedButton(
            onClick = {
              if (!(modelState is Load3dModelState.Success))
                load3dModel()
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
          ) {

            if (modelState is Load3dModelState.Idle) {
              Icon(
                painter = painterResource(id = com.example.timecapsule.R.drawable.icon_view_in_ar),
                contentDescription = "ar icon",
                tint = LightBlue,
                modifier = Modifier.padding(horizontal = 2.dp)
              )
              Text(
                "View AR",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
            if (modelState is Load3dModelState.Loading)
              Text(
                "loading..",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )

            if (modelState is Load3dModelState.Error)
              Text(
                "can't load",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
          }
          Button(
            onClick = {
              if (isReadyToOpen)
                openCapsule()
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
          ) {
            Text(
              "Open",
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}
