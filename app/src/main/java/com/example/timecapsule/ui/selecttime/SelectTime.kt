package com.example.timecapsule.ui.selecttime

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.NavigatioButtons
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class NavigationAddCapsule {
  NEXT,
  BACK
}

@Composable
fun NavigationRow(showBackBtn: Boolean = true, onClick: (NavigationAddCapsule) -> Unit = {}) {
  val isTablet = DeviceType.isTablet()

  val navigationButtonBgcolor = if (showBackBtn)
    Brush.horizontalGradient(NavigatioButtons)
  else
    Brush.horizontalGradient(listOf(Color.Transparent, Color.Transparent))
  Row(
    modifier = Modifier
        .zIndex(2f)
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
          .background(brush = navigationButtonBgcolor)
          .align(Alignment.CenterVertically)
    )
    {
      if (showBackBtn) {
        IconButton(
          onClick = { onClick(NavigationAddCapsule.BACK) },
          modifier = Modifier.align(Alignment.Center)
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "back",
            tint = Color.White
          )
        }
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
        onClick = { onClick(NavigationAddCapsule.NEXT) },
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
fun BackRow(clickedBack: () -> Unit = {}) {
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
        .background(Color.Transparent),
    horizontalArrangement = Arrangement.Start
  ) {
    IconButton(
      onClick = { clickedBack() }, modifier =
        Modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(1.dp, Color.DarkGray, CircleShape)
    ) {
      Icon(
        painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "back",
        tint = Color.Gray
      )
    }
  }
}

@Composable
fun SelectTime(modifier: Modifier = Modifier, viewModel: CapsuleCreationViewModel) {
  DateTimePicker(modifier, viewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePicker(modifier: Modifier = Modifier, viewModel: CapsuleCreationViewModel) {

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



  LaunchedEffect(selectedDate, selectedTime) {
    val selectedTimestamp = getSelectedTimestamp(
      datePickerState.selectedDateMillis,
      timePickerState.hour,
      timePickerState.minute
    )
    if (selectedTimestamp != null) {
      viewModel.setTimeStamp(selectedTimestamp)
    }
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
                  .wrapContentSize()
                  .padding(vertical = 0.dp)

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

fun getSelectedTimestamp(dateMillis: Long?, hour: Int, minute: Int): Timestamp? {
  return if (dateMillis != null) {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateMillis

    // Set the time from the time picker
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0) // Optional: set seconds to 0

    // Get the Date object and convert to Firebase Timestamp
    val selectedDate = calendar.time
    Timestamp(selectedDate) // Convert Date to Firebase Timestamp
  } else {
    null
  }
}
