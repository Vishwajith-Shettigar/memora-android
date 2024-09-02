package com.example.timecapsule.ui.theme.sharewithpeople

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.ui.theme.SubTitleFontColor
import com.example.timecapsule.ui.theme.selecttime.NavigationRow

@Preview
@Composable
fun ShareOptionScreen() {
  Scaffold(modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.primary)
      .padding(vertical = 30.dp),
    containerColor = MaterialTheme.colorScheme.primary,
    bottomBar = {
      NavigationRow()
    }
  ) { padding ->

    SelectionScreen(modifier = Modifier.padding(padding))
  }
}

@Composable
fun SelectionScreen(modifier: Modifier = Modifier) {
  var selectedOption by remember { mutableStateOf("") }

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
        selected = selectedOption == "Option 1",
        onClick = { selectedOption = "Option 1" }
      )

      RadioButtonOption(
        text = "Don't share with anyone",
        description = "You are not sharing with anyone.",
        selected = selectedOption == "Option 2",
        onClick = { selectedOption = "Option 2" }
      )

      RadioButtonOption(
        text = "Share with all",
        description = "Anyone near your time capsule can see it.",
        selected = selectedOption == "Option 3",
        onClick = { selectedOption = "Option 3" }
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
  SelectionScreen()
}
