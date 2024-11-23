package com.example.timecapsule.ui.capsuledetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.model.CapsuleDetails
import com.example.timecapsule.R
import com.example.timecapsule.ui.capsulelist.TimerPlaceholder
import com.example.timecapsule.ui.review.SelectedLocation
import com.example.timecapsule.ui.review.SharedWithALlIcon
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.sharewithpeople.Profile
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.viewmodel.DisplayCapsuleDetailsState
import com.example.timecapsule.viewmodel.DisplayCapsuleDetailsViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import com.google.android.gms.maps.model.LatLng

@Preview
@Composable
fun previewProfile() {
  Profile(
    userName = "dark6v",
    "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/default_profile_pictures%2Ftestimg3.jpg?alt=media&token=0f8ad9af-9661-462f-9dfd-d99612109170",
    true,
    remove = {}
  )
}

@Composable
fun CapsuleDetailsScreen(
  capsuleId: String = "",
  viewModel: DisplayCapsuleDetailsViewModel = hiltViewModel(),
  onBack: () -> Unit = {}
) {

  LaunchedEffect(Unit) {
    viewModel.getCapsuleDetails(capsuleId)
  }

  val capsuleDetailsState by viewModel.capsuleDetailsState.collectAsState()

  val isLoading = capsuleDetailsState is DisplayCapsuleDetailsState.Loading

  val isSuccess = capsuleDetailsState is DisplayCapsuleDetailsState.Success

  var capsuleDetails: CapsuleDetails? by remember {
    mutableStateOf(null)
  }

  LaunchedEffect(capsuleDetailsState) {
    when (capsuleDetailsState) {
      is DisplayCapsuleDetailsState.Success -> {
        capsuleDetails =
          (capsuleDetailsState as DisplayCapsuleDetailsState.Success).capsuleDetails
      }

      is DisplayCapsuleDetailsState.Error -> {}
      is DisplayCapsuleDetailsState.Loading -> {}
    }
  }

  Scaffold(
    containerColor = LightBlue,
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
    ) {
      item {
        TopPart(
          isLoading = isLoading,
          isSuccess = isSuccess,
          imageUrl = capsuleDetails?.imageUrl,
          onBack = onBack
        )
      }
      item {
        BottomPart(
          isLoading = isLoading,
          isSuccess = isSuccess, capsuleDetails
        )
      }

    }
  }
}

@Composable
fun TopPart(
  isLoading: Boolean = true,
  isSuccess: Boolean = false,
  imageUrl: String? = null,
  modifier: Modifier = Modifier,
  onBack: () -> Unit
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(280.dp)
  ) {
    Box(
      modifier = modifier
        .fillMaxWidth()
        .height(280.dp)
        .clip(
          shape =
          RoundedCornerShape(bottomStart = 70.dp, bottomEnd = 70.dp)
        )
        .shadow(8.dp, shape = RoundedCornerShape(bottomStart = 70.dp, bottomEnd = 70.dp))
        .background(LightBlue)
        .zIndex(1f),
    ) {
      BackRow {
        onBack()
      }

      if (isLoading) {
        Box(
          modifier = Modifier
            .size(280.dp)
            .align(Alignment.Center)
            .placeholder(
              visible = isLoading,
              color = Color.Gray.copy(alpha = 0.1f),
              highlight = PlaceholderHighlight.shimmer()
            )
        )
      } else if (isSuccess) {
        AsyncImage(
          model = imageUrl,
          contentDescription = "Capsule model",
          modifier = Modifier
            .size(280.dp)
            .align(Alignment.Center)
        )
      }
    }
  }
}

@Composable
fun BottomPart(
  isLoading: Boolean = true,
  isSuccess: Boolean = false,
  capsuleDetails: CapsuleDetails? = null
) {
  var isPaneVisible by remember {
    mutableStateOf(false)
  }
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 10.dp)
      .fillMaxHeight()
      .background(MaterialTheme.colorScheme.primary),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = 20.dp, vertical = 10.dp),
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Shimmer effect for the title text
      (if (isLoading) "" else if (isSuccess) capsuleDetails?.title else "")?.let {
        Text(
          text = it,
          style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          ),
          modifier = Modifier
            .placeholder(
              visible = isLoading,
              color = Color.Gray.copy(alpha = 0.1f),
              highlight = PlaceholderHighlight.shimmer()
            )
            .wrapContentSize()
        )
      }

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
          tint = Color.LightGray
        )
      }
    }

    // Shimmer effect for loading placeholder
    if (isLoading) {
      Box(
        modifier = Modifier
          .size(100.dp, 20.dp)
          .placeholder(
            visible = isLoading,
            color = Color.Gray.copy(alpha = 0.1f),
            highlight = PlaceholderHighlight.shimmer()
          )
      )
    }

    if (isSuccess) {
      capsuleDetails?.let {
        TimerPlaceholder(it.time, isSmallSize = false)
      }
    }

    AnimatedVisibility(visible = isPaneVisible) {
      if (isSuccess) {
        capsuleDetails?.description?.let {
          Text(
            modifier = Modifier.padding(10.dp),
            text = it,
            style = MaterialTheme.typography.bodyMedium
          )
        }
      }
    }

    // Shared with people.
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
      // Shimmer effect for the title text
      Text(
        modifier = Modifier
          .padding(bottom = 5.dp)
          .placeholder(
            visible = isLoading,
            highlight = PlaceholderHighlight.shimmer(),
            color = Color.Gray.copy(alpha = 0.3f)
          ),
        text = stringResource(id = R.string.shared_with_capsule_details_screen),
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 20.sp,
        )
      )

      val horizontalGridModifier: Modifier = if (isLoading) {
        Modifier.height(70.dp)
      } else if (isSuccess)
        Modifier.height(120.dp)
      else
        Modifier

      // LazyHorizontalGrid with Shimmer effect
      LazyHorizontalGrid(
        modifier = horizontalGridModifier
          .fillMaxWidth(),
        rows = GridCells.Fixed(1)
      ) {

        if (isLoading) {
          items(7) {
            Box(
              modifier = Modifier
                .size(70.dp)
                .placeholder(
                  visible = isLoading,
                  shape = CircleShape,
                  highlight = PlaceholderHighlight.shimmer(),
                  color = Color.Gray.copy(alpha = 0.3f),
                )
                .clip(shape = CircleShape)
            )
          }
        }

        if (isSuccess) {
          capsuleDetails?.users?.let {
            items(it) { user ->
              Profile(
                isOwner = user["isOwner"] as Boolean,
                userName = user["userName"] as String,
                imageUrl = user["imageUrl"] as String,
                disableCrossBtn = true,
                remove = {})
            }
          }
          if (capsuleDetails?.isSharedWithAll == true) {
            item {
              SharedWithALlIcon()
            }
          }
        }
      }
    }

    // SelectedLocation with shimmer effect
    if (isLoading)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(200.dp)
          .padding(horizontal = 20.dp)
          .clip(RoundedCornerShape(20.dp))
          .placeholder(
            visible = isLoading,
            highlight = PlaceholderHighlight.shimmer(),
            color = Color.Gray.copy(alpha = 0.3f)
          )
      )

    if (isSuccess)
      capsuleDetails?.location?.let {
        SelectedLocation(
          latlang = LatLng(it.latitude, it.longitude),
          modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 20.dp)
            .placeholder(
              visible = isLoading,
              highlight = PlaceholderHighlight.shimmer(),
              color = Color.Gray.copy(alpha = 0.3f)
            )
        )
      }
  }
}
