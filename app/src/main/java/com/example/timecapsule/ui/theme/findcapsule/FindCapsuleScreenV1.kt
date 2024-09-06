package com.example.timecapsule.ui.theme.findcapsule

import android.animation.Animator
import android.util.Log
import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.timecapsule.R
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.RenderedQueryGeometry
import com.mapbox.maps.ScreenCoordinate
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.DoubleListValue
import com.mapbox.maps.extension.compose.style.DoubleValue
import com.mapbox.maps.extension.compose.style.layers.ModelIdValue
import com.mapbox.maps.extension.compose.style.layers.generated.ModelLayer
import com.mapbox.maps.extension.compose.style.layers.generated.ModelTypeValue
import com.mapbox.maps.extension.compose.style.sources.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.rememberGeoJsonSourceState
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.model.addModel
import com.mapbox.maps.extension.style.model.model
import com.mapbox.maps.plugin.animation.CameraAnimatorOptions.Companion.cameraAnimatorOptions
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.animator.CameraAnimator
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.formatDuration


@Composable
fun FindCapsuleScreenV1() {

  val initialCameraPoint =
    Point.fromLngLat(74.65932213633747, 13.68945525955631)

  var initialCamera = rememberMapViewportState {
    mutableStateOf(setCameraOptions {
      zoom(17.0)
      center(initialCameraPoint)
      pitch(60.0)
      bearing(16.0)
    })
  }

  var newCameraPoint =
    rememberMapViewportState {
      mutableStateOf(setCameraOptions {
        zoom(17.0)
        center(initialCameraPoint)
        pitch(60.0)
        bearing(16.0)
      })
    }


  Scaffold(floatingActionButton = {
    VerticalFABs() {
      when (it) {
        LOCATIONTYPE.USER_LOCATION -> {
          initialCamera.setCameraOptions {
            zoom(17.0)
            center(initialCameraPoint)
            pitch(60.0)
            bearing(16.0)
          }
        }

        LOCATIONTYPE.CAPSULE_LOCATION -> {
          initialCamera.setCameraOptions {
            zoom(17.0)
            center(MODEL1_COORDINATES)
            pitch(60.0)
            bearing(16.0)
          }
        }
      }

    }
  }) { innerPadding ->
    MapView(Modifier.padding(innerPadding), initialCamera)
  }
}

val MODEL_ID_1 = "model-id-1"

//  val SAMPLE_MODEL_URI_1 =
//    "asset://testmodel2folder/scene.gltf"
val SAMPLE_MODEL_URI_1 =
  "asset://testmodel.glb"
val MODEL1_COORDINATES: Point =
  Point.fromLngLat(77.69932213633747, 13.68945525955631)

val MODEL_ID_KEY = "model-id-key"

@OptIn(MapboxExperimental::class)
@Composable
fun MapView(
  modifier: Modifier = Modifier,
  cameraView: MapViewportState,
) {
  var is3dModelSelected by remember {
    mutableStateOf(false)
  }

  if (is3dModelSelected)
    ShowDialog() {
      is3dModelSelected = false
    }

  MapboxMap(
    modifier.fillMaxSize(),
    mapViewportState = cameraView,
    onMapClickListener = { point ->
      if (is3dModelClicked(point)) {
        is3dModelSelected = true
      }
      true
    }
  ) {
    MapEffect(Unit) { mapView ->
      mapView.mapboxMap.apply {
        addModel(model(MODEL_ID_1) { uri(SAMPLE_MODEL_URI_1) })
      }
      mapView.mapboxMap.loadStyle("mapbox://styles/dark-vish1/cm0pf1i3z00iz01qughq744ex")
    }

    ModelLayer(
      sourceState = rememberGeoJsonSourceState {
        data = GeoJSONData(
          listOf(
            Feature.fromGeometry(MODEL1_COORDINATES)
              .also { it.addStringProperty(MODEL_ID_KEY, MODEL_ID_1) },
          )
        )
      }
    ) {
      modelId = ModelIdValue(Expression.get(MODEL_ID_KEY))
      modelType = ModelTypeValue.LOCATION_INDICATOR
      modelScale = DoubleListValue(listOf(10.0, 10.0, 10.0))
      modelTranslation = DoubleListValue(listOf(0.0, 0.0, 0.0))
      modelRotation = DoubleListValue(listOf(0.0, 0.0, 90.0))
      modelOpacity = DoubleValue(1.0)
      modelAmbientOcclusionIntensity = DoubleValue(1.0)
    }
  }


}

fun is3dModelClicked(
  point: Point,
  modelCoordinates: Point = MODEL1_COORDINATES,
  radiusInMeters: Double = 10.0
): Boolean {
  // Function to calculate distance between two geographical points
  fun calculateDistance(point1: Point, point2: Point): Double {
    val earthRadius = 6371e3 // Earth's radius in meters
    val lat1 = Math.toRadians(point1.latitude())
    val lon1 = Math.toRadians(point1.longitude())
    val lat2 = Math.toRadians(point2.latitude())
    val lon2 = Math.toRadians(point2.longitude())

    val dLat = lat2 - lat1
    val dLon = lon2 - lon1

    val a = sin(dLat / 2).pow(2) +
      cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadius * c
  }

  // Calculate distance between the point and model coordinates
  val distance = calculateDistance(point, modelCoordinates)

  // Check if the distance is within the specified radius
  return distance <= radiusInMeters
}

@Composable
fun VerticalFABs(onClick: (type: LOCATIONTYPE) -> Unit) {
  Column(
    modifier = Modifier.padding(16.dp), // Padding to keep some space from the screen edges
    verticalArrangement = Arrangement.spacedBy(16.dp) // Space between the two FABs
  ) {
    FloatingActionButton(
      onClick = { onClick(LOCATIONTYPE.CAPSULE_LOCATION) },
    ) {
      Image(
        painter = painterResource(id = R.drawable.testimg),
        modifier = Modifier.size(30.dp),
        contentDescription = "point capsule"
      )
    }

    FloatingActionButton(
      onClick = { onClick(LOCATIONTYPE.USER_LOCATION) },
    ) {
      Icon(
        painter = painterResource(id = R.drawable.ic_location_search),
        modifier = Modifier.size(30.dp),
        contentDescription = "point location"
      )
    }
  }
}

enum class LOCATIONTYPE {
  CAPSULE_LOCATION,
  USER_LOCATION
}
