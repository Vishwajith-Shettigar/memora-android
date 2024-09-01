package com.example.timecapsule.ui.theme.createcapsule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SelectTimeScreen() {
  Scaffold(modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.primary)
      .padding(vertical = 30.dp),
    containerColor = MaterialTheme.colorScheme.primary,
    topBar = {
      BackRow()
    },
    bottomBar = {
      NavigationRow()
    }
  ) { padding ->
    SelectTime(modifier = Modifier.padding(padding))
  }
}
