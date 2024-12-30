package com.example.timecapsule.ui.profile

import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.ripple
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.palette.graphics.Palette
import coil.compose.AsyncImage
import com.example.model.Profile
import com.example.timecapsule.R
import com.example.timecapsule.ui.editprofile.EditProfileContent
import com.example.timecapsule.ui.editprofile.EditProfileScreen
import com.example.timecapsule.ui.login.TitleSubtitleWithOkayButtonDialog
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.SubTitleFontColor
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.viewmodel.ProfileState
import com.example.timecapsule.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(
  viewModel: ProfileViewModel = hiltViewModel(),
  onViewProfileClick: (String) -> Unit = {}, onSettingClick: () -> Unit,
  onContactUsClicked: () -> Unit, onPrivacyClicked: () -> Unit,
  signOut:()->Unit
) {

  var showSignOutDialog by remember {
    mutableStateOf(false)
  }

  val profileState by viewModel.profile.collectAsState()

  val editProfileState by viewModel.editProfileState.collectAsState()

  var isLoading by remember {
    mutableStateOf(true)
  }

  var isSuccess by remember {
    mutableStateOf(false)
  }

  var editMode by remember {
    mutableStateOf(false)
  }

  var editedPreviewCoverImageUrl by remember {
    mutableStateOf<String?>(null)
  }

  LaunchedEffect(Unit) {
    viewModel.getProfile()
  }

  LaunchedEffect(profileState) {
    when (profileState) {
      is ProfileState.Success -> {
        isSuccess = true
      }

      is ProfileState.Error -> {
        isSuccess = false
        isLoading = false
      }

      else -> {}
    }
  }

  if (showSignOutDialog) {
    TitleSubtitleWithOkayButtonDialog(
      title = "Sign out",
      subtitle = "Are you sure ?",
      buttonColor = Color.Red
    ) {
      showSignOutDialog = false
      viewModel.signOut()
      signOut()
    }
  }

  Scaffold { innerPadding ->

    AnimatedVisibility(visible = editMode && (profileState is ProfileState.Success)) {
      EditProfileScreen(editProfileState,
        profile = (profileState as ProfileState.Success).data,
        onUpdate = { profile ->
          viewModel.updateProfle(profile)
        },
        onDismiss = {
          editedPreviewCoverImageUrl = null
          editMode = false
          viewModel.resetEditProfileState()
        },
        changeCoverImage = { url ->
          editedPreviewCoverImageUrl = url
        })
    }

    Box(modifier = Modifier.fillMaxSize()) {
      Column(
          Modifier
              .fillMaxSize()
              .align(Alignment.Center)
              .zIndex(3.0F)
      ) {
        Column(
          modifier = Modifier
              .fillMaxWidth()
              .weight(0.4F),
        ) {
        }
        Column(
          modifier =
          Modifier
              .fillMaxWidth()
              .background(
                  MaterialTheme.colorScheme.primary,
                  shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
              )
              .padding(top = 10.dp)
              .weight(1.0f)
              .verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
          ) {
            IconButton(modifier = Modifier
                .size(40.dp)
                .border(
                    width = 1.dp,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ), onClick = {
              if (profileState is ProfileState.Success)
                onViewProfileClick((profileState as ProfileState.Success).data.userId)
            }) {
              Icon(
                painter = painterResource(id = com.example.timecapsule.R.drawable.icon_face),
                contentDescription = "edit profile icon",
                tint = LightBlue
              )
            }

            AsyncImage(
              model = if (isSuccess)
                (profileState as ProfileState.Success).data.profileImageUrl
              else
                R.drawable.testimg1,
              contentDescription = "Profile Picture",
              modifier = Modifier
                  .size(110.dp)
                  .clip(CircleShape),
              contentScale = ContentScale.Crop,
            )

            IconButton(modifier = Modifier
                .size(40.dp)
                .border(
                    width = 1.dp,
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer
                ), onClick = { editMode = true }) {
              Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = "edit profile icon",
                tint = LightBlue
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text =
            if (isSuccess)
              (profileState as ProfileState.Success).data.username
            else
              "loading..",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              fontSize = 20.sp
            )
          )
          Text(
            text =
            if (isSuccess)
              (profileState as ProfileState.Success).data.firstName + " " + (profileState as ProfileState.Success).data.lastName
            else
              "loading..", style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              color = SubTitleFontColor,
              fontSize = 15.sp
            )
          )

          Spacer(modifier = Modifier.height(16.dp))

          SettingsOption(icon = R.drawable.ic_darkmode, text = "Dark Mode", true) {}
          SettingsOption(icon = com.example.timecapsule.R.drawable.ic_setting, text = "Setting") {
            onSettingClick()
          }
          SettingsOption(icon = com.example.timecapsule.R.drawable.ic_email, text = "Contact Us") {
            onContactUsClicked()
          }
          SettingsOption(
            icon = com.example.timecapsule.R.drawable.ic_shield,
            text = "Privacy"
          ) {
            onPrivacyClicked()
          }
          SettingsOption(icon = com.example.timecapsule.R.drawable.ic_logout, text = "Sign Out") {
            showSignOutDialog = true
          }
        }
      }

      Column(
        modifier = Modifier
            .padding()
            .fillMaxSize()
            .background((Color.Transparent))
            .align(Alignment.Center)
            .zIndex(2.0F)
      ) {
        AsyncImage(
          modifier = Modifier
              .fillMaxWidth()
              .height(1000.dp)
              .weight(0.7f),
          model = if (isSuccess) {
            if (editMode && editedPreviewCoverImageUrl != null)
              editedPreviewCoverImageUrl
            else
              (profileState as ProfileState.Success).data.coverImageUrl
          } else
            R.drawable.testimg1,
          contentDescription = "cover image",
          contentScale = ContentScale.Crop,
        )
        Column(
          modifier = Modifier
              .fillMaxWidth()
              .weight(1F),
        ) {
        }
      }
    }
  }
}

@Composable
fun SettingsOption(
  icon: Int,
  text: String,
  isDarkModeOption: Boolean = false,
  onClick: () -> Unit
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isTablet = DeviceType.isTablet()
  Row(
    modifier =
    if (isTablet)
        Modifier
            .width(600.dp)
            .height(55.dp)
            .clickable(
                onClick = { onClick() },
                interactionSource = interactionSource,
                indication = ripple()
            )
            .padding(horizontal = 16.dp)
    else
        Modifier
            .fillMaxWidth()
            .height(55.dp)
            .padding(horizontal = 16.dp)
            .clickable(
                onClick = { onClick() },
                interactionSource = interactionSource,
                indication = ripple()
            ),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      modifier = Modifier
          .wrapContentWidth()
          .wrapContentHeight(),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      if (isDarkModeOption)
        Image(
          painterResource(id = icon),
          contentDescription = text,
          modifier = Modifier.size(24.dp),
        ) else
        Icon(
          painterResource(id = icon),
          contentDescription = text,
          modifier = Modifier.size(24.dp),
          tint = LightBlue.copy(alpha = 0.8F)
        )
      Spacer(modifier = Modifier.width(16.dp))
      Text(
        text, style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontSize = 16.sp
        )
      )
    }
    if (isDarkModeOption)
      Switch(checked = true, onCheckedChange = {})
    else
      Icon(
        painter = painterResource(id = R.drawable.ic_forward), contentDescription = "open option",
        tint = Color.LightGray
      )
  }
}
