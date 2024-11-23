package com.example.timecapsule.ui.nearbycapsules

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.timecapsule.BuildConfig
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun NearbyCapsulesScreen() {
  val context = LocalContext.current
  val initialCamera = rememberMapViewportState {
    setCameraOptions {
      zoom(16.0)
      center(Point.fromLngLat(0.0, 0.0))
      pitch(0.0)
      bearing(0.0)
    }
  }
  var pointAnnotationManager by remember { mutableStateOf<PointAnnotationManager?>(null) }

  val nearbyCapsules = remember { mutableStateOf<List<Point>>(emptyList()) }
  val userLocation = remember { mutableStateOf<Point?>(null) }
  val radiusMeters = remember { mutableStateOf(20000.0) }

  getInitialLocation(context) { location ->
    userLocation.value = location
    initialCamera.flyTo(CameraOptions.Builder().center(location).zoom(16.0).build())
  }

  LaunchedEffect(Unit) {
    startLocationUpdates(context) { location ->
      val updatedPoint = Point.fromLngLat(location.longitude, location.latitude)

      userLocation.value = updatedPoint
      nearbyCapsules.value = fetchNearbyCapsules(updatedPoint, radiusMeters.value)
    }
  }

  Scaffold { innerPadding ->
    MapboxMap(
      modifier = Modifier
        .fillMaxSize(),
      mapViewportState = initialCamera,
      onMapClickListener = { clickedPoint ->
        false
      }
    ) {
      MapEffect(this) { mapView ->
        mapView.mapboxMap.apply {
          loadStyle(BuildConfig.STYLE_URI)
        }
        pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
      }

      // Add capsule location markers.
      AddPointer(
        isUser = false,
        context,
        points = nearbyCapsules.value, pointAnnotationManager = pointAnnotationManager
      )

      // Add user location marker
      userLocation.value?.let { location ->
        AddPointer(
          isUser = true,
          context,
          points = listOf(location),
          pointAnnotationManager = pointAnnotationManager
        )
      }
    }
  }
}

fun calculateDistance(point1: Point, point2: Point): Double {
  val earthRadius = 6371e3 // Radius in meters
  val lat1 = Math.toRadians(point1.latitude())
  val lon1 = Math.toRadians(point1.longitude())
  val lat2 = Math.toRadians(point2.latitude())
  val lon2 = Math.toRadians(point2.longitude())

  val dLat = lat2 - lat1
  val dLon = lon2 - lon1

  val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
  val c = 2 * atan2(sqrt(a), sqrt(1 - a))

  return earthRadius * c
}

fun AddPointer(
  isUser: Boolean = false,
  context: Context,
  pointAnnotationManager: PointAnnotationManager?,
  points: List<Point> = emptyList()
) {

  if (isUser) {
    val annotation = pointAnnotationManager?.annotations?.filter {
      it.textField == "user_location"
    }
    if (annotation != null) {
      pointAnnotationManager.delete(annotation)
    }
  } else {
    val annotations = pointAnnotationManager?.annotations?.filter {
      it.textField == "capsule_location"
    }
    if (annotations != null) {
      pointAnnotationManager.delete(annotations)
    }
  }

  val drawable = ResourcesCompat.getDrawable(
    context.resources,
    com.example.timecapsule.R.drawable.direction,
    null
  )
  drawable?.let {
    val bitmap = it.toBitmap(70, 70, Bitmap.Config.ARGB_8888)
    points.forEach {
      val annotationOptions = PointAnnotationOptions()
        .withPoint(it)
        .withIconImage(bitmap)
      if (isUser)
        annotationOptions.withTextField("user_location")
      else
        annotationOptions.withTextField("capsule_location")

      annotationOptions.textOpacity = 0.0
      pointAnnotationManager?.create(annotationOptions)
    }
  }
}

fun fetchNearbyCapsules(center: Point, radius: Double): List<Point> {
  val mockCapsules = listOf(
    Point.fromLngLat(74.059386, 13.679263),
    Point.fromLngLat(74.959386, 13.679263),
    Point.fromLngLat(74.859386, 13.679263),
    Point.fromLngLat(74.659386, 13.679263),
    Point.fromLngLat(74.559386, 13.679263),
    Point.fromLngLat(74.659386, 13.679263),
  )
  return mockCapsules.filter { capsulePoint ->
    val distance = calculateDistance(center, capsulePoint)
    distance <= radius
  }
}

fun getInitialLocation(context: Context, onLocationFetched: (Point) -> Unit) {
  if (ActivityCompat.checkSelfPermission(
      context,
      Manifest.permission.ACCESS_FINE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED &&
    ActivityCompat.checkSelfPermission(
      context,
      Manifest.permission.ACCESS_COARSE_LOCATION
    ) != PackageManager.PERMISSION_GRANTED
  ) {
    ActivityCompat.requestPermissions(
      context as ComponentActivity,
      arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
      1
    )
    return
  }

  LocationServices.getFusedLocationProviderClient(context).lastLocation
    .addOnSuccessListener { location ->
      if (location != null) {
        val point = Point.fromLngLat(location.longitude, location.latitude)
        onLocationFetched(point)
      } else {
        onLocationFetched(Point.fromLngLat(0.0, 0.0))
      }
    }
    .addOnFailureListener {
      onLocationFetched(Point.fromLngLat(0.0, 0.0))
    }
}

fun startLocationUpdates(context: Context, onLocationUpdate: (Location) -> Unit) {
  val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
  val locationRequest = LocationRequest.create().apply {
    interval = 2000
    fastestInterval = 1000
    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
  }

  val locationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
      locationResult.lastLocation?.let { location ->
        onLocationUpdate(location)
      }
    }
  }

  if (ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED
  ) {
    fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
  } else {
    ActivityCompat.requestPermissions(
      context as ComponentActivity,
      arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
      1
    )
  }
}
