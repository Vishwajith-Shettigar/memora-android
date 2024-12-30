package com.example.timecapsule.ui.capsulelist.v2

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Chip
import androidx.compose.material.ChipDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.model.CapsuleDetails
import com.example.timecapsule.PermissionHandler
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.DMSerifText
import com.example.timecapsule.ui.theme.Inter
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.RubikBubble
import com.example.timecapsule.ui.theme.openSansExtraBold
import com.example.timecapsule.ui.theme.overSeer
import com.example.timecapsule.ui.util.ColorsMap
import com.example.timecapsule.viewmodel.CapsuleListScreenState
import com.example.timecapsule.viewmodel.ShowCapsulesListViewModel
import com.example.util.NetWorkException
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.firebase.firestore.FirebaseFirestoreException
import com.mapbox.maps.extension.style.layers.generated.backgroundLayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
  openCapule: (id: String, isSurPriseCapsule: Boolean) -> Unit = { _, _ -> }
) {

  val state by viewModel.capsuleListState.collectAsState()

  val isLoading: Boolean = state is CapsuleListScreenState.Loading
  val isSuccess: Boolean = state is CapsuleListScreenState.Success

  val isError: Boolean = state is CapsuleListScreenState.Error

  var showCapsuleList: Boolean = state is CapsuleListScreenState.Success

  var capsuleList = remember {
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

      is CapsuleListScreenState.Error -> {
      }

      CapsuleListScreenState.Idle -> {
      }
    }
  }

  val context = LocalContext.current

  val rotation = remember { Animatable(0f) }
  val coroutineScope = rememberCoroutineScope()

  var filter by remember {
    mutableStateOf(Filter.ALL)
  }

  var refresh by remember {
    mutableStateOf(false)
  }


  LaunchedEffect(refresh) {
    if (refresh) {
      viewModel.getCapsulesList() {
        refresh = false
      }
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
    PermissionHandler(
      onPermissionsGranted = {

      },
      onPermissionsDenied = {
        Toast.makeText(
          context,
          "To access all features, please enable permissions in the settings.",
          Toast.LENGTH_SHORT
        ).show()
      }
    )
    if (isError) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .zIndex(5.0F)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .align(Alignment.Center)
            .padding(top = 20.dp),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .padding(vertical = 20.dp)
              .size(220.dp)
              .background(
                brush = Brush.radialGradient(
                  colors = listOf(
                    Color.Red,
                    Color.Red,
                    Color.LightGray.copy(0.1f)
                  ),
                  center = Offset.Unspecified,
                  radius = 220f
                ),
                shape = CircleShape
              )
          ) {
            Image(
              painter = painterResource(id = com.example.timecapsule.R.drawable.nonetwork_graphic),
              contentDescription = "No network",
              modifier = Modifier.size(200.dp)
            )
          }

          Text(
            if (((state as CapsuleListScreenState.Error).exception is NetWorkException)) "No Connection"
            else
              "Something went wrong!",
            style = MaterialTheme.typography.titleLarge.copy(
              fontSize = 24.sp,
              fontWeight = FontWeight.Light,
              fontFamily = openSansExtraBold
            )
          )

          androidx.compose.material3.Button(
            modifier = Modifier.padding(vertical = 5.dp),
            onClick = {
              viewModel.getCapsulesList()

            },
            colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
          ) {
            Text(text = "Retry", color = Color.LightGray)
          }
        }
      }
    }

    if (isSuccess) {
      if ((state as CapsuleListScreenState.Success).capsuleList.size == 0) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .zIndex(5.0F)
        ) {
          Column(
            Modifier
              .fillMaxSize()
              .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Image(
              painter = painterResource(id = com.example.timecapsule.R.drawable.empty_capsule),
              contentDescription = "No capsules icon",
              Modifier
                .size(200.dp)
                .padding(10.dp)
            )
            Text(
              text = "No capsules found",
              style = MaterialTheme.typography.labelLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "Click on + to create.",
              style = MaterialTheme.typography.labelMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )

          }
        }
      }
    }

    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
        .padding(top = innerPadding.calculateTopPadding())
        .verticalScroll(rememberScrollState())
    ) {

      var expandedCardIndex by remember { mutableStateOf(-1) }

      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 3.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
      ) {

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
          Box(
            modifier = Modifier
              .wrapContentSize()
              .clip(shape = CircleShape)
              .zIndex(20.0F)
              .shadow(10.dp, shape = CircleShape)
              .clickable(true) {
                refresh = true
              }
          )
          {
            AsyncImage(
              model = R.drawable.onboarding_image,
              contentDescription = "capsule icon",
              modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .align(Alignment.Center)
                .padding(1.dp)
                .graphicsLayer(rotationZ = rotation.value),
              contentScale = ContentScale.Crop,

              )
          }
        }

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
            onClick = {
              filter = Filter.ALL
              if (isSuccess) {
                capsuleList.clear()
                capsuleList.addAll((state as CapsuleListScreenState.Success).capsuleList)
              }
            },
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
            onClick = {
              filter = Filter.ACTIVE
              if (isSuccess) {
                val list = ((state as CapsuleListScreenState.Success).capsuleList).filter {
                  it.isOpened == false
                }
                capsuleList.clear()
                capsuleList.addAll(list)
              }
            },
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
            onClick = {
              filter = Filter.OPENED
              if (isSuccess) {
                val list = ((state as CapsuleListScreenState.Success).capsuleList).filter {
                  it.isOpened == true
                }
                capsuleList.clear()
                capsuleList.addAll(list)
              }
            },
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

        if (isLoading)
          Column(
            modifier = Modifier
              .fillMaxSize()
              .height(1000.dp)
          ) {
            repeat(7) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .wrapContentHeight()
                  .padding(vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),

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
          }
        if (isSuccess) {
          LazyColumn(
            modifier = Modifier
              .fillMaxWidth()
              .heightIn(max = 12000.dp)
              .padding(horizontal = 3.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(0.dp),
            userScrollEnabled = false
          ) {

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
              if (p)
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
                        if (expandedCardIndex == rowItems[0])
                          expandedCardIndex = -1
                        else
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
                          if (expandedCardIndex == rowItems[1])
                            expandedCardIndex = -1
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
  }
}
