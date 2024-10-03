package com.example.timecapsule.ui.selectlocation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timecapsule.BuildConfig
import com.example.timecapsule.R
import com.example.timecapsule.ui.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.selecttime.NavigationRow
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.ui.util.searchPlace
import com.example.timecapsule.ui.theme.white
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel
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


@Composable
fun SelectLocationScreen(
  viewModel: CapsuleCreationViewModel = hiltViewModel(),
  onNavigate: (NavigationAddCapsule) -> Unit
) {
  if (!Places.isInitialized()) {
    Places.initialize(LocalContext.current, BuildConfig.MAPS_API_KEY)
  }
  Scaffold { padding ->
    MyMapWithSearch(Modifier.padding(padding), viewModel, onNavigate)
  }
}

@Composable
fun MyMapWithSearch(
  modifier: Modifier = Modifier,
  viewModel: CapsuleCreationViewModel,
  onNavigate: (NavigationAddCapsule) -> Unit
) {
  val context = LocalContext.current
  val placesClient = remember { Places.createClient(context) }
  val cameraPositionState = rememberCameraPositionState {
    mutableStateOf(
      CameraPosition.fromLatLngZoom(
        viewModel.latLang, 10f
      )
    )
  }

  val isSataliteView = viewModel.isSataliteView

  var markerSate by remember {
    mutableStateOf(MarkerState(viewModel.latLang))
  }

  Box(modifier = modifier.fillMaxSize()) {
    SearchBar(isSataliteView) { query ->
      searchPlace(placesClient, query) { latLng ->
        if (latLng != null) {
          cameraPositionState.position = CameraPosition.fromLatLngZoom(latLng, 15f)
          markerSate = MarkerState(latLng)
          viewModel.latLang=latLng
        } else {
          Unit
        }
      }
    }
    GoogleMap(
      modifier = Modifier.fillMaxSize(),
      cameraPositionState = cameraPositionState,
      uiSettings = MapUiSettings(zoomControlsEnabled = true), onMapClick = { latLng ->
        markerSate = MarkerState(latLng)
        viewModel.latLang=latLng

      },
      properties = if (isSataliteView.value) {
        MapProperties(mapType = MapType.HYBRID)
      } else {
        MapProperties(mapType = MapType.TERRAIN)

      }
    ) {
      Marker(
        state = markerSate,
        contentDescription = markerSate.position.latitude.toString() + "\n" + markerSate.position.longitude.toString()
      )
    }
    Box(
      modifier = Modifier
          .fillMaxWidth()
          .padding(0.dp)
          .align(Alignment.BottomCenter)
    ) {
      NavigationRow { navigationFlow ->
        onNavigate(navigationFlow)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(isSataliteView: MutableState<Boolean>, onSearch: (String) -> Unit) {
  val isTablet = DeviceType.isTablet()
  var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentWidth()
        .zIndex(1f)
        .padding(vertical = 10.dp, horizontal = 5.dp), horizontalArrangement = Arrangement.Center,
    verticalAlignment = Alignment.CenterVertically
  ) {
    OutlinedTextField(
      value = searchQuery,
      onValueChange = {
        searchQuery = it
        onSearch(it.text)
      },
      leadingIcon = {
        Icon(
          painter = painterResource(id = R.drawable.ic_location_search),
          contentDescription = "search icon"
        )
      },
      modifier =
      if (!isTablet) {
          Modifier
              .background(Color.White, RoundedCornerShape(30))
              .weight(2f)
      } else {
          Modifier
              .widthIn(min = 500.dp, max = 800.dp)
              .zIndex(2f)
              .background(Color.White, RoundedCornerShape(40))
      },
      placeholder = {
        Text(
          "Search...",
          style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray, fontSize = 15.sp)
        )
      },
      colors = TextFieldDefaults.outlinedTextFieldColors(
        containerColor = Color.White,
        focusedTextColor = Color.Black
      ),
      shape = RoundedCornerShape(30),
      singleLine = true
    )
    Switch(
      modifier = if (!isTablet) {
          Modifier
              .padding(horizontal = 4.dp)
              .weight(1f)
      } else {
        Modifier.padding(horizontal = 4.dp)
      },
      colors = SwitchDefaults.colors(
        checkedThumbColor = Color.Blue,
        checkedTrackColor = white,
        uncheckedTrackColor = white
      ),
      checked = isSataliteView.value,
      onCheckedChange = {
        isSataliteView.value = !isSataliteView.value
      })
  }
}
