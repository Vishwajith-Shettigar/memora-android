package com.example.timecapsule.ui.capsuledetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.DMSerifText
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.RubikBubble
import com.example.timecapsule.ui.capsulelist.TimerPlaceholder
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.sharewithpeople.ShowSelectedPeople

@Preview
@Composable
fun CapsuleDetailsScreen(onBack: () -> Unit = {}) {
  Scaffold(
    containerColor = LightBlue,
  ) { innerpadding ->
    LazyColumn(
      modifier = Modifier
          .padding(innerpadding)
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.primary)
    ) {
      item {
        TopPart(onBack = onBack)
      }
      item {
        BottomPart()
      }

    }

  }
}

@Composable
fun TopPart(modifier: Modifier = Modifier, onBack: () -> Unit) {
  Box(
    modifier = modifier
        .fillMaxWidth()
        .height(330.dp)
  ) {
    Box(
      modifier = modifier
          .fillMaxWidth()
          .height(330.dp)
          .clip(
              shape =
              RoundedCornerShape(bottomStart = 290.dp)
          )
          .shadow(8.dp, shape = RoundedCornerShape(bottomStart = 290.dp))
          .background(LightBlue)
          .zIndex(1f),
    ) {
      BackRow {
        onBack()
      }
      Image(
        painter = painterResource(id = R.drawable.testimg),
        contentDescription = null,
        modifier = Modifier
            .size(280.dp)
            .align(Alignment.CenterEnd)
      )

    }
    Column(
      modifier = Modifier
          .wrapContentSize()
          .align(Alignment.BottomStart)
          .padding(start = 10.dp)
          .zIndex(2f)
    ) {
      TimerPlaceholder("109099D", isSmallSize = false)

    }
  }
}

@Composable
fun BottomPart() {
  Column(
    modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp)
        .wrapContentHeight()
        .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
        .background(MaterialTheme.colorScheme.primaryContainer)
        .padding(vertical = 30.dp)

  ) {
    Row(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(horizontal = 20.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = stringResource(id = R.string.capsule_details),
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 25.sp,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    }

    Row(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(horizontal = 20.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = stringResource(id = R.string.date_created),
        style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 20.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontFamily = DMSerifText
        )
      )
      Text(
        text = "July 10 2024", style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 16.sp,
          fontFamily = RubikBubble,
          color = LightBlue
        )
      )

    }

    Row(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(horizontal = 20.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = stringResource(id = R.string.creator),
        style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 20.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontFamily = DMSerifText
        )
      )
      Text(
        text = "User15xDark", style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 16.sp,
          fontFamily = RubikBubble,
          color = LightBlue
        )
      )

    }

    Row(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(horizontal = 20.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = stringResource(id = R.string.opening_date),
        style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 20.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontFamily = DMSerifText
        )
      )
      Text(
        text = "Dec 05 2084, 1:40 PM", style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 16.sp,
          fontFamily = RubikBubble,
          color = LightBlue
        )
      )
    }

    Column(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
      Text(
        modifier = Modifier.padding(bottom = 5.dp),
        text = stringResource(id = R.string.shared_with_capsule_details_screen),
        style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 20.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontFamily = DMSerifText
        )
      )
      ShowSelectedPeople(disableCrossBtn = true)
    }
  }
}
