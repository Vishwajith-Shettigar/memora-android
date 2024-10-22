package com.example.timecapsule.ui.capsuledetails

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material3.placeholder
import coil.compose.AsyncImage
import com.example.model.CapsuleDetails
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.DMSerifText
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.RubikBubble
import com.example.timecapsule.ui.capsulelist.TimerPlaceholder
import com.example.timecapsule.ui.fakedata.userList
import com.example.timecapsule.ui.review.MapPreviewCard
import com.example.timecapsule.ui.review.SelectedLocation
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.sharewithpeople.Profile
import com.example.timecapsule.ui.sharewithpeople.ShowSelectedPeople
import com.example.timecapsule.viewmodel.DisplayCapsuleDetailsState
import com.example.timecapsule.viewmodel.DisplayCapsuleDetailsViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp

@Preview
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
          (capsuleDetailsState as DisplayCapsuleDetailsState.Success).calsuleDetails
      }

      is DisplayCapsuleDetailsState.Error -> {}
      DisplayCapsuleDetailsState.Loading -> {}
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

      if (isLoading)
        AsyncImage(
          model = imageUrl,
          contentDescription = "Capsule model",
          modifier = Modifier
              .size(280.dp)
              .align(Alignment.Center)
        )

      if (isSuccess) {
        Log.e("pokemon", "Success")

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
  isSuccess: Boolean = false, capsuleDetails: CapsuleDetails? = null
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
      Text(
        text = "The Family Capsule",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 25.sp,
          fontWeight = FontWeight.ExtraBold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
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

    TimerPlaceholder(Timestamp.now(), isSmallSize = false)

    AnimatedVisibility(visible = isPaneVisible) {
      Text(
        modifier = Modifier.padding(10.dp),
        text = "Lorem ipsum lorebe hee pokemon yuuiooe g" +
          "eo man jioej htyuya ppoe jjie got yo knoww hat im s" +
          "aying is you are not aneoyugh hd"
      )
    }

    Column(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
      Text(
        modifier = Modifier.padding(bottom = 5.dp),
        text = stringResource(id = R.string.shared_with_capsule_details_screen),
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 20.sp,
        )
      )
      LazyHorizontalGrid(
        modifier = Modifier
            .wrapContentSize()
            .height(120.dp),
        rows = GridCells.Fixed(1)
      ) {
        items(1) {
          Profile(
            isOwner = true,
            userName = "dark6v",
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/default_profile_pictures%2Ftestimg3.jpg?alt=media&token=0f8ad9af-9661-462f-9dfd-d99612109170",
            disableCrossBtn = true,
            remove = {}
          )
        }
      }
    }
  }
  SelectedLocation(
    latlang = LatLng(0.00, 0.99),
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = 20.dp)
  )
}

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

