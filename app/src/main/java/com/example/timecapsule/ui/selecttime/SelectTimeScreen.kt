package com.example.timecapsule.ui.selecttime

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel

@Preview
@Composable
fun SelectTimeScreen(
  viewModel: CapsuleCreationViewModel = hiltViewModel(),
  onNavigate: (NavigationAddCapsule) -> Unit = {}
) {

  val context = LocalContext.current

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
  ) { innerPadding ->
    Box(
      modifier = Modifier
          .fillMaxSize()
          .padding(
              start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
              end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
              top = innerPadding.calculateTopPadding()
          )
    ) {
      SelectTime(modifier = Modifier, viewModel = viewModel)
      Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .align(Alignment.BottomCenter)
            .zIndex(2f)
      ) {
        NavigationRow(showBackBtn = false) { navigationFlow ->
          if (viewModel.selectedTimeStamp == null) {
            Toast.makeText(context, "Please select data and time", Toast.LENGTH_SHORT).show()
          } else
            onNavigate(navigationFlow)
        }
      }
    }
  }
}
