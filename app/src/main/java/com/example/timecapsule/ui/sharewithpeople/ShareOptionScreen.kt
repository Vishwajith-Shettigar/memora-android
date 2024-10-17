package com.example.timecapsule.ui.sharewithpeople

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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.ViewModel
import com.example.timecapsule.ui.theme.SubTitleFontColor
import com.example.timecapsule.ui.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.selecttime.NavigationRow
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel
import com.example.timecapsule.viewmodel.ShareWithPeopleOption

enum class SharePeopleOptions {
  SELECTED_PEOPLE,
  ALL,
  NONE,
}

@Preview
@Composable
fun ShareOptionScreen(
  viewModel: CapsuleCreationViewModel = hiltViewModel(),
  onNavigate: (NavigationAddCapsule, SharePeopleOptions) -> Unit = { _, _ -> }
) {
  var selectedOption by rememberSaveable { mutableStateOf(SharePeopleOptions.NONE) }
  Scaffold(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
        .padding(top = 20.dp),
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
      SelectionScreen(modifier = Modifier, selectedOption) {
        if (it == SharePeopleOptions.NONE)
          viewModel.setShareWithPeople(ShareWithPeopleOption.DONT_SHARE)
        else if (it == SharePeopleOptions.SELECTED_PEOPLE)
          viewModel.setShareWithPeople(ShareWithPeopleOption.SELECTED_PEOPLES)
        else
          viewModel.setShareWithPeople(ShareWithPeopleOption.SHARE_ALL)

        selectedOption = it

      }
      Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .align(Alignment.BottomCenter)
            .zIndex(2f)
      ) {
        NavigationRow() { navigationFlow ->
          onNavigate(navigationFlow, selectedOption)
        }
      }
    }
  }
}

@Composable
fun SelectionScreen(
  modifier: Modifier = Modifier,
  selectedOption: SharePeopleOptions = SharePeopleOptions.NONE,
  onOptionChange: (SharePeopleOptions) -> Unit = {}
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
          text = "Would you like to share your time capsule?",
          style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
          text = "You can share your time capsule with your family, friends, or the world.",
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
        text = "Share with selected members",
        description = "You can share with selected people on the next screen.",
        selected = selectedOption == SharePeopleOptions.SELECTED_PEOPLE,
        onClick = { onOptionChange(SharePeopleOptions.SELECTED_PEOPLE) }
      )

      RadioButtonOption(
        text = "Don't share with anyone",
        description = "You are not sharing with anyone.",
        selected = selectedOption == SharePeopleOptions.NONE,
        onClick = { onOptionChange(SharePeopleOptions.NONE) }
      )

      RadioButtonOption(
        text = "Share with all",
        description = "Anyone near your time capsule can see it.",
        selected = selectedOption == SharePeopleOptions.ALL,
        onClick = { onOptionChange(SharePeopleOptions.ALL) }
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

@Preview(showBackground = true)
@Composable
fun PreviewSelectionScreen() {
  SelectionScreen() {}
}
