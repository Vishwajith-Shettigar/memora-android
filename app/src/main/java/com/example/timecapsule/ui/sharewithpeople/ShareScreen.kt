package com.example.timecapsule.ui.sharewithpeople

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Chip
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ripple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.model.UserDetails
import com.example.timecapsule.R
import com.example.timecapsule.ui.review.SharedWithALlIcon
import com.example.timecapsule.ui.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.selecttime.NavigationRow
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.SubTitleFontColor
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel
import com.example.timecapsule.viewmodel.SearchPeopleState

@Composable
fun ShareScreen(
  viewModel: CapsuleCreationViewModel = hiltViewModel(),
  onNavigate: (NavigationAddCapsule) -> Unit = {}
) {
  val isTablet = DeviceType.isTablet()
  val scaffoldPadding = if (isTablet) PaddingValues(30.dp) else PaddingValues(0.dp)

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .padding(scaffoldPadding)
      .background(MaterialTheme.colorScheme.primary),
    containerColor = MaterialTheme.colorScheme.primary,
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .padding(
          start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
          end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
          top = innerPadding.calculateTopPadding()
        )
        .fillMaxSize()
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(bottom = 30.dp), verticalArrangement = Arrangement.Top
      ) {
        // Visibility Text
        AnimatedVisibility(
          visible = viewModel.selectedPeoples.size == 1 && viewModel.addedEmails.isEmpty()
        ) {
          Text(
            text = "You can select people by searching their username or adding email.",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
              .padding(start = 15.dp, top = 50.dp, bottom = 8.dp)

          )
        }

        // Content Section
        Column(
          Modifier
            .wrapContentSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.Top,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          if (viewModel.selectedPeoples.size > 1)
            ShowSelectedPeople(
              modifier = Modifier,
              selectedPeoples = viewModel.selectedPeoples,
              ownerUserId = viewModel.userId
            ) { userName ->
              viewModel.selectedPeoples.removeIf { it.userName == userName }
            }

          if (viewModel.addedEmails.size > 0)
            ShowAddedEmails(
              addedEmails = viewModel.addedEmails
            ) { email ->
              viewModel.addedEmails.remove(email)
            }

          // Non-scrollable SearchPeople
          SearchPeople(viewModel) { user ->
            if (!viewModel.selectedPeoples.contains(user)) {
              viewModel.selectedPeoples.add(user)
            }
          }
        }
      }

      // Bottom Navigation Row
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(0.dp)
          .align(Alignment.BottomCenter)
          .zIndex(2f)
      ) {
        NavigationRow { navigationFlow ->
          onNavigate(navigationFlow)
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPeople(
  viewModel: CapsuleCreationViewModel,
  addSelectedPeople: (UserDetails) -> Unit = {}
) {

  var isEmailEnabled by remember {
    mutableStateOf(false)
  }

  val isTablet = DeviceType.isTablet()

  var searchResult = remember { mutableStateListOf<UserDetails>() }
  var searchValue by remember { mutableStateOf("") }

  val searchPeopleState by viewModel.searchPeopleState.collectAsState()

  LaunchedEffect(searchValue) {
    if (searchValue.isNotBlank()) {
      if (!isEmailEnabled)
        viewModel.searchUsers(searchValue)
    }
  }

  LaunchedEffect(searchPeopleState) {
    when (searchPeopleState) {
      is SearchPeopleState.Success -> {
        searchResult.clear()
        searchResult.addAll((searchPeopleState as SearchPeopleState.Success).data)
      }

      is SearchPeopleState.Error -> {
        // Handle error
      }

      else -> {}
    }
  }

  Column(
    Modifier
      .wrapContentSize(),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Top
  ) {

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {

      IconButton(
        onClick = {
          isEmailEnabled = !isEmailEnabled
          searchValue = ""
        }, modifier = Modifier
          .clip(RoundedCornerShape(20.dp))
          .border(
            1.dp,
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          .padding(horizontal = 5.dp)
      ) {
        Icon(
          painter = painterResource(id = com.example.timecapsule.R.drawable.ic_email),
          contentDescription = "email icon",
          tint =
          if (isEmailEnabled)
            LightBlue
          else
            MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      OutlinedTextField(
        value = searchValue,
        onValueChange = { searchValue = it },
        modifier = Modifier
          .background(Color.White, RoundedCornerShape(30)),
        placeholder = {
          Text(
            if (isEmailEnabled)
              "Please enter email."
            else
              "Search username.",
            style = MaterialTheme.typography.titleSmall.copy(color = Color.Gray, fontSize = 15.sp)
          )
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
          containerColor = Color.White,
          focusedTextColor = Color.Black
        ),
        shape = RoundedCornerShape(30),
        singleLine = true
      )
    }
    AnimatedVisibility(isEmailEnabled)
    {
      OutlinedButton(onClick = {

        if ((!searchValue.isEmpty()) && (!viewModel.addedEmails.contains(searchValue)) && searchValue.contains(
            '@'
          )
        ) {
          viewModel.addedEmails.add(searchValue.trim())
          searchValue = ""
        }
      }) {
        Text(
          text = "Add", modifier = Modifier.align(Alignment.CenterVertically),
          style = MaterialTheme.typography.titleSmall,
          color = LightBlue
        )
      }
    }
    // Search Results (Non-scrollable LazyColumn)
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .heightIn(max = 200.dp) // Limit LazyColumn height
        .clip(shape = RoundedCornerShape(10.dp))
        .padding(top = 20.dp)
        .background(MaterialTheme.colorScheme.primary),
    ) {
      searchResult.forEach { user ->
        UserInfo(
          user = user,
          addSelectedPeople
        )
      }
    }
  }
}


@Composable
fun UserInfo(
  user: UserDetails,
  addSelectedPeople: (UserDetails) -> Unit = {}
) {
  val isTablet = DeviceType.isTablet()
  val interactionSource = remember { MutableInteractionSource() }

  val rowMod = if (isTablet)
    Modifier.widthIn(min = 500.dp, max = 800.dp)
  else
    Modifier
      .fillMaxWidth()

  Row(
    rowMod
      .wrapContentHeight()
      .padding(5.dp)
      .clickable(
        onClick = {}, interactionSource = interactionSource,
        indication = ripple()
      ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween,
  ) {
    Row(
      Modifier
        .wrapContentWidth()
        .wrapContentHeight(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start,
    ) {
      AsyncImage(
        model = user.imageUrl,
        contentDescription = "seleccted people",
        modifier = Modifier
          .height(50.dp)
          .width(50.dp)
          .clip(shape = CircleShape),
        contentScale = ContentScale.Crop
      )
      Column(
        modifier = if (!isTablet) {
          Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp)
        } else {
          Modifier
            .widthIn(min = 500.dp, max = 900.dp)
            .wrapContentHeight()
            .padding(horizontal = 10.dp)

        }
      ) {
        Text(
          text = user.userName,
          style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 16.sp
          ),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
          text = user.firstName + " " + user.lastName,
          style = MaterialTheme.typography.titleSmall.copy(
            fontSize = 13.sp
          ),
          color = SubTitleFontColor
        )
      }
    }

    IconButton(
      onClick = { addSelectedPeople(user) },
    ) {
      Icon(
        painter = painterResource(id = com.example.timecapsule.R.drawable.ic_add),
        contentDescription = "add icon",
        tint = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ShowAddedEmails(addedEmails: List<String>, onRemoveCLicked: (String) -> Unit) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = Modifier
      .padding(vertical = 10.dp)
      .fillMaxWidth()
      .heightIn(max = 300.dp)
  ) {
    items(addedEmails) {
      EmailChip(email = it, onRemoveCLicked)
    }
  }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmailChip(email: String, onRemoveClick: (String) -> Unit) {
  Chip(modifier = Modifier
    .fillMaxWidth()
    .padding(1.dp), onClick = { /*TODO*/ }) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = email,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(0.8F)
      )
      IconButton(onClick = { onRemoveClick(email) }, modifier = Modifier.weight(0.2F)) {
        Icon(
          painter = painterResource(id = com.example.timecapsule.R.drawable.ic_close),
          contentDescription = "close icon", Modifier.size(20.dp)
        )
      }
    }
  }
}

@Preview
@Composable
fun ShowSelectedPeople(
  modifier: Modifier = Modifier,
  disableCrossBtn: Boolean = false,
  selectedPeoples: MutableList<UserDetails> = mutableListOf(),
  ownerUserId: String? = null,
  showSharedWithALl: Boolean = false,
  remove: (String) -> Unit = {},
) {

  LazyHorizontalGrid(
    modifier = modifier
      .wrapContentSize()
      .heightIn(max = 105.dp),
    rows = GridCells.Fixed(1)
  ) {
    items(selectedPeoples) { user ->
      if (disableCrossBtn || user.userId != ownerUserId)
        Profile(
          userName = user.userName,
          user.imageUrl,
          disableCrossBtn,
          remove = remove
        )
    }
    if (showSharedWithALl)
      item {
        SharedWithALlIcon()
      }
  }
}

@Preview
@Composable
fun Profile(
  userName: String = "",
  imageUrl: String = "",
  disableCrossBtn: Boolean = false,
  isOwner: Boolean = false,
  size: Dp = 70.dp,
  hideUserName: Boolean = false,
  remove: (String) -> Unit = {}
) {

  val imageModifier = if (isOwner)
    Modifier
      .height(size)
      .width(size)
      .clip(shape = CircleShape)
      .border(2.dp, color = Color.Red, shape = CircleShape)
  else
    Modifier
      .height(size)
      .width(size)
      .clip(shape = CircleShape)


  Column(
    modifier = Modifier
      .wrapContentHeight()
      .wrapContentWidth()
      .padding(horizontal = 4.dp)
      .background(Color.Transparent),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    Box(
      modifier = Modifier
        .wrapContentHeight()
        .wrapContentWidth()
        .padding(horizontal = 4.dp)
        .background(Color.Transparent),
    )
    {
      AsyncImage(
        model = imageUrl,
        contentDescription = "selected people",
        modifier = imageModifier.align(Alignment.Center),
        contentScale = ContentScale.Crop
      )
      if (!disableCrossBtn)
        IconButton(
          onClick = { remove(userName) }, modifier = Modifier
            .height(30.dp)
            .width(30.dp)
            .align(Alignment.TopEnd)
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = "seleccted people",
            tint = Color.Gray, modifier = Modifier
              .height(30.dp)
              .width(30.dp)
              .align(Alignment.Center)
          )
        }

    }
    if (!hideUserName)
      Text(
        text = userName,
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize =
          17.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
  }
}
