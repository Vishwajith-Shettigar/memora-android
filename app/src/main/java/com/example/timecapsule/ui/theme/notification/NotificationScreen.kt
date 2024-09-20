package com.example.timecapsule.ui.theme.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.util.DeviceType

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NotificationScreen(navController: NavController = rememberNavController(),) {
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
  val isTablet = DeviceType.isTablet()

  Scaffold(containerColor = MaterialTheme.colorScheme.primary,
    modifier = Modifier.background(MaterialTheme.colorScheme.primary),
    topBar = {
      TopAppBar(
        modifier =
        Modifier.fillMaxWidth(),
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
          .padding(innerPadding)
          .nestedScroll(scrollBehavior.nestedScrollConnection),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      LazyColumn(
        modifier = modifier,
      ) {
        item {
          NotificationBar(
            image = R.drawable.testimg6,
            username = "DarkX12",
            text = "has shared time capsule with you, click on view to see more details",
            isViewable = true,
            isRightRound = true
          )
          NotificationBar(
            image = R.drawable.testimg2,
            username = "Leo13x",
            text = "has opened the family time capsule.",
            isViewable = false,
            isRightRound = false
          )
          NotificationBar(
            image = R.drawable.testimg1,
            username = "poeekX15",
            text = "has shared time capsule with you, click on view to see more details",
            isViewable = true,
            isRightRound = true
          )
          NotificationBar(
            image = R.drawable.testimg8,
            username = "etyqo13x",
            text = "has opened the family time capsule.",
            isViewable = false,
            isRightRound = false
          )
          NotificationBar(
            image = R.drawable.testimg3,
            username = "ektyX12",
            text = "has shared time capsule with you, click on view to see more details",
            isViewable = true,
            isRightRound = true
          )
          NotificationBar(
            image = R.drawable.testimg9,
            username = "hardy34",
            text = "has opened the family time capsule.",
            isViewable = false,
            isRightRound = false
          )
          NotificationBar(
            image = R.drawable.testimg7,
            username = "oiokX12",
            text = "has shared time capsule with you, click on view to see more details",
            isViewable = true,
            isRightRound = true
          )
          NotificationBar(
            image = R.drawable.testimg2,
            username = "Leo13x",
            text = "has opened the family time capsule.",
            isViewable = false,
            isRightRound = false
          )
          NotificationBar(
            image = R.drawable.testimg6,
            username = "DarkX12",
            text = "has shared time capsule with you, click on view to see more details",
            isViewable = true,
            isRightRound = true
          )
          NotificationBar(
            image = R.drawable.testimg2,
            username = "Leo13x",
            text = "has opened the family time capsule.",
            isViewable = false,
            isRightRound = false
          )
          NotificationBar(
            image = R.drawable.testimg6,
            username = "DarkX12",
            text = "has shared time capsule with you, click on view to see more details",
            isViewable = true,
            isRightRound = true
          )
          NotificationBar(
            image = R.drawable.testimg2,
            username = "Leo13x",
            text = "has opened the family time capsule.",
            isViewable = false,
            isRightRound = false
          )
        }
      }
    }
  }
}

@Composable
fun NotificationBar(
  image: Int,
  username: String,
  text: String,
  isViewable: Boolean = false,
  isRightRound: Boolean = false
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
      Image(
        painter = painterResource(id = image),
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
          onClick = { /*TODO*/ }, modifier = Modifier
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
