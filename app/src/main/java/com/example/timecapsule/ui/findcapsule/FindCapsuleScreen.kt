package com.example.timecapsule.ui.findcapsule

import CapsuleImage
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.model.NearByCapsule
import com.example.timecapsule.BuildConfig
import com.example.timecapsule.R
import com.example.timecapsule.ui.selectlocation.SearchBar
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.ui.util.searchPlace
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

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
  closeDialog: () -> Unit = {},
  openCapsule: () -> Unit = {}
) {
  val isTablet = DeviceType.isTablet()
  Dialog(onDismissRequest = { closeDialog() }) {
    Box(
      modifier = Modifier
          .fillMaxWidth()
          .clip(shape = RoundedCornerShape(10.dp))
          .background(MaterialTheme.colorScheme.primary)
          .padding(16.dp)
          .fillMaxWidth(0.8f)
          .wrapContentHeight()
    ) {
      Column(
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

        // Image
        AsyncImage(
          model = selectedCapsule?.capsuleImageUrl,
          contentDescription = null,
          modifier = if (isTablet) {
              Modifier
                  .fillMaxSize(0.5f)
                  .padding(bottom = 16.dp)
          } else {
            Modifier.padding(bottom = 16.dp)
          }
        )

        Text(
          text = selectedCapsule?.capsuleTitle ?: "No information",
          style = MaterialTheme.typography.titleLarge,
          modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically,
        ) {
          OutlinedButton(
            onClick = {
            },
            colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
          ) {
            Text(
              "View",
              style = MaterialTheme.typography.titleSmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Button(
            onClick = {
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
