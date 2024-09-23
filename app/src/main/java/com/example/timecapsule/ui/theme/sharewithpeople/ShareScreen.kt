package com.example.timecapsule.ui.theme.sharewithpeople

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.SubTitleFontColor
import com.example.timecapsule.ui.theme.selecttime.NavigationRow
import com.example.timecapsule.ui.theme.fakedata.userList
import com.example.timecapsule.ui.theme.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.theme.util.DeviceType

@Preview
@Composable
fun ShareScreen(onNavigate: (NavigationAddCapsule) -> Unit = {}) {
  Scaffold(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary),
    containerColor = MaterialTheme.colorScheme.primary,
  ) { padding ->
    Box(
      modifier = Modifier
          .padding(padding)
          .fillMaxSize()
    )
    {
      Column(
        Modifier
          .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        ShowSelectedPeople(Modifier)
        SearchPeople()
      }
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
fun SearchPeople() {
  val isTablet = DeviceType.isTablet()
  Column(
      Modifier
          .wrapContentSize()
          .padding(20.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    OutlinedTextField(
      value = "",
      onValueChange = {},
      modifier =
      if (!isTablet) {
          Modifier
              .background(Color.White, RoundedCornerShape(30))
              .fillMaxWidth()
      } else {
          Modifier
              .widthIn(min = 500.dp, max = 800.dp)
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
      shape = RoundedCornerShape(30),
      singleLine = true
    )
    LazyColumn(
      modifier = if (!isTablet) {
          Modifier
              .fillMaxWidth()
              .wrapContentHeight()
              .clip(shape = RoundedCornerShape(10.dp))
              .padding(top = 5.dp)
              .background(MaterialTheme.colorScheme.primary)
      } else {
          Modifier
              .wrapContentSize()
              .heightIn(max = 600.dp)
              .wrapContentHeight()
              .clip(shape = RoundedCornerShape(10.dp))
              .padding(top = 5.dp)
              .background(MaterialTheme.colorScheme.primary)
      },
    ) {
      items(userList) { user ->
        UserInfo(user.username, user.name, user.imageResId)
      }
    }
  }
}

@Preview
@Composable
fun UserInfo(
  userName: String = "Shinzo",
  name: String = "Shinzo chan",
  image: Int = R.drawable.testimg1
) {
  val isTablet = DeviceType.isTablet()
  val interactionSource = remember { MutableInteractionSource() }
  Row(
      Modifier
          .wrapContentWidth()
          .wrapContentHeight()
          .padding(5.dp)
          .clickable(
              onClick = {}, interactionSource = interactionSource,
              indication = ripple()
          ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Start,
  ) {
    Image(
      painter = painterResource(id = image),
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
              .fillMaxWidth()
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
        text = userName,
        style = MaterialTheme.typography.titleSmall.copy(
          fontSize = 16.sp
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Text(
        text = name,
        style = MaterialTheme.typography.titleSmall.copy(
          fontSize = 13.sp
        ),
        color = SubTitleFontColor
      )
    }
  }
}

@Preview
@Composable
fun ShowSelectedPeople(modifier: Modifier = Modifier, disableCrossBtn: Boolean = false) {
  LazyHorizontalGrid(
    modifier = modifier
        .wrapContentSize()
        .height(120.dp),
    rows = GridCells.Fixed(1)
  ) {
    items(userList + userList) { user ->
      Profile(user.imageResId, disableCrossBtn)
    }
  }
}

@Preview
@Composable
fun Profile(imageId: Int = R.drawable.testimg1, disableCrossBtn: Boolean = false) {

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
      Image(
        painter = painterResource(id = imageId),
        contentDescription = "seleccted people",
        modifier = Modifier
            .height(70.dp)
            .width(70.dp)
            .clip(shape = CircleShape)
            .align(Alignment.Center),
        contentScale = ContentScale.Crop
      )
      if (!disableCrossBtn)
        IconButton(
          onClick = { /*TODO*/ }, modifier = Modifier
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
    Text(
      text = "Darkx6",
      style = MaterialTheme.typography.titleLarge.copy(
        fontSize =
        17.sp
      ),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
  }
}
