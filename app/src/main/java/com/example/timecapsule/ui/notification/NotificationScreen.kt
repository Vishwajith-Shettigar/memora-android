package com.example.timecapsule.ui.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.viewmodel.NotificatioViewModel
import com.example.timecapsule.viewmodel.NotificationScreenState
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
  navController: NavController = rememberNavController(),
  viewModel: NotificatioViewModel = hiltViewModel(),
  onViewClicked: (String) -> Unit = {}
) {
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
  val isTablet = DeviceType.isTablet()

  val state by viewModel.notificationListState.collectAsState()

  LaunchedEffect(Unit) {
    viewModel.getNotifications()
  }

  Scaffold(containerColor = MaterialTheme.colorScheme.primary,
    modifier = Modifier.background(MaterialTheme.colorScheme.primary),
    topBar = {
      TopAppBar(
        modifier =
        Modifier
          .fillMaxWidth()
        ,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = {
          Row(
            modifier =
            Modifier.fillMaxWidth(),
            horizontalArrangement =
            Arrangement.Center
          ) {
            Row(
              modifier =
              if (isTablet)
                Modifier.fillMaxWidth(0.6f)
              else
                Modifier.fillMaxWidth(),
            ) {
              Text(
                text = "Notifications",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontSize = 30.sp,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  lineHeight = TextUnit.Unspecified
                )
              )
            }
          }

        },
        scrollBehavior = scrollBehavior
      )
    }
  ) { innerPadding ->
    val modifier =
      if (isTablet) {
        Modifier
          .fillMaxWidth(0.6f)
      } else
        Modifier.fillMaxSize()
    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.primary)
        .fillMaxSize()
        .padding(top = innerPadding.calculateTopPadding())
        .nestedScroll(scrollBehavior.nestedScrollConnection),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      if (state is NotificationScreenState.Success) {
        if ((state as NotificationScreenState.Success).notificationList.size == 0) {

          Column(
            Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Image(
              painter = painterResource(id = com.example.timecapsule.R.drawable.empty),
              contentDescription = "No notification icon",
              Modifier
                .size(150.dp)
                .padding(10.dp)
            )
            Text(
              text = "No Notifications",
              style = MaterialTheme.typography.labelLarge,
              color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
          }

        } else
          LazyColumn(
            modifier = modifier,
          ) {
            items(
              (state as NotificationScreenState.Success).notificationList,
              key = { notification -> notification.timestamp }) {
              NotificationBar(
                image = it.imageUrl,
                username = it.username,
                text = it.body,
                isViewable = it.capsuleId != null,
                isRightRound = it.capsuleId != null,
                onViewClicked = {
                  it.capsuleId?.let { capsuleId ->
                    onViewClicked(
                      capsuleId
                    )
                  }
                }
              )
            }
          }
      } else if (state is NotificationScreenState.Loading) {
        LazyColumn(
          modifier = modifier.padding(horizontal = 20.dp)
        ) {
          items(10) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .placeholder(
                  visible = true,
                  shape = RoundedCornerShape(10.dp),
                  highlight = PlaceholderHighlight.shimmer(),
                  color = Color.Gray.copy(alpha = 0.3f),
                )
                .clip(shape = RoundedCornerShape(10.dp))
            ) {}
            Spacer(modifier = Modifier.height(10.dp))


          }
        }
      }
      else if (state is NotificationScreenState.Error){
        Column(
          Modifier.wrapContentSize(), horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(id = com.example.timecapsule.R.drawable.empty),
            contentDescription = "No notification icon",
            Modifier
              .size(150.dp)
              .padding(10.dp)
          )
          Text(
            text = "Something went wrong, please try again.",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
          )
        }

      }
    }
  }
}

@Composable
fun NotificationBar(
  image: String,
  username: String,
  text: String,
  isViewable: Boolean = false,
  isRightRound: Boolean = false,
  onViewClicked: () -> Unit = {}
) {

  val isTablet = DeviceType.isTablet()
  val textStartPadding = if (isTablet)
    10.dp
  else
    3.dp

  val modifier =
    if (isRightRound)
      Modifier
        .fillMaxWidth()
        .padding(end = 10.dp)
        .padding(vertical = 5.dp)
        .shadow(
          5.dp, RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp)
        )
        .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
        .background(MaterialTheme.colorScheme.primaryContainer)
        .padding(horizontal = 10.dp, vertical = 15.dp)
    else
      Modifier
        .fillMaxWidth()
        .padding(start = 10.dp)
        .padding(vertical = 5.dp)
        .shadow(1.dp, RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
        .clip(RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp))
        .background(MaterialTheme.colorScheme.primaryContainer)
        .padding(horizontal = 10.dp, vertical = 15.dp)


  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Row(
      modifier = Modifier.weight(0.8f),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Start
    ) {
      AsyncImage(
        model = image,
        modifier = Modifier
          .padding(3.dp)
          .padding(end = textStartPadding)
          .clip(shape = CircleShape)
          .size(60.dp),
        contentDescription = "profile picture",
        contentScale = ContentScale.Crop
      )
      Text(
        text = buildAnnotatedString {
          withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(username)
            append(" ")
          }
          append(text)
        },
        style = MaterialTheme.typography.titleMedium.copy(
          fontSize = 14.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          lineHeight = TextUnit.Unspecified
        ),
      )
    }

    if (isViewable)
      Box(modifier = Modifier.weight(0.2f)) {
        Button(
          onClick = { onViewClicked() }, modifier = Modifier
            .padding(3.dp)
            .width(90.dp)
            .height(40.dp)
            .align(Alignment.CenterEnd),
          colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
          contentPadding = PaddingValues(2.dp)
        ) {
          Text(
            text = "View", style = MaterialTheme.typography.titleMedium.copy(
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = TextUnit.Unspecified
            )
          )
        }
      }
  }
}
