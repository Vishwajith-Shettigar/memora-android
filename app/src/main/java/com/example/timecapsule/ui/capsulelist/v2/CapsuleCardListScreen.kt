package com.example.timecapsule.ui.capsulelist.v2

import android.util.Log
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CapsuleCardListScreen() {
  Scaffold()
  { innerPadding ->

    Column(
      modifier = Modifier
        .padding(innerPadding)
        .padding(vertical = 10.dp, horizontal = 2.dp)
    ) {

      var expandedCardIndex by remember { mutableStateOf(-1) } // Track expanded card

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
      ) {

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
