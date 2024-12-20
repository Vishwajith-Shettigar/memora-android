package com.example.timecapsule.ui.nearbycapsules

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.model.NearByCapsule
import com.example.timecapsule.BuildConfig
import com.example.timecapsule.R
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.findcapsule.ShowDialog
import com.example.timecapsule.ui.findcapsule.VerticalFABs
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.openSansExtraBold
import com.example.timecapsule.viewmodel.NearByCapsulesViewModel
import com.example.util.getModelMapIcon
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.type.LatLng
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.style.expressions.dsl.generated.mod
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

  var isSheetVisible by remember { mutableStateOf(false) }


  Scaffold(
    floatingActionButton = {
      FloatingActionButton(onClick = { isSheetVisible = true }) {
        Image(
          painter = painterResource(id = R.drawable.testimg),
          modifier = Modifier.size(30.dp),
          contentDescription = "point capsule"
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
    ) {
      MapboxMap(
        modifier = Modifier
            .align(Alignment.Center)
            .zIndex(0.0F)
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
      NearByCapsulesBottomSheet(isSheetVisible) { isSheetVisible = false }
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
    annotationOptions.iconSize = 1.2
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
      null
    }

    iconBitmap?.let {
      val annotationOptions = PointAnnotationOptions()
        .withPoint(Point.fromLngLat(capsule.location.longitude, capsule.location.latitude))
        .withIconImage(iconBitmap)
      annotationOptions.iconSize = 0.4
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NearByCapsulesBottomSheet(isSheetVisible: Boolean, onDismiss: () -> Unit) {
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  if (isSheetVisible) {
    ModalBottomSheet(
      containerColor = MaterialTheme.colorScheme.primary,
      onDismissRequest = { onDismiss() },
      sheetState = sheetState
    ) {
      Column(
          Modifier
              .fillMaxWidth()
              .background(MaterialTheme.colorScheme.primary)
              .padding(vertical = 10.dp, horizontal = 3.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
      ) {
        Text(
          text = "Near by capsules",
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(15.dp))

        LazyColumn(
          Modifier
            .fillMaxWidth()
        ) {
          items(10)
          {
            CapsuleRow()
            Spacer(Modifier.height(15.dp))
          }
        }
      }
    }
  }
}

@Composable
fun CapsuleRow() {
  val context = LocalContext.current
  val iconBitmap = try {
    val inputStream = context.assets.open(getModelMapIcon("100"))
    BitmapFactory.decodeStream(inputStream)
  } catch (e: Exception) {
    null
  }
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = 10.dp)
        .clip(RoundedCornerShape(20.dp))
        .background(LightBlue.copy(alpha = 0.6F))
        .padding(horizontal = 10.dp, vertical = 15.dp),
    horizontalArrangement = Arrangement.Start,
    verticalAlignment = Alignment.CenterVertically
  ) {

    iconBitmap?.asImageBitmap()?.let {
      Image(
        bitmap = it,
        contentDescription = "capsule",
        modifier = Modifier
            .weight(0.2F)
            .size(60.dp)
      )
    }

    Column(
      modifier = Modifier
          .weight(0.6F)
          .wrapContentHeight(),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.Start
    ) {
      Text(
        text = "The Family",
        style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 20.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontFamily = openSansExtraBold
        )
      )
      Text(
        text = "Near by capsules ,lore ipsum i had to be honest i dont vebn know hat to domso ou hjusta shity uop.",
        style = MaterialTheme.typography.titleSmall.copy(
          fontSize = 10.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,

          )
      )
    }

    Row(
      modifier = Modifier
        .weight(0.1F), horizontalArrangement = Arrangement.End,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "0.5m", style = MaterialTheme.typography.titleSmall.copy(
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      )
    }
  }
}
