package com.example.timecapsule.ui.theme.capsulelist

import android.graphics.drawable.PaintDrawable
import android.icu.text.ListFormatter.Width
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.darkCardBackground
import com.example.timecapsule.ui.theme.util.DeviceType

@Composable
fun CapsuleCard(
  rowItemSize: Int = 2,
  modifier: Modifier = Modifier,
  onCapsuleClicked: (id: String) -> Unit = {}
) {
  // State to toggle the visibility of the pane
  var isPaneVisible by remember { mutableStateOf(false) }

  // Card UI
  Card(
    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer),
    modifier = modifier
        .wrapContentHeight()
        .fillMaxWidth(),
    elevation = CardDefaults.cardElevation(10.dp),
    shape = RoundedCornerShape(6.dp),
    onClick = { onCapsuleClicked("1234") }
  ) {
    Column(
      modifier =
      if (rowItemSize == 1) Modifier
          .padding(16.dp)
          .background(MaterialTheme.colorScheme.primaryContainer)
      else Modifier
          .padding(5.dp)
          .background(MaterialTheme.colorScheme.primaryContainer)
    ) {

      ResponsiveRowColumn(title = "Card title", timerValue = "51719D", rowItemSize = rowItemSize)

      if (rowItemSize == 1) {
        Spacer(modifier = Modifier.height(8.dp))
      }

      AsyncImage(
        modifier = Modifier
            .size(200.dp)
            .align(Alignment.CenterHorizontally),
        model = R.drawable.testimg, contentDescription = "capsule image"
      )

      // Icon Button at the bottom right
      Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.End,
      ) {
        Box(
          modifier = Modifier
              .size(30.dp)
              .clip(shape = RoundedCornerShape(100))
              .background(Color.Gray.copy(alpha = 0.2F))
              .align(Alignment.CenterVertically)
        )
        {
          IconButton(
            onClick = { isPaneVisible = !isPaneVisible },
          ) {
            Icon(
              painter =
              if (isPaneVisible)
                painterResource(id = R.drawable.ic_drop_down)
              else
                painterResource(id = R.drawable.ic_drop_up),
              contentDescription = "More Options",
              tint = Color.Black
            )
          }
        }
      }
      AnimatedVisibility(
        visible = isPaneVisible,
      ) {
        SmallPane()
      }
    }
  }
}

@Composable
fun ResponsiveRowColumn(
  modifier: Modifier = Modifier,
  title: String,
  timerValue: String,
  rowItemSize: Int
) {
  val smallerTextsize: Boolean = if (rowItemSize == 2 && !DeviceType.isTablet()) {
    true
  } else
    false
  if (rowItemSize == 2) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = if (smallerTextsize)
            20.sp
          else
            35.sp
        )
      )
      Spacer(modifier = Modifier.height(8.dp))
      TimerPlaceholder(timerValue = timerValue, smallerTextsize)
    }
  } else {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge
      )
      TimerPlaceholder(timerValue = timerValue)
    }
  }
}

@Composable
fun TimerPlaceholder(timerValue: String, isSmallSize: Boolean = false) {
  Row {
    timerValue.forEach { digit ->
      Box(
        modifier = Modifier
            .wrapContentSize()
            .padding(horizontal = 1.dp)
            .clip(
                shape = RoundedCornerShape(10)
            )
            .background(Color.Black)
            .padding(horizontal = 4.dp)
      )
      {
        Text(
          text = digit.toString(),
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            fontSize = if (isSmallSize)
              14.sp
            else
              25.sp
          ),
          color = Color.Gray,
        )
      }

    }
  }
}

@Composable
fun SmallPane() {
  Card(
    modifier = Modifier
      .fillMaxWidth(),
    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
  ) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp))
    {

      Text(
        text = "Created by you",
        style = MaterialTheme.typography.bodySmall
      )
      Text(
        text = "Created on 18-09-2024",
        modifier = Modifier.padding(vertical = 5.dp),
        style = MaterialTheme.typography.bodySmall
      )
      Text(
        text = "Lorem ispum poek uytie okeo poekke yuyey looio jeyue teyet huuie iepoe loe.",
        modifier = Modifier.padding(vertical = 5.dp),
        style = MaterialTheme.typography.bodySmall
      )
    }

  }
}

@Preview(showBackground = true)
@Composable
fun CustomCardPreview() {
  CapsuleCard()
}
