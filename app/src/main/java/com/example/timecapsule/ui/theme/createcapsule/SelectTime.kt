package com.example.timecapsule.ui.theme.createcapsule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType.Companion.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.NavigatioButtons
import com.example.timecapsule.ui.theme.SignUpBackground
import com.example.timecapsule.ui.theme.black
import com.example.timecapsule.ui.theme.util.DeviceType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NavigationRow() {
  val isTablet = DeviceType.isTablet()

  Row(
    modifier = Modifier.zIndex(2f)
      .fillMaxWidth()
      .padding(start = 20.dp, end = 20.dp, bottom = 50.dp, top = 3.dp)
      .background(Color.Transparent),
    horizontalArrangement =
    if (isTablet)
      Arrangement.Absolute.SpaceEvenly
    else
      Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier =
      Modifier
        .size(50.dp)
        .clip(CircleShape)
        .padding(0.dp)
        .background(brush = Brush.horizontalGradient(NavigatioButtons))
        .align(Alignment.CenterVertically)
    )
    {
      IconButton(
        onClick = { },
        modifier = Modifier.align(Alignment.Center)

      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "back",
          tint = Color.White
        )
      }
    }

    Box(
      modifier =
      Modifier
        .size(50.dp)
        .clip(CircleShape)
        .padding(0.dp)
        .background(brush = Brush.horizontalGradient(NavigatioButtons))
        .align(Alignment.CenterVertically)
    )
    {
      IconButton(
        onClick = { },
        modifier = Modifier.align(Alignment.Center)
      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_forward), contentDescription = "back",
          tint = Color.White
        )
      }
    }
  }

}

@Composable
fun BackRow() {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(20.dp)
      .background(MaterialTheme.colorScheme.primary),
    horizontalArrangement = Arrangement.Start
  ) {
    IconButton(
      onClick = { }, modifier =
      Modifier
        .size(40.dp)
        .clip(CircleShape)
        .border(1.dp, Color.Gray, CircleShape)
    ) {
      Icon(
        painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "back",
        tint = Color.Gray
      )
    }
  }

}

@Composable
fun SelectTime(modifier: Modifier = Modifier) {
  DateTimePicker(modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(modifier: Modifier = Modifier) {

  val isTablet = DeviceType.isTablet()
  var showDatePicker by remember { mutableStateOf(true) }
  var shpwTimePicker by remember {
    mutableStateOf(false)
  }

  val datePickerState = rememberDatePickerState()
  val timePickerState = rememberTimePickerState()
  val selectedDate = datePickerState.selectedDateMillis?.let {
    convertMillisToDate(it)
  } ?: ""

  val selectedTime = timePickerState.let {
    convertToTimeFormat(it.hour, it.minute)
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 15.dp)
      .background(MaterialTheme.colorScheme.primary),

    ) {

    Row {
      OutlinedTextField(
        value = selectedDate,
        onValueChange = { },
        label = { Text("D/M/Y ") },
        readOnly = true,
        enabled = false,
        trailingIcon = {
          IconButton(
            onClick = {
              showDatePicker = true
            },
            modifier =
            if (showDatePicker) {
              Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(1.dp)
            } else {
              Modifier
            }
          ) {
            Icon(
              painter = painterResource(id = R.drawable.ic_date_range),
              contentDescription = "Select date"
            )
          }
        },
        modifier = Modifier
          .weight(2F)
          .height(64.dp)
      )

      OutlinedTextField(
        value = selectedTime,
        onValueChange = { },
        label = { Text("H:M") },
        readOnly = true,
        enabled = false,
        trailingIcon = {
          IconButton(
            onClick = { showDatePicker = false },
            modifier =
            if (!showDatePicker) {
              Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(1.dp)
            } else {
              Modifier
            }

          ) {
            Icon(
              painter = painterResource(id = R.drawable.ic_time_range),
              contentDescription = "Select date",
            )
          }
        },
        modifier = Modifier
          .weight(1.5F)
          .height(64.dp)
      )
    }

    Box(
      modifier = Modifier
        .wrapContentSize()
        .shadow(elevation = 4.dp)
        .background(MaterialTheme.colorScheme.surface)
        .padding(10.dp)
        .align(Alignment.CenterHorizontally)
    ) {
      if (showDatePicker) {
        DatePicker(
          state = datePickerState,
          showModeToggle = false,

          modifier =
          if (isTablet) {
            Modifier
              .size(700.dp)
              .padding(vertical = 5.dp)
          } else {
            Modifier
              .padding(vertical = 0.dp)
              .scale(1f)
          },
          colors = DatePickerDefaults.colors(selectedDayContainerColor = NavigatioButtons.get(0))
        )
      } else {
        TimePicker(
          state = timePickerState,
          modifier =
          if (isTablet) {
            Modifier
              .size(700.dp)
              .padding(vertical = 5.dp)
          } else {
            Modifier
              .size(400.dp)
              .padding(vertical = 5.dp)
          },
        )
      }
    }
  }
}

fun convertMillisToDate(millis: Long): String {
  val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
  return formatter.format(Date(millis))
}

fun convertToTimeFormat(hour: Int, minute: Int): String {
  return String.format("%02d:%02d", hour, minute)
}