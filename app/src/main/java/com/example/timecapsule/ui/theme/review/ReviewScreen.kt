package com.example.timecapsule.ui.theme.review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.ui.theme.DateTimeFontColor
import com.example.timecapsule.ui.theme.sharewithpeople.ShowSelectedPeople

@Preview
@Composable
fun ReviewScreen() {
  Scaffold(modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.primary,
    topBar = {
      Text(
        modifier = Modifier
          .padding(
            horizontal = 5.dp
          ),
        text = "Review your details",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize =
          30.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  ) { innerPadding ->

    LazyColumn(
      modifier = Modifier
          .padding(
              innerPadding
          )
          .padding(vertical = 30.dp, horizontal = 10.dp)
    ) {
      item { SharedPeople() }
      item {
        DateAndTime()
      }
    }

  }
}

@Composable
fun SharedPeople() {
  Column(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
  ) {
    Text(
      text = "You have shared with ",
      style = MaterialTheme.typography.titleLarge.copy(
        fontSize =
        20.sp
      ),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    ShowSelectedPeople()
  }
}

@Composable
fun DateAndTime() {
  Column(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
  ) {
    Text(
      text = "Date And Time ",
      style = MaterialTheme.typography.titleLarge.copy(
        fontSize =
        20.sp
      ),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
  ) {
    Text(
      modifier = Modifier.padding(10.dp),
      text = "10-08-2089",
      style = MaterialTheme.typography.titleLarge.copy(
        fontSize =
        25.sp,
        fontWeight = FontWeight.Bold
      ),
      color = DateTimeFontColor
    )
    Text(
      modifier = Modifier.padding(10.dp),
      text = "4:30 PM",
      style = MaterialTheme.typography.titleLarge.copy(
        fontSize =
        25.sp,
        fontWeight = FontWeight.Bold
      ),
      color = DateTimeFontColor
    )
  }
}

