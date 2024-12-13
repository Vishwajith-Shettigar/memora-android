package com.example.timecapsule.ui.capsulelist.v2

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.openSansExtraBold
import kotlinx.coroutines.launch

enum class Filter {
  ALL,
  ACTIVE,
  OPENED
}

@OptIn(
  ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class,
  ExperimentalMaterialApi::class
)
@Composable
fun CapsuleCardListScreen() {

  val rotation = remember { Animatable(0f) }
  val coroutineScope = rememberCoroutineScope()

  var filter by remember {
    mutableStateOf(Filter.ALL)
  }

  // Trigger the animation
  LaunchedEffect(filter) {
    rotation.snapTo(0f)
    coroutineScope.launch {
      rotation.animateTo(
        targetValue = 360f * 500F,
        animationSpec = tween(
          durationMillis = 2000,
          easing = LinearOutSlowInEasing
        )
      )
    }
  }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { },
        containerColor = LightBlue
      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_add),
          tint = Color.White.copy(alpha = 0.7F),
          contentDescription = "add time capsule",
          modifier = Modifier.size(30.dp)
        )
      }
    }
  )
  { innerPadding ->

    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.primary)
        .padding(innerPadding)

    ) {

      var expandedCardIndex by remember { mutableStateOf(-1) }

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 3.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(0.dp),
      ) {
        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(100.dp)
              .padding(start = 12.dp, end = 17.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              "Time Capsule",
              style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = openSansExtraBold
              )
            )
            AsyncImage(
              model = R.drawable.onboarding_image,
              contentDescription = "Profile Picture",
              modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .padding(end = 1.dp)
                .graphicsLayer(rotationZ = rotation.value),
              contentScale = ContentScale.Crop
            )
          }
        }

        item {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .height(50.dp)
              .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
          ) {

            Chip(
              colors = ChipDefaults.chipColors(backgroundColor = LightBlue),
              onClick = { filter = Filter.ALL },
              modifier = Modifier
                .padding(horizontal = 3.dp)
                .height(40.dp)
                .animateContentSize()
            ) {
              Icon(
                modifier = Modifier.padding(end = 3.dp),
                painter = painterResource(id = com.example.timecapsule.R.drawable.ic_view_list),
                contentDescription = "all icon",
                tint =
                if (filter == Filter.ALL)
                  Color.Black
                else
                  Color.White
              )
              if (filter == Filter.ALL)
                Text(text = "All", color = Color.Black)
            }
            Chip(
              colors = ChipDefaults.chipColors(backgroundColor = LightBlue),
              onClick = { filter = Filter.ACTIVE },
              modifier = Modifier
                .padding(horizontal = 3.dp)
                .height(40.dp)
                .animateContentSize()
            ) {
              Icon(
                modifier = Modifier.padding(end = 3.dp),
                painter = painterResource(id = com.example.timecapsule.R.drawable.ic_time_range),
                contentDescription = "active icon",
                tint =
                if (filter == Filter.ACTIVE)
                  Color.Black
                else
                  Color.White
              )
              if (filter == Filter.ACTIVE)
                Text(
                  text = "Active",
                  color = Color.Black
                )
            }
            Chip(
              colors = ChipDefaults.chipColors(backgroundColor = LightBlue),
              onClick = { filter = Filter.OPENED },
              modifier = Modifier
                .padding(horizontal = 3.dp)
                .height(40.dp)
                .animateContentSize()
            ) {
              Icon(
                modifier = Modifier.padding(end = 3.dp),
                painter = painterResource(id = com.example.timecapsule.R.drawable.ic_history),
                contentDescription = "opened icon",
                tint =
                if (filter == Filter.OPENED)
                  Color.Black
                else
                  Color.White
              )
              if (filter == Filter.OPENED)

                Text(text = "Opened", color = Color.Black)
            }
          }
        }

        items((0..9).chunked(2)) { rowItems ->

          var p = false
          if (rowItems[0] == expandedCardIndex || rowItems[1] == expandedCardIndex) {
            p = true
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            rowItems.forEachIndexed { index, item ->
              val isExpanded = expandedCardIndex == item
              if (p && isExpanded != true) {
              } else
                Box(
                  modifier = Modifier
                    .weight(
                      if (isExpanded) 1f else 0.5f,
                      fill = false
                    )
                    .animateContentSize()
                ) {
                  CapsuleCard(
                    isExpanded = isExpanded,
                    onClick = {
                      expandedCardIndex = if (isExpanded) -1 else item
                    }
                  )
                }
            }
          }
          if (p == true)
            if (rowItems[0] != expandedCardIndex)
              Box(
                modifier = Modifier
                  .fillMaxWidth(0.5F)
                  .padding(top = 8.dp)
              ) {
                CapsuleCard(
                  isExpanded = expandedCardIndex == rowItems[0],
                  onClick = {
                    expandedCardIndex = rowItems[0]
                  }
                )
              }
            else
              Box(
                modifier = Modifier
                  .fillMaxWidth(0.5F)
                  .padding(top = 8.dp)
              ) {
                CapsuleCard(
                  isExpanded = expandedCardIndex == rowItems[1],
                  onClick = {
                    expandedCardIndex = rowItems[1]
                  }
                )
              }
        }
      }
    }
  }
}
