package com.example.timecapsule.ui.selectlocation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.timecapsule.ui.theme.SubTitleFontColor
import com.example.timecapsule.ui.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.selecttime.NavigationRow
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel
import com.example.timecapsule.viewmodel.LocationOption

enum class LocationOptions {
  SELECTED,
  NONE
}

@Preview
@Composable
fun SelectLocationOptionScreen(
  viewModel: CapsuleCreationViewModel = hiltViewModel(),
  onNavigate: (NavigationAddCapsule, LocationOptions) -> Unit = { _, _ -> }
) {
  var selectedOption by rememberSaveable { mutableStateOf(LocationOptions.NONE) }

  Scaffold(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
        .padding(top = 30.dp),
    containerColor = MaterialTheme.colorScheme.primary,
  ) { innerPadding ->

    Box(
      modifier = Modifier
        .padding(
          start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
          end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
          top = innerPadding.calculateTopPadding()
        )
          .fillMaxSize()
    ) {
      SelectionScreen(modifier = Modifier.padding(innerPadding), selectedOption) {
        if (it==LocationOptions.NONE)
          viewModel.setLocationOption(LocationOption.DONT_SELECT_LOCATION)
        else
          viewModel.setLocationOption(LocationOption.SELECT_LOCATION)

        selectedOption = it
      }
      Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .align(Alignment.BottomCenter)
            .zIndex(2f)
      ) {
        NavigationRow { navigationFlow ->
          onNavigate(navigationFlow, selectedOption)
        }
      }
    }
  }
}

@Composable
fun SelectionScreen(
  modifier: Modifier = Modifier,
  selectedOption: LocationOptions,
  onSelectionChange: (LocationOptions) -> Unit
) {

  Column(
    modifier = modifier
        .fillMaxSize()
        .padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {

    Row(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .weight(1f),
      horizontalArrangement = Arrangement.Start
    ) {
      Column {
        Text(
          text = "Would you like to hide your time capsule in a specific location?",
          style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
          text = "You can place your time capsule in a specific location to make it more interesting.",
          fontSize = 16.sp,
          modifier = Modifier.padding(bottom = 16.dp),
          color = SubTitleFontColor,
        )
      }
    }

    Column(
      modifier = Modifier.weight(2f),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      RadioButtonOption(
        text = "Select a location",
        description = "You can choose a location on the next screen.",
        selected = selectedOption == LocationOptions.SELECTED,
        onClick = { onSelectionChange(LocationOptions.SELECTED) }
      )
      RadioButtonOption(
        text = "Don't select a location",
        description = "You are not placing the time capsule anywhere.",
        selected = selectedOption == LocationOptions.NONE,
        onClick = { onSelectionChange(LocationOptions.NONE) }
      )
    }
  }
}

@Composable
fun RadioButtonOption(text: String, description: String, selected: Boolean, onClick: () -> Unit) {
  Column(
    modifier = Modifier
        .padding(vertical = 10.dp)
        .fillMaxWidth()
        .wrapContentHeight()
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start
    ) {
      RadioButton(
        selected = selected,
        onClick = onClick,
        colors = RadioButtonDefaults.colors(selectedColor = Color.Blue)
      )
      Text(
        text = text,
        fontSize = 18.sp,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 8.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,

        )
    }
    Text(
      text = description,
      fontSize = 14.sp,
      color = SubTitleFontColor,
      modifier = Modifier.padding(start = 40.dp)
    )
  }
}
