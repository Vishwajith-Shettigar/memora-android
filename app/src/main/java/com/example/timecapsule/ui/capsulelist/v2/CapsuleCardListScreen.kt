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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.model.CapsuleDetails
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.openSansExtraBold
import com.example.timecapsule.ui.util.ColorsMap
import com.example.timecapsule.viewmodel.CapsuleListScreenState
import com.example.timecapsule.viewmodel.ShowCapsulesListViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
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
fun CapsuleCardListScreen(
  viewModel: ShowCapsulesListViewModel = hiltViewModel(),
  addCapsuleBtnClicked: () -> Unit = {},
  onCapsuleClicked: (id: String) -> Unit = {},
  openCapule: (id: String) -> Unit = {}
) {

  val state by viewModel.capsuleListState.collectAsState()

  val isLoading: Boolean = state is CapsuleListScreenState.Loading
  val isSuccess: Boolean = state is CapsuleListScreenState.Success

  var showCapsuleList: Boolean = state is CapsuleListScreenState.Success

  val capsuleList = remember {
    mutableStateListOf<CapsuleDetails>()
  }

  LaunchedEffect(key1 = state) {
    when (state) {
      is CapsuleListScreenState.Loading -> {
      }

      is CapsuleListScreenState.Success -> {
        capsuleList.clear()
        capsuleList.addAll((state as CapsuleListScreenState.Success).capsuleList)
      }

      is CapsuleListScreenState.Error -> {}
      CapsuleListScreenState.Idle -> {
      }
    }
  }


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
        targetValue = 360f * 100F,
        animationSpec = tween(
          durationMillis = 1000,
          easing = LinearOutSlowInEasing
        )
      )
    }
  }

  Scaffold(
    floatingActionButton = {
      FloatingActionButton(
        onClick = { addCapsuleBtnClicked() },
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

        if (isLoading)
          items(3) {
            Row(
              modifier = Modifier
                  .fillMaxWidth()
                  .wrapContentHeight(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)

            ) {
              Box(
                modifier = Modifier
                    .weight(0.5F)
                    .height(200.dp)
                    .placeholder(
                        visible = true,
                        shape = RoundedCornerShape(30.dp),
                        highlight = PlaceholderHighlight.shimmer(),
                        color = Color.Gray.copy(alpha = 0.3f),
                    )
                    .clip(shape = RoundedCornerShape(30.dp))
              )
              Box(
                modifier = Modifier
                    .weight(0.5F)
                    .height(200.dp)
                    .placeholder(
                        visible = true,
                        shape = RoundedCornerShape(30.dp),
                        highlight = PlaceholderHighlight.shimmer(),
                        color = Color.Gray.copy(alpha = 0.3f),
                    )
                    .clip(shape = RoundedCornerShape(30.dp))
              )
            }
          }

        if (isSuccess)
          items((0..<capsuleList.size).chunked(2)) { rowItems ->

            var p = false
            if (rowItems[0] == expandedCardIndex) {
              p = true
            } else if (rowItems.size == 2) {
              if (rowItems[1] == expandedCardIndex)
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
                        .then(
                            if (rowItems.size == 2 || isExpanded)
                                Modifier
                                    .weight(
                                        if (isExpanded) 1f else 0.5f,
                                        fill = false
                                    )
                            else
                                Modifier.fillMaxWidth(0.5F)
                        )
                        .animateContentSize()
                  ) {
                    CapsuleCard(
                      capsuleDetails = capsuleList[item],
                      isExpanded = isExpanded,
                      bgColor = ColorsMap.getColor(item),
                      onClick = {
                        expandedCardIndex = if (isExpanded) -1 else item
                      }, onCapsuleDetailsClicked = onCapsuleClicked,
                      openCapule = openCapule
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
                    capsuleDetails = capsuleList[rowItems[0]],
                    isExpanded = expandedCardIndex == rowItems[0],
                    bgColor = ColorsMap.getColor(rowItems[0]),
                    onClick = {
                      expandedCardIndex = rowItems[0]
                    }, onCapsuleDetailsClicked = onCapsuleClicked,
                    openCapule = openCapule
                  )
                }
              else
                if (rowItems.size == 2)
                  Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5F)
                        .padding(top = 8.dp)
                  ) {
                    CapsuleCard(
                      capsuleDetails =
                      capsuleList[rowItems[1]],
                      isExpanded = expandedCardIndex == rowItems[1],
                      bgColor = ColorsMap.getColor(rowItems[1]),
                      onClick = {
                        expandedCardIndex = rowItems[1]
                      }, onCapsuleDetailsClicked = onCapsuleClicked,
                      openCapule = openCapule
                    )
                  }
          }
      }
    }
  }
}
