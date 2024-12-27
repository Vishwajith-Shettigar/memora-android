package com.example.timecapsule.ui.findcapsule

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.location.Location
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.model.NearByCapsule
import com.example.timecapsule.BuildConfig
import com.example.timecapsule.R
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.ui.nearbycapsules.calculateDistance
import com.example.timecapsule.ui.util.checkARCoreAvailability
import com.example.timecapsule.viewmodel.DisplayCapsuleDetailsState
import com.example.timecapsule.viewmodel.Load3dModelState
import com.example.timecapsule.viewmodel.OpenCapsuleViewModel
import com.example.util.getModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxExperimental
import com.mapbox.maps.extension.compose.MapEffect
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.MapViewportState
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.DoubleListValue
import com.mapbox.maps.extension.compose.style.DoubleValue
import com.mapbox.maps.extension.compose.style.layers.ModelIdValue
import com.mapbox.maps.extension.compose.style.layers.generated.ModelLayer
import com.mapbox.maps.extension.compose.style.layers.generated.ModelTypeValue
import com.mapbox.maps.extension.compose.style.layers.generated.SymbolLayer
import com.mapbox.maps.extension.compose.style.sources.GeoJSONData
import com.mapbox.maps.extension.compose.style.sources.generated.rememberGeoJsonSourceState
import com.mapbox.maps.extension.style.expressions.generated.Expression
import com.mapbox.maps.extension.style.model.addModel
import com.mapbox.maps.extension.style.model.model
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
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
fun FindCapsuleScreenV1(
  navController: NavController = rememberNavController(),
  viewModel: OpenCapsuleViewModel = hiltViewModel(),
  navigate: (String) -> Unit = {}, onViewAr: (String) -> Unit
) {

  val capsuleDetails = remember {
    (viewModel.capsuleDetailsState.value
      as DisplayCapsuleDetailsState.Success).capsuleDetails
  }

  val loadModelState by viewModel.loadingLoad3dModelState.collectAsState()

  val selectedCapsule = remember {
    NearByCapsule(
      capsuleId = capsuleDetails.id, location = capsuleDetails.location!!,
      capsuleTitle = capsuleDetails.title, capsuleImageUrl = capsuleDetails.imageUrl,
      modelId = capsuleDetails.modelId.toString(), description = capsuleDetails.description,
      time = capsuleDetails.time
    )
  }

  val capsulePoint by remember {
    val lat = capsuleDetails.location!!.latitude
    val longt = capsuleDetails.location!!.longitude
    mutableStateOf<Point>(
      Point.fromLngLat(longt, lat)
    )
  }

  val modelId by remember {
    mutableStateOf(capsuleDetails.modelId)
  }

  val modelUri by remember {
    mutableStateOf(viewModel.modelPath)
  }

  LaunchedEffect(Unit) {
    viewModel.saveScreenCheckPoint(Screen.OpenCapsuleFindCapsuleScreen.route)
  }

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
      zoom(90.0)
      center(initialCameraPoint)
      pitch(360.0)
      bearing(200.0)
    })
  }

  getInitialLocation(context) {
    initialCameraPoint = it
    userLocationPoint = it
    animateCamera(initialCamera = initialCamera, center = it)
  }

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
        animateCamera(initialCamera, capsulePoint)
      }
    }
    MapView(
      Modifier.padding(),
      initialCamera,
      initialCameraPoint,
      context,
      userLocationPoint,
      capsulePoint,
      modelId.toString(),
      modelUri,
      selectedCapsule = selectedCapsule,
      isCapsuleSelected = viewModel.isCapsuleSelected,
      loadModelState = loadModelState,
      navigate = navigate, updateLocation = {
        userLocationPoint = it
      }, onCapsuleSelected = { it ->
        viewModel.isCapsuleSelected = it
      },
      onArViewClicked = {
        onViewAr(selectedCapsule.modelId)
      },
      setModelLoadingStateIdle = {
        viewModel.setModelLoadingStateIdle()
      },
      loadModel = {
        viewModel.loadModel(selectedCapsule.modelId)
      })
  }
}

