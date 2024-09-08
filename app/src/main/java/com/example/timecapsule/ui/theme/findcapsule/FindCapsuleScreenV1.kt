package com.example.timecapsule.ui.theme.findcapsule

import android.Manifest
import android.animation.Animator
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.timecapsule.BuildConfig
import com.example.timecapsule.R
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
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
import com.mapbox.maps.extension.compose.annotation.generated.PointAnnotation
import com.mapbox.maps.extension.compose.style.DoubleListValue
import com.mapbox.maps.extension.compose.style.DoubleValue
import com.mapbox.maps.extension.compose.style.layers.ModelIdValue
import com.mapbox.maps.extension.compose.style.layers.generated.ModelLayer
import com.mapbox.maps.extension.compose.style.layers.generated.ModelTypeValue
import com.mapbox.maps.extension.compose.style.sources.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.rememberGeoJsonSourceState
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.layers.generated.ModelLayer
import com.mapbox.maps.extension.style.model.addModel
import com.mapbox.maps.extension.style.model.model
import com.mapbox.maps.plugin.animation.CameraAnimatorOptions.Companion.cameraAnimatorOptions
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.MapAnimationOptions.Companion.mapAnimationOptions
import com.mapbox.maps.plugin.animation.animator.CameraAnimator
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.AnnotationType
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.PolylineAnnotation
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

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
    // Request location permissions if not granted
    ActivityCompat.requestPermissions(
      context as ComponentActivity,
      arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
      1
    )
    return
  }

  // Fetch the last known location asynchronously
  LocationServices.getFusedLocationProviderClient(context).lastLocation.addOnSuccessListener { location ->
    if (location != null) {
      val point = Point.fromLngLat(location.longitude, location.latitude)
      onLocationFetched(point)
    } else {
      onLocationFetched(Point.fromLngLat(0.0, 0.0))
    }
  }.addOnFailureListener { exception ->
    onLocationFetched(Point.fromLngLat(0.0, 0.0))
  }
}

@Composable
fun FindCapsuleScreenV1() {

  var locationTypeState by remember { mutableStateOf(LOCATIONTYPE.USER_LOCATION) }

  var flagCapsuleLocationType by remember {
    mutableStateOf(false)
  }
  var userLocationPoint by remember {
    mutableStateOf<Point?>(null)
  }

  var initialCameraPoint = Point.fromLngLat(0.0, 0.0)

  val context = LocalContext.current
  val initialCamera = rememberMapViewportState {
    (setCameraOptions {
      zoom(17.0)
      center(initialCameraPoint)
      pitch(60.0)
      bearing(16.0)
    })
  }

  getInitialLocation(context) {
    initialCameraPoint = it
    userLocationPoint = it
    animateCamera(initialCamera = initialCamera, center = it)
  }

//    Point.fromLngLat(74.65932213633747, 13.68945525955631)

  Scaffold(floatingActionButton = {
    VerticalFABs { locationType ->
      locationTypeState = locationType
    }
  }) { innerPadding ->
    LaunchedEffect(locationTypeState, userLocationPoint) {
      if (locationTypeState == LOCATIONTYPE.USER_LOCATION) {
        animateCamera(initialCamera, userLocationPoint ?: initialCameraPoint)
        flagCapsuleLocationType = false
      } else if (!flagCapsuleLocationType) {
        flagCapsuleLocationType = true
        animateCamera(initialCamera, MODEL1_COORDINATES)
      }
    }
    MapView(
      Modifier.padding(innerPadding), initialCamera, initialCameraPoint, context,
      userLocationPoint
    ) {
      userLocationPoint = it
    }
  }
}

val MODEL_ID_1 = "model-id-1"

//  val SAMPLE_MODEL_URI_1 =
//    "asset://testmodel2folder/scene.gltf"
val SAMPLE_MODEL_URI_1 =
  "asset://testmodel.glb"
val MODEL1_COORDINATES: Point =
  Point.fromLngLat(74.65992213633747, 13.68945525955631)

val MODEL_ID_KEY = "model-id-key"

@OptIn(MapboxExperimental::class)
@Composable
fun MapView(
  modifier: Modifier = Modifier,
  cameraView: MapViewportState, initialPoint: Point, context: Context,
  locationPoint: Point?, updateLocation: (Point) -> Unit
) {
  var pointAnnotationManager by remember { mutableStateOf<PointAnnotationManager?>(null) }

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
      mapView.mapboxMap.loadStyle(BuildConfig.STYLE_URI)

      startLocationUpdates(context) {
        updateLocation(Point.fromLngLat(it.longitude, it.latitude))
      }

      pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
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

  LaunchedEffect(locationPoint) {
    AddPointer(
      context = context,
      point = locationPoint,
      pointAnnotationManager = pointAnnotationManager
    )
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
    modifier = Modifier.padding(16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
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

fun animateCamera(initialCamera: MapViewportState, center: Point) {
  val mapAnimationOptions = MapAnimationOptions.Builder().duration(2500L).build()
  initialCamera.flyTo(
    CameraOptions.Builder()
      .zoom(17.0)
      .center(center)
      .pitch(60.0)
      .bearing(16.0)
      .build(),
    mapAnimationOptions
  )
}

fun AddPointer(context: Context, point: Point?, pointAnnotationManager: PointAnnotationManager?) {

  pointAnnotationManager?.deleteAll()

  val drawable = ResourcesCompat.getDrawable(context.resources, R.drawable.direction, null)

  drawable?.let {
    val bitmap = it.toBitmap(70, 70, Bitmap.Config.ARGB_8888)

    val annotationOptions = point?.let { it1 ->
      PointAnnotationOptions()
        .withPoint(it1)
        .withIconImage(bitmap)
    }

    if (annotationOptions != null) {
      pointAnnotationManager?.create(annotationOptions)
    }
  } ?: run {
  }
}

fun startLocationUpdates(context: Context, onLocationUpdate: (Location) -> Unit) {
  val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
  val locationRequest = LocationRequest.create().apply {
    interval = 1000
    fastestInterval = 1000
    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
  }

  val locationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
      locationResult.let {
        val location = it.lastLocation
        if (location != null) {
          onLocationUpdate(location)
        }
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
    // Request permissions
    ActivityCompat.requestPermissions(
      context as ComponentActivity,
      arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
      1
    )
  }

//  val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//  if (ActivityCompat.checkSelfPermission(
//      context,
//      Manifest.permission.ACCESS_FINE_LOCATION
//    ) == PackageManager.PERMISSION_GRANTED
//  ) {
//    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10L, 1f) { location ->
//      onLocationUpdate(location)
//    }
//  }
}
