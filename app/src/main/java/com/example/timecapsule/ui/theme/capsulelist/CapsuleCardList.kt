package com.example.timecapsule.ui.theme.capsulelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.timecapsule.ui.theme.util.DeviceType

@Composable
fun CapsuleCardList(modifier: Modifier = Modifier) {
  if (DeviceType.isTablet()) {
    CapsuleCardListTablet(modifier)
  } else {
    CapsuleCardListMobile(modifier)
  }
}

@Composable
fun CapsuleCardListMobile(modifier: Modifier = Modifier) {
  LazyVerticalStaggeredGrid(
    modifier = modifier.background(Color.Transparent), columns = StaggeredGridCells.Fixed(2),
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalItemSpacing = 8.dp
  ) {
    items(10) {
      CapsuleCard(2)
    }
  }
}

@Composable
fun CapsuleCardListTablet(modifier: Modifier = Modifier) {
  LazyVerticalStaggeredGrid(
    columns = StaggeredGridCells.Adaptive(minSize = 400.dp),
    modifier = modifier
        .fillMaxSize()
        .background(Color.Transparent),
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    verticalItemSpacing = 8.dp,
    content = {
      items(10) { index ->
        CapsuleCard(1)
      }
    }
  )
}