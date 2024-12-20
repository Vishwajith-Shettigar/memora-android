package com.example.timecapsule.ui.nearbycapsules

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.model.NearByCapsule
import com.example.timecapsule.BuildConfig
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.findcapsule.ShowDialog
import com.example.timecapsule.viewmodel.NearByCapsulesViewModel
import com.example.util.getModelMapIcon
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.type.LatLng
import com.mapbox.geojson.Feature
import com.mapbox.geojson.Geometry
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
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
fun NearbyCapsulesScreen(
  viewModel: NearByCapsulesViewModel = hiltViewModel(),
  navigate: (String) -> Unit = {},
) {
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

  val nearbyCapsules = remember { mutableStateOf<List<NearByCapsule>>(emptyList()) }
  val userLocation = remember { mutableStateOf<Point?>(null) }
  val radiusMeters = remember { 100.0 }

  var selectedCapsule by remember { mutableStateOf<NearByCapsule?>(null) }

  var isCapsuleSelected by remember {
    mutableStateOf(false)
  }

  fun getCapsuleDetails(capsuleId: String): NearByCapsule {
    return nearbyCapsules.value.filter {
      it.capsuleId == capsuleId
    }[0]
  }

  getInitialLocation(context) { location ->
    userLocation.value = location
    initialCamera.flyTo(CameraOptions.Builder().center(location).zoom(16.0).build())
    viewModel.fetchNearByCapsules(
      location = LatLng.newBuilder().apply {
        latitude = location.latitude()
        longitude = location.longitude()
      }.build(),
      radius = radiusMeters,
    ) {
      nearbyCapsules.value = it
    }
  }

  LaunchedEffect(Unit) {
    startLocationUpdates(context) { location ->
      val updatedPoint = Point.fromLngLat(location.longitude, location.latitude)
      userLocation.value = updatedPoint

      viewModel.fetchNearByCapsules(
        location = LatLng.newBuilder().apply {
          latitude = location.latitude
          longitude = location.longitude
        }.build(),
        radius = radiusMeters,
      ) {
        nearbyCapsules.value = it
      }
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

      if (isCapsuleSelected)
        ShowDialog(selectedCapsule, closeDialog = {
          isCapsuleSelected = false
        }, openCapsule = {
          isCapsuleSelected = false
          navigate(
            Screen.OpenCapsuleLoadingScreen.createRoute(
              id = selectedCapsule!!.capsuleId,
              isCapsuleHunt = true
            )
          )
        })

      MapEffect(this) { mapView ->
        mapView.mapboxMap.apply {
          loadStyle(BuildConfig.STYLE_URI)
        }
        pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
      }

      // Add capsule location markers.
      AddPointer(
        context,
        points = nearbyCapsules.value, pointAnnotationManager = pointAnnotationManager
      )

      // Add user location marker
      userLocation.value?.let { location ->
        AddUserPointer(
          context,
          point = location,
          pointAnnotationManager = pointAnnotationManager
        )
      }

      pointAnnotationManager?.addClickListener { annotation ->
        if (!(annotation.textField.toString().equals("user_location"))) {
          if (arePointsWithin10Meters(
              userLocation.value,
              annotation.point
            )
          ) {
            selectedCapsule = getCapsuleDetails(annotation.textField.toString())
            isCapsuleSelected = true
          }
        }
        true
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

fun AddUserPointer(
  context: Context,
  pointAnnotationManager: PointAnnotationManager?, point: Point
) {
  val annotation = pointAnnotationManager?.annotations?.filter {
    it.textField == "user_location"
  }
  if (annotation != null) {
    pointAnnotationManager.delete(annotation)
  }
  val drawable = ResourcesCompat.getDrawable(
    context.resources,
    com.example.timecapsule.R.drawable.direction,
    null
  )
  drawable?.let {
    val bitmap = it.toBitmap(70, 70, Bitmap.Config.ARGB_8888)
    val annotationOptions = PointAnnotationOptions()
      .withPoint(point)
      .withIconImage(bitmap)
    annotationOptions.iconSize=1.2
    annotationOptions.withTextField("user_location")
    annotationOptions.textOpacity = 0.0
    pointAnnotationManager?.create(annotationOptions)
  }
}

@Composable
fun AddPointer(
  context: Context,
  pointAnnotationManager: PointAnnotationManager?,
  points: List<NearByCapsule> = emptyList()
) {


  val annotations = pointAnnotationManager?.annotations?.filter {
    it.textField != "user_location"
  }
  if (annotations != null) {
    pointAnnotationManager.delete(annotations)
  }

  points.forEach { capsule ->
    val iconBitmap = try {
      val inputStream = context.assets.open(getModelMapIcon(capsule.modelId))
      BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
      Log.e("pokemon", "Error loading asset: ${e.toString()}")
      null
    }

    Log.e("pokemon", iconBitmap.toString() + getModelMapIcon(capsule.modelId))
    iconBitmap?.let {
      val annotationOptions = PointAnnotationOptions()
        .withPoint(Point.fromLngLat(capsule.location.longitude, capsule.location.latitude))
        .withIconImage(iconBitmap)
      annotationOptions.iconSize=0.4
      annotationOptions.withTextField(capsule.capsuleId)
      annotationOptions.textOpacity = 0.0
      pointAnnotationManager?.create(annotationOptions)
    }
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

fun arePointsWithin10Meters(locationPoint: Point?, capsulePoint: Point): Boolean {
  // Return false if locationPoint is null, as we cannot perform the check
  if (locationPoint == null) return false

  // Extract the latitude and longitude from each point
  val locationLatitude = locationPoint.latitude()
  val locationLongitude = locationPoint.longitude()
  val capsuleLatitude = capsulePoint.latitude()
  val capsuleLongitude = capsulePoint.longitude()

  // Use Location.distanceBetween to get the distance in meters
  val results = FloatArray(1)
  Location.distanceBetween(
    locationLatitude,
    locationLongitude,
    capsuleLatitude,
    capsuleLongitude,
    results
  )

  // Check if the distance is less than or equal to 10 meters
  return results[0] <= 10
}
