package com.example.timecapsule.ui.theme.findcapsule

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Point
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.RenderedQueryGeometry
import com.mapbox.maps.ScreenCoordinate
import com.mapbox.maps.Style
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
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
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


@Composable
fun FindCapsuleScreenV1() {
  Scaffold { innerPadding ->

    MapView(Modifier.padding(innerPadding))
  }
}

val MODEL_ID_1 = "model-id-1"

//  val SAMPLE_MODEL_URI_1 =
//    "asset://testmodel2folder/scene.gltf"
val SAMPLE_MODEL_URI_1 =
  "asset://testmodel.glb"
val MODEL1_COORDINATES: Point =
  Point.fromLngLat(74.659890785825766, 13.68933946746576)

val MODEL_ID_KEY = "model-id-key"

@OptIn(MapboxExperimental::class)
@Composable
fun MapView(
  modifier: Modifier = Modifier
) {

  var is3dModelSelected by remember {
    mutableStateOf(false)
  }

  if (is3dModelSelected)
    ShowDialog() {
      is3dModelSelected = false
    }

  MapboxMap(
    Modifier.fillMaxSize(),
    mapViewportState = rememberMapViewportState {
      setCameraOptions {
        zoom(15.0)
        center(Point.fromLngLat(74.65930785825766, 13.68933946746576))
        pitch(40.0)
        bearing(16.0)
      }
    },
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