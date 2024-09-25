package com.example.timecapsule.ui.selecttime

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun SelectTimeScreen(
  navController: NavController = rememberNavController(),
  onNavigate: (NavigationAddCapsule) -> Unit = {}
) {
  Scaffold(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
        .padding(top = 30.dp),
    containerColor = MaterialTheme.colorScheme.primary,
    topBar = {
      BackRow {
        onNavigate(NavigationAddCapsule.BACK)
      }
    },
  ) { padding ->
    Box(
      modifier = Modifier
          .padding(padding)
          .fillMaxSize()
    ) {
      SelectTime(modifier = Modifier)
      Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .align(Alignment.BottomCenter)
            .zIndex(2f)
      ) {
        NavigationRow(showBackBtn = false) { navigationFlow ->
          onNavigate(navigationFlow)
        }
      }
    }
  }
}
