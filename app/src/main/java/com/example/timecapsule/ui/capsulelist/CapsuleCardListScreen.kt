package com.example.timecapsule.ui.capsulelist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.model.CapsuleDetails
import com.example.timecapsule.R
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.viewmodel.CapsuleListScreenState
import com.example.timecapsule.viewmodel.ShowCapsulesListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun CapsuleCardListScreen(
  navController: NavController = rememberNavController(),
  modifier: Modifier = Modifier,
  viewModel: ShowCapsulesListViewModel = hiltViewModel(),
  addCapsuleBtnClicked: () -> Unit = {},
  onCapsuleClicked: (id: String) -> Unit = {}
) {
  val isTablet = DeviceType.isTablet()
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

  val state by viewModel.capsuleListState.collectAsState()

  val isLoading: Boolean = state is CapsuleListScreenState.Loading
  val isSuccess: Boolean = state is CapsuleListScreenState.Success

  var showCapsuleList: Boolean = state is CapsuleListScreenState.Success

  val capsuleList = remember {
    mutableStateListOf<CapsuleDetails>()
  }

  LaunchedEffect(key1 = Unit) {
    viewModel.getCapsulesList()
  }

  LaunchedEffect(key1 = state) {
    when (state) {
      is CapsuleListScreenState.Loading -> {
      }

      is CapsuleListScreenState.Success -> {
        capsuleList.clear()
        capsuleList.addAll((state as CapsuleListScreenState.Success).capsuleList)
      }

      is CapsuleListScreenState.Error -> {}
      CapsuleListScreenState.Idle -> {
      }
    }
  }

  Scaffold(
    containerColor = MaterialTheme.colorScheme.primary,
    topBar = {
      Column {
        TopAppBar(
          title = {
            Text(
              "Time Capsule",
              style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraBold
              )
            )
          },
          scrollBehavior = scrollBehavior,
          colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
        )
        SearchBarWithProfile(isTablet)
      }
    },
    floatingActionButton = {
      FloatingActionButton(
        onClick = { addCapsuleBtnClicked() },
        containerColor = MaterialTheme.colorScheme.secondaryContainer
      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_add),
          tint = Color.White.copy(alpha = 0.7F),
          contentDescription = "add time capsule",
          modifier = Modifier.size(30.dp)
        )
      }
    }) { paddingValues ->
    CapsuleCardList(
      modifier = Modifier
          .fillMaxSize()
          .padding(paddingValues)
          .nestedScroll(scrollBehavior.nestedScrollConnection),
      isLoading = isLoading,
      isSuccess = isSuccess,
      capsuleList = capsuleList,
      onCapsuleClicked = onCapsuleClicked
    )
  }
}

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithProfile(isTablet: Boolean = false) {
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 4.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
  ) {
    // Search Bar
    OutlinedTextField(
      value = "",
      onValueChange = {},
      modifier =
      if (!isTablet) {
          Modifier
              .weight(1f)
              .background(Color.White, RoundedCornerShape(40))
      } else {
          Modifier
              .widthIn(min = 500.dp, max = 900.dp)
              .background(Color.White, RoundedCornerShape(40))
      },
      placeholder = {
        Text(
          "Search...",
          style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray, fontSize = 15.sp)
        )
      },
      colors = TextFieldDefaults.outlinedTextFieldColors(
        containerColor = Color.White
      ),
      shape = RoundedCornerShape(40),
      singleLine = true
    )

    Spacer(modifier = Modifier.width(8.dp))

    // Profile Picture
    AsyncImage(
      model = R.drawable.onboarding_image,
      contentDescription = "Profile Picture",
      modifier = Modifier
          .size(70.dp)
          .clip(CircleShape)
          .padding(3.dp)
    )
  }
}
