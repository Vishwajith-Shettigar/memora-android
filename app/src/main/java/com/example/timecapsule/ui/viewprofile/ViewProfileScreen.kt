package com.example.timecapsule.ui.viewprofile

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.example.model.Profile
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProfileScreen(profile: Profile) {

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
      AsyncImage(
        modifier = Modifier
            .fillMaxWidth()
            .weight(0.35F),
        model = profile.coverImageUrl,
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
              .padding(horizontal = 10.dp, vertical = 20.dp),
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
            model = profile.profileImageUrl,
            contentDescription = "profile image",
            contentScale = ContentScale.Crop,
          )
          TextField(
            modifier = Modifier
                .wrapContentSize()
                .width(150.dp),
            value = profile.username,
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
          value = profile.firstName + " " + profile.lastName,
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
          value = profile.aboutMe,
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
