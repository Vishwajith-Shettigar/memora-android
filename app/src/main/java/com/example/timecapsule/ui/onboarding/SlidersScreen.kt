package com.example.timecapsule.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.timecapsule.ui.theme.LightBlue
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalPagerApi::class)
@Composable
fun SlidersScreen() {
  val pagerState = rememberPagerState()
  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent)
  ) {
    HorizontalPager(
      state = pagerState,
      count = 3,
      modifier = Modifier.fillMaxSize()
    ) { pageIndex ->
      Screen(pageIndex = pageIndex)
    }

    HorizontalPagerIndicator(
      pagerState = pagerState,
      modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(bottom = 20.dp)
    )
  }
}

@Composable
fun Screen(pageIndex: Int) {
  // Customize this content based on the pageIndex
  when (pageIndex) {
    0 -> {
      // First screen content
      SlideOne()
    }

    1 -> {
      // Second screen content
      SlideTwo()
    }

    2 -> {
      // Third screen content
      SlideThree()
    }
  }
}