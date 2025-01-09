package com.example.timecapsule.ui.viewprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.model.UserDetails
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.util.DeviceType
import com.example.timecapsule.viewmodel.ViewProfileState
import com.example.timecapsule.viewmodel.ViewProfileViewModel
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProfileScreen(
  userId: String,
  viewModel: ViewProfileViewModel = hiltViewModel(),
  onBackClick: () -> Unit
) {

  val state by viewModel.viewProfileState.collectAsState()

  var userDetails: UserDetails? by remember {
    mutableStateOf(null)
  }

  LaunchedEffect(Unit) {
    viewModel.loadUserDetails(userId)
  }

  val isTablet = DeviceType.isTablet()

  LaunchedEffect(state) {
    if (state is ViewProfileState.Success)
      userDetails = (state as ViewProfileState.Success).data
  }

  if (state is ViewProfileState.Loading)
    ProfileShimmerLayout()
  else if (userDetails != null)
    Column(
      modifier = Modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.primary),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .then(
            if (isTablet)
              Modifier.width(600.dp)
            else
              Modifier.fillMaxWidth()
          )
      ) {
        com.example.timecapsule.ui.selectcapsule.BackRow(modifier = Modifier.padding(vertical = 30.dp)) {
          onBackClick()
        }
        Column(
          modifier = Modifier
              .fillMaxSize()
              .align(Alignment.Center),
          verticalArrangement = Arrangement.Top
        ) {
          AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.35F),
            model = userDetails!!.coverImageUrl,
            contentDescription = "cover image",
            contentScale = ContentScale.Crop
          )
          Column(modifier = Modifier.weight(0.65F)) {
          }
        }

        Column(
          modifier = Modifier
              .fillMaxSize()
              .align(Alignment.Center),
          verticalArrangement = Arrangement.Top
        ) {
          Column(modifier = Modifier.weight(0.25F)) {
          }
          Column(
              Modifier
                  .fillMaxWidth()
                  .weight(0.75F)
                  .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                  .background(MaterialTheme.colorScheme.primary)
                  .border(
                      1.dp,
                      shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                      color = Color.Black
                  )
                  .padding(horizontal = 10.dp, vertical = 20.dp)
                  .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
          ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent)
                    .wrapContentHeight(), horizontalAlignment = Alignment.CenterHorizontally
            ) {
              AsyncImage(
                modifier = Modifier
                    .size(180.dp)
                    .clip(CircleShape),
                model = userDetails!!.imageUrl,
                contentDescription = "profile image",
                contentScale = ContentScale.Crop,
              )
              TextField(
                modifier = Modifier
                    .wrapContentSize()
                    .width(150.dp),
                value = userDetails!!.userName,
                onValueChange = {},
                label = {
                  Text(text = "username")
                },
                readOnly = true,
                colors = TextFieldDefaults.textFieldColors(
                  containerColor = Color.Transparent,
                  unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                  focusedTextColor = LightBlue,
                  cursorColor = LightBlue,
                  focusedIndicatorColor = Color.Transparent,
                  unfocusedIndicatorColor = Color.Transparent,
                  errorTextColor = Color.Red,
                  errorContainerColor = Color.Transparent,
                  errorIndicatorColor = Color.Transparent,
                  errorSupportingTextColor = Color.Red,
                  focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                  unfocusedLabelColor = LightBlue,
                  focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                  unfocusedTrailingIconColor = LightBlue
                ),
                maxLines = 1,
                textStyle = MaterialTheme.typography.titleMedium.copy(
                  textAlign = TextAlign.Center
                ),
                isError = false
              )
            }

            TextField(
              modifier = Modifier
                  .wrapContentSize()
                  .padding(vertical = 10.dp),
              value = userDetails!!.firstName + " " + userDetails!!.lastName,
              onValueChange = {},
              readOnly = true,
              label = {
                Text(text = "full name")
              },
              colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedTextColor = LightBlue,
                cursorColor = LightBlue,
                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedLabelColor = LightBlue,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedLeadingIconColor = LightBlue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
              ),
              maxLines = 1,
              textStyle = MaterialTheme.typography.titleMedium.copy(
                textAlign = TextAlign.Start
              ),
              leadingIcon = {
                Icon(
                  painter = painterResource(id = R.drawable.ic_outline_person),
                  contentDescription = ""
                )
              },
            )

            TextField(
              modifier = Modifier
                  .wrapContentSize()
                  .padding(vertical = 10.dp),
              value = userDetails!!.aboutMe,
              onValueChange = {},
              readOnly = true,
              label = {
                Text(text = "about me")
              },
              colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedTextColor = LightBlue,
                cursorColor = LightBlue,
                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedLabelColor = LightBlue,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedLeadingIconColor = LightBlue,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
              ),
              singleLine = false,
              maxLines = 4,
              textStyle = MaterialTheme.typography.titleMedium.copy(
                textAlign = TextAlign.Start
              ),
              leadingIcon = {
                Icon(
                  painter = painterResource(id = com.example.timecapsule.R.drawable.ic_subtitles),
                  contentDescription = ""
                )
              },
            )
          }
        }
      }
    }
}