//@Composable
//fun CapsuleDetailsScreen(
//  capsuleId: String = "",
//  viewModel: DisplayCapsuleDetailsViewModel = hiltViewModel(),
//  onBack: () -> Unit = {}
//) {
//
//  LaunchedEffect(Unit) {
//    Log.e("pokemon", capsuleId)
//    viewModel.getCapsuleDetails(capsuleId)
//  }
//
//  val capsuleDetailsState by viewModel.capsuleDetailsState.collectAsState()
//
//  val isLoading = capsuleDetailsState is DisplayCapsuleDetailsState.Loading
//
//  val isSuccess = capsuleDetailsState is DisplayCapsuleDetailsState.Success
//
//  var capsuleDetails: CapsuleDetails? by remember {
//    mutableStateOf(null)
//  }
//
//  LaunchedEffect(capsuleDetailsState) {
//    when (capsuleDetailsState) {
//      is DisplayCapsuleDetailsState.Success -> {
//        capsuleDetails =
//          (capsuleDetailsState as DisplayCapsuleDetailsState.Success).calsuleDetails
//      }
//
//      is DisplayCapsuleDetailsState.Error -> {}
//      DisplayCapsuleDetailsState.Loading -> {}
//    }
//  }
//
//  Scaffold(
//    containerColor = LightBlue,
//  ) { innerPadding ->
//    LazyColumn(
//      modifier = Modifier
//          .fillMaxSize()
//          .background(MaterialTheme.colorScheme.primary)
//    ) {
//      item {
//        TopPart(
//          isLoading = isLoading,
//          isSuccess = isSuccess,
//          imageUrl = capsuleDetails?.imageUrl,
//          onBack = onBack
//        )
//      }
//      item {
//        BottomPart(
//          isLoading = isLoading,
//          isSuccess = isSuccess, capsuleDetails
//        )
//      }
//
//    }
//
//  }
//}
//
//@Composable
//fun TopPart(
//  isLoading: Boolean = true,
//  isSuccess: Boolean = false,
//  imageUrl: String? = null,
//  modifier: Modifier = Modifier,
//  onBack: () -> Unit
//) {
//  Box(
//    modifier = modifier
//        .fillMaxWidth()
//        .height(280.dp)
//  ) {
//    Box(
//      modifier = modifier
//          .fillMaxWidth()
//          .height(280.dp)
//          .clip(
//              shape =
//              RoundedCornerShape(bottomStart = 70.dp, bottomEnd = 70.dp)
//          )
//          .shadow(8.dp, shape = RoundedCornerShape(bottomStart = 70.dp, bottomEnd = 70.dp))
//          .background(LightBlue)
//          .zIndex(1f),
//    ) {
//      BackRow {
//        onBack()
//      }
//
//      if (isLoading) {
//        // Shimmer effect for the loading image
//        Box(
//          modifier = Modifier
//            .size(280.dp)
//            .align(Alignment.Center)
//            .placeholder(
//              visible = isLoading,
//              color = Color.Gray.copy(alpha = 0.1f),
//              highlight = shimmer()
//            )
//        )
//      } else if (isSuccess) {
//        Log.e("pokemon", "Success")
//
//        AsyncImage(
//          model = imageUrl,
//          contentDescription = "Capsule model",
//          modifier = Modifier
//              .size(280.dp)
//              .align(Alignment.Center)
//        )
//      }
//
//    }
//
//  }
//}
//
//@Composable
//fun BottomPart(
//  isLoading: Boolean = true,
//  isSuccess: Boolean = false,
//  capsuleDetails: CapsuleDetails? = null
//) {
//  var isPaneVisible by remember {
//    mutableStateOf(false)
//  }
//  Column(
//    modifier = Modifier
//        .fillMaxWidth()
//        .padding(vertical = 10.dp)
//        .fillMaxHeight()
//        .background(MaterialTheme.colorScheme.primary),
//    horizontalAlignment = Alignment.CenterHorizontally,
//    verticalArrangement = Arrangement.Top
//  ) {
//    Row(
//      modifier = Modifier
//          .fillMaxWidth()
//          .wrapContentHeight()
//          .padding(horizontal = 20.dp, vertical = 10.dp),
//      horizontalArrangement = Arrangement.Center,
//      verticalAlignment = Alignment.CenterVertically
//    ) {
//      // Shimmer effect for the title text
//      Text(
//        text = if (isLoading) "" else "The Family Capsule",
//        style = MaterialTheme.typography.titleLarge.copy(
//          fontSize = 25.sp,
//          fontWeight = FontWeight.ExtraBold,
//          color = MaterialTheme.colorScheme.onSurfaceVariant
//        ),
//        modifier = Modifier
//          .placeholder(
//            visible = isLoading,
//            color = Color.Gray.copy(alpha = 0.1f),
//            highlight = shimmer()
//          )
//          .fillMaxWidth()
//      )
//
//      IconButton(
//        onClick = { isPaneVisible = !isPaneVisible },
//      ) {
//        Icon(
//          painter =
//          if (isPaneVisible)
//            painterResource(id = R.drawable.ic_drop_down)
//          else
//            painterResource(id = R.drawable.ic_drop_up),
//          contentDescription = "More Options",
//          tint = Color.LightGray
//        )
//      }
//    }
//
//    // Shimmer effect for loading placeholder
//    if (isLoading) {
//      Box(
//        modifier = Modifier
//          .size(100.dp, 20.dp)
//          .placeholder(
//            visible = isLoading,
//            color = Color.Gray.copy(alpha = 0.1f),
//            highlight = shimmer()
//          )
//      )
//    } else {
//      TimerPlaceholder(Timestamp.now(), isSmallSize = false)
//    }
//
//    AnimatedVisibility(visible = isPaneVisible) {
//      Text(
//        modifier = Modifier.padding(10.dp),
//        text = "Lorem ipsum...",
//        style = MaterialTheme.typography.bodyMedium
//      )
//    }
//
//    // Other details section
//    Column(
//      modifier = Modifier
//          .fillMaxWidth()
//          .wrapContentHeight()
//          .padding(horizontal = 20.dp, vertical = 20.dp)
//    ) {
//      Text(
//        modifier = Modifier.padding(bottom = 5.dp),
//        text = stringResource(id = R.string.shared_with_capsule_details_screen),
//        style = MaterialTheme.typography.titleLarge.copy(
//          fontSize = 20.sp,
//        )
//      )
//      LazyHorizontalGrid(
//        modifier = Modifier
//            .wrapContentSize()
//            .height(120.dp),
//        rows = GridCells.Fixed(1)
//      ) {
//        items(1) {
//          Profile(
//            isOwner = true,
//            userName = "dark6v",
//            imageUrl = "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/default_profile_pictures%2Ftestimg3.jpg?alt=media&token=0f8ad9af-9661-462f-9dfd-d99612109170",
//            disableCrossBtn = true,
//            remove = {}
//          )
//        }
//      }
//    }
//  }
//}
