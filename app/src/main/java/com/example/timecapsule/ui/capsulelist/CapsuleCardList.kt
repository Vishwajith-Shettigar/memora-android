package com.example.timecapsule.ui.capsulelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.model.CapsuleDetails
import com.example.timecapsule.ui.util.DeviceType

@Composable
fun CapsuleCardList(
  modifier: Modifier = Modifier,
  isLoading: Boolean = false,
  isSuccess: Boolean = false,
  capsuleList: List<CapsuleDetails> = mutableListOf(),
  onCapsuleClicked: (id: String) -> Unit = {}
) {
  if (DeviceType.isTablet()) {
    CapsuleCardListTablet(
      modifier = modifier, isLoading = isLoading,
      isSuccess = isSuccess,
      capsuleList = capsuleList, onCapsuleClicked = onCapsuleClicked
    )
  } else {
    CapsuleCardListMobile(
      modifier = modifier, isLoading = isLoading,
      isSuccess = isSuccess,
      capsuleList = capsuleList, onCapsuleClicked = onCapsuleClicked
    )
  }
}

@Composable
fun CapsuleCardListMobile(
  modifier: Modifier = Modifier,
  isLoading: Boolean = false,
  isSuccess: Boolean = false,
  capsuleList: List<CapsuleDetails> = mutableListOf(),
  onCapsuleClicked: (id: String) -> Unit = {}
) {

  if (isLoading)
    Column(
      modifier = modifier,
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      CircularProgressIndicator(
        modifier = Modifier
          .size(44.dp),
        color = Color.White,
        strokeWidth = 2.dp
      )
    }
  if (isSuccess)
    LazyVerticalStaggeredGrid(
      modifier = modifier.background(Color.Transparent), columns = StaggeredGridCells.Fixed(2),
      contentPadding = PaddingValues(8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalItemSpacing = 8.dp
    ) {
      items(capsuleList.size) { it ->
        CapsuleCard(2, capsuleDetails = capsuleList[it], onCapsuleClicked = onCapsuleClicked)
      }
    }
}

@Composable
fun CapsuleCardListTablet(
  modifier: Modifier = Modifier,
  isLoading: Boolean = false,
  isSuccess: Boolean = false,
  capsuleList: List<CapsuleDetails> = mutableListOf(),
  onCapsuleClicked: (id: String) -> Unit = {}
) {

  if (isLoading)
    Column(
      modifier = modifier,
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {

      CircularProgressIndicator(
        modifier = Modifier
          .size(44.dp),
        color = Color.White,
        strokeWidth = 2.dp
      )
    }

  if (isSuccess)
    LazyVerticalStaggeredGrid(
      columns = StaggeredGridCells.Adaptive(minSize = 350.dp),
      modifier = modifier
          .fillMaxSize()
          .background(Color.Transparent),
      contentPadding = PaddingValues(8.dp),
      horizontalArrangement = Arrangement.spacedBy(10.dp),
      verticalItemSpacing = 8.dp,
      content = {
        items(capsuleList.size) {
          CapsuleCard(1, capsuleDetails = capsuleList[it], onCapsuleClicked = onCapsuleClicked)
        }
      }
    )
}