@Composable
fun ProfileShimmerLayout() {
  Box(
    modifier = Modifier
      .fillMaxSize()
  ) {
    Column(
      modifier = Modifier
          .fillMaxSize()
          .align(Alignment.Center),
      verticalArrangement = Arrangement.Top
    ) {
      Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(0.35F)
            .placeholder(
                visible = true,
                color = Color.LightGray.copy(alpha = 0.3f),
                shape = RoundedCornerShape(0.dp),
                highlight = PlaceholderHighlight.shimmer()
            )
      )
      Column(modifier = Modifier.weight(0.65F)) {}
    }

    Column(
      modifier = Modifier
          .fillMaxSize()
          .align(Alignment.Center),
      verticalArrangement = Arrangement.Top
    ) {
      Column(modifier = Modifier.weight(0.25F)) {}
      Column(
          Modifier
              .fillMaxWidth()
              .weight(0.75F)
              .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
              .background(MaterialTheme.colorScheme.primary)
              .border(
                  1.dp,
                  shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                  color = Color.Black
              )
              .padding(horizontal = 10.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
      ) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .wrapContentHeight(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Box(
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .placeholder(
                    visible = true,
                    color = Color.LightGray.copy(alpha = 0.3f),
                    shape = CircleShape,
                    highlight = PlaceholderHighlight.shimmer()
                )
          )
          Spacer(modifier = Modifier.height(30.dp))
          BasicText(
            text = "",
            modifier = Modifier
                .width(150.dp)
                .height(30.dp)
                .placeholder(
                    visible = true,
                    color = Color.LightGray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderHighlight.shimmer()
                )
          )


          Spacer(modifier = Modifier.height(60.dp))
          BasicText(
            text = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.Start)
                .padding(end = 100.dp)
                .placeholder(
                    visible = true,
                    color = Color.LightGray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderHighlight.shimmer()
                )
          )


          Spacer(modifier = Modifier.height(30.dp))
          BasicText(
            text = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .align(Alignment.Start)
                .padding(end = 100.dp)
                .placeholder(
                    visible = true,
                    color = Color.LightGray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(4.dp),
                    highlight = PlaceholderHighlight.shimmer()
                )
          )
        }

        Box(
          modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 10.dp)
              .placeholder(
                  visible = true,
                  color = Color.LightGray.copy(alpha = 0.3f),
                  shape = RoundedCornerShape(4.dp),
                  highlight = PlaceholderHighlight.shimmer()
              )
        )

        Box(
          modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 10.dp)
              .placeholder(
                  visible = true,
                  color = Color.LightGray.copy(alpha = 0.3f),
                  shape = RoundedCornerShape(4.dp),
                  highlight = PlaceholderHighlight.shimmer()
              )
        )

        Box(
          modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 10.dp)
              .placeholder(
                  visible = true,
                  color = Color.LightGray.copy(alpha = 0.3f),
                  shape = RoundedCornerShape(4.dp),
                  highlight = PlaceholderHighlight.shimmer()
              )
        )
      }
    }
  }
}
