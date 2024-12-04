package com.example.timecapsule.ui.editprofile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.ViewTreeObserver
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import coil.compose.AsyncImage
import com.example.model.Profile
import com.example.model.UpdateProfile
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.viewmodel.EditProfileState

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
  editProfileState: EditProfileState,
  profile: Profile,
  onUpdate: (UpdateProfile) -> Unit,
  onDismiss: () -> Unit = {}, changeCoverImage: (String) -> Unit = {}
) {

  val bottomSheetState =
    rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val view = LocalView.current
  var isImeVisible by remember { mutableStateOf(false) }

  DisposableEffect(LocalWindowInfo.current) {
    val listener = ViewTreeObserver.OnPreDrawListener {
      isImeVisible = ViewCompat.getRootWindowInsets(view)
        ?.isVisible(WindowInsetsCompat.Type.ime()) == true
      true
    }
    view.viewTreeObserver.addOnPreDrawListener(listener)
    onDispose {
      view.viewTreeObserver.removeOnPreDrawListener(listener)
    }
  }

  LaunchedEffect(isImeVisible) {
    Log.e("pokemon", isImeVisible.toString())
  }

  Column(
    modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent),
    verticalArrangement = Arrangement.Bottom
  ) {
    ModalBottomSheet(
      windowInsets = WindowInsets.ime,
      onDismissRequest = {
        onDismiss()
      },
      sheetState = bottomSheetState,
      modifier = Modifier.then(
        if (isImeVisible)
          Modifier.fillMaxHeight(1.0F)
        else
          Modifier.fillMaxHeight(0.73F)
      )
    ) {
      EditProfileContent(editProfileState, profile, changeCoverImage, onUpdate, onDismiss)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
  editProfileState: EditProfileState,
  profile: Profile,
  changeCoverImage: (String) -> Unit = {},
  onUpdate: (profile: UpdateProfile) -> Unit, onDismiss: () -> Unit
) {

  var userName by remember {
    mutableStateOf(profile.username)
  }

  var isProfilePicturePicked by remember {
    mutableStateOf(false)
  }

  var fullName by remember {
    mutableStateOf(profile.firstName + " " + profile.lastName)
  }

  var aboutMe by remember {
    mutableStateOf(profile.aboutMe)
  }

  var profileImageUrl by remember {
    mutableStateOf(profile.profileImageUrl)
  }

  var profileImageUri by remember {
    mutableStateOf<Uri?>(null)
  }

  var coverImageUrl by remember {
    mutableStateOf(profile.coverImageUrl)
  }

  var coverImageUri by remember {
    mutableStateOf<Uri?>(null)
  }

  LaunchedEffect(editProfileState) {
    if (editProfileState is EditProfileState.Success)
      onDismiss()
  }

  val imagePickerLauncher: ManagedActivityResultLauncher<Intent, *> =
    rememberLauncherForActivityResult(
      contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
      if (result.resultCode == Activity.RESULT_OK) {
        val data: Intent? = result.data
        if (data?.data != null) {
          if (isProfilePicturePicked) {
            profileImageUri = data.data
          } else {
            changeCoverImage(data.data.toString())
            coverImageUri = data.data
          }
        }
      }
    }

  fun openFilePicker(isProfilePicture: Boolean) {
    isProfilePicturePicked = isProfilePicture
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
      addCategory(Intent.CATEGORY_OPENABLE)
      type = "image/*"
      putExtra("isProfilePicture", isProfilePicture)
    }
    imagePickerLauncher.launch(intent)
  }

  Column(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(16.dp)
        .imePadding()
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
              ), onClick = {
            openFilePicker(isProfilePicture = true)
          }, content = {
            Icon(
              painter = painterResource(id = R.drawable.ic_camera),
              tint = LightBlue,
              contentDescription = "edit profile picture"
            )
          })

        AsyncImage(
          model =
          if (profileImageUri == null)
            profileImageUrl
          else
            profileImageUri,
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
              ), onClick = {
            openFilePicker(isProfilePicture = false)
          }, content = {
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
        value = profile.username,
        readOnly = true,
        onValueChange = {
        },
        label = {
          Text(text = "username")
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
      value = fullName,
      onValueChange = {
        fullName = it
      },
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
          painter = painterResource(id = R.drawable.ic_outline_person),
          contentDescription = ""
        )
      },
    )

    TextField(
      modifier = Modifier
          .wrapContentSize()
          .padding(vertical = 10.dp)
          .imePadding(),
      value = aboutMe,
      onValueChange = {
        aboutMe = it
      },
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
          .padding(vertical = 10.dp)
          .imePadding(),
      onClick = {
        val updatedProfile = UpdateProfile(
          firstName = fullName.substringBefore(" "),
          lastName = fullName.substringAfter(" "),
          oldProfileImageUrl = profileImageUrl,
          oldCoverImageUrl = coverImageUrl,
          profileImageUri = profileImageUri,
          coverImageUri = coverImageUri,
          aboutMe = aboutMe
        )
        onUpdate(updatedProfile)
      },
      colors = ButtonDefaults.outlinedButtonColors(containerColor = LightBlue)
    ) {

      if (editProfileState is EditProfileState.Loading) {
        androidx.compose.material.CircularProgressIndicator(
          modifier = Modifier
            .size(22.dp),
          color = Color.White,
          strokeWidth = 2.dp,
          backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      } else {
        Text(
          text = when (editProfileState) {
            is EditProfileState.Idle -> "save"
            is EditProfileState.Error -> editProfileState.message!!
            else -> ""
          },
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
