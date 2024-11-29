package com.example.timecapsule.ui.editprofile

import android.widget.EditText
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen() {
  val bottomSheetState =
    rememberModalBottomSheetState(skipPartiallyExpanded = true)

  ModalBottomSheet(
    onDismissRequest = { },
    sheetState = bottomSheetState,
    modifier = Modifier.height(580.dp)
  ) {
    EditProfileContent()
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .padding(16.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = Modifier.fillMaxWidth()
      ) {

        IconButton(
          modifier = Modifier
            .size(40.dp)
            .border(
              width = 1.dp,
              shape = CircleShape,
              color = MaterialTheme.colorScheme.primaryContainer
            ), onClick = {}, content = {
            Icon(
              painter = painterResource(id = R.drawable.ic_camera),
              tint = LightBlue,
              contentDescription = "edit profile picture"
            )
          })

        Image(
          painter = painterResource(id = R.drawable.testimg6), // Replace with actual drawable
          contentDescription = "Profile Picture",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
        )

        IconButton(
          modifier = Modifier
            .size(40.dp)
            .border(
              width = 1.dp,
              shape = CircleShape,
              color = MaterialTheme.colorScheme.primaryContainer
            ), onClick = {}, content = {
            Icon(
              painter = painterResource(id = com.example.timecapsule.R.drawable.ic_cover_image),
              tint = LightBlue,
              contentDescription = "edit profile picture"
            )
          })
      }
      TextField(
        modifier = Modifier
          .wrapContentSize()
          .width(150.dp),
        value = "darkX45",
        onValueChange = {},
        label = {
          Text(text = "username")
        },
        trailingIcon = {
          Icon(
            painter = painterResource(id = com.example.timecapsule.R.drawable.ic_edit),
            contentDescription = ""
          )
        },
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
      value = "darkX45",
      onValueChange = {},
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
        unfocusedLeadingIconColor = LightBlue
      ),
      maxLines = 1,
      textStyle = MaterialTheme.typography.titleMedium.copy(
        textAlign = TextAlign.Start
      ),
      leadingIcon = {
        Icon(
          painter = painterResource(id = com.example.timecapsule.R.drawable.ic_outline_person),
          contentDescription = ""
        )
      },
    )

    TextField(
      modifier = Modifier
        .wrapContentSize()
        .padding(vertical = 10.dp),
      value = "darkX45",
      onValueChange = {},
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
        unfocusedLeadingIconColor = LightBlue
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

    Button(
      modifier = Modifier
        .wrapContentSize()
        .padding(vertical = 10.dp),
      onClick = {
      },
      colors = ButtonDefaults.outlinedButtonColors(containerColor = LightBlue)
    ) {
      Text(
        "save",
        style = MaterialTheme.typography.titleSmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
  EditProfileScreen()
}