val MODEL_ID_KEY = "model-id-key"

@OptIn(MapboxExperimental::class)
@Composable
fun MapView(
  modifier: Modifier = Modifier,
  cameraView: MapViewportState,
  initialPoint: Point,
  context: Context,
  locationPoint: Point?,
  capsulePoint: Point,
  modelId: String,
  modelUri: String?,
  selectedCapsule: NearByCapsule,
  isCapsuleSelected: Boolean,
  loadModelState: Load3dModelState,
  navigate: (String) -> Unit,
  updateLocation: (Point) -> Unit,
  onCapsuleSelected: (Boolean) -> Unit,
  onArViewClicked: () -> Unit,
  setModelLoadingStateIdle: () -> Unit,
  loadModel: () -> Unit
) {
  var pointAnnotationManager by remember { mutableStateOf<PointAnnotationManager?>(null) }

  if (isCapsuleSelected)
    ShowDialog(selectedCapsule = selectedCapsule, modelState = loadModelState,
      flag = true,
      openCapsule = {
        onCapsuleSelected(false)
        navigate(Screen.OpenCapsuleContentScreen.route)
      }, closeDialog = {
        onCapsuleSelected(false)
      }, viewAr = {
        if (checkARCoreAvailability(context))
          onArViewClicked()
      }, load3dModel = {
      })

  MapboxMap(
    modifier.fillMaxSize(),
    mapViewportState = cameraView,
    onMapClickListener = { point ->
      if (is3dModelClicked(point, modelCoordinates = capsulePoint)) {
        if (arePointsWithin10Meters(locationPoint, capsulePoint)) {
          onCapsuleSelected(true)
        }
      }
      false
    }
  ) {
    MapEffect(this) { mapView ->


      if (modelUri != null) {
        mapView.mapboxMap.apply {
          addModel(model(modelId) { uri("file://${modelUri}") })

        }
      } else {
        mapView.mapboxMap.apply {
          addModel(model(modelId) { uri("asset://testmodel.glb") })

        }
      }
      mapView.mapboxMap.loadStyle(BuildConfig.MAPBOX_STYLE_URI_DAY)

      startLocationUpdates(context) {
        updateLocation(Point.fromLngLat(it.longitude, it.latitude))
      }
      pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
    }

    ModelLayer(
      sourceState = rememberGeoJsonSourceState {
        data = GeoJSONData(
          listOf(
            Feature.fromGeometry(capsulePoint)
              .also { it.addStringProperty(MODEL_ID_KEY, modelId) },
          )
        )
      }
    ) {
      this.modelId = ModelIdValue(Expression.get(MODEL_ID_KEY))
      modelType = ModelTypeValue.COMMON_3D
      modelScale = DoubleListValue(listOf(7.0, 7.0, 7.0))
      modelTranslation = DoubleListValue(listOf(0.0, 0.0, 10.0))
      modelRotation = DoubleListValue(listOf(0.0, 0.0, 90.0))
      modelOpacity = DoubleValue(1.0)
      modelAmbientOcclusionIntensity = DoubleValue(1.0)
    }
  }

  LaunchedEffect(locationPoint) {
    AddPointer(
      context = context,
      point = locationPoint,
      capsulePoint = capsulePoint,
      pointAnnotationManager = pointAnnotationManager
    )
  }
}

fun is3dModelClicked(
  point: Point,
  modelCoordinates: Point,
  radiusInMeters: Double = 200.0
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

fun AddPointer(
  context: Context,
  point: Point?,
  capsulePoint: Point,
  pointAnnotationManager: PointAnnotationManager?
) {

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
  }
  point?.let {
    val textAnnotation = PointAnnotationOptions()
      .withPoint(capsulePoint)
      .withTextField(String.format("%.2f m", calculateDistance(point, capsulePoint)))
      .withTextSize(14.0)
      .withTextColor(Color.GRAY)

      .withTextOffset(listOf(0.0, -7.5)) // Offset to position above model
    pointAnnotationManager?.create(textAnnotation)
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
