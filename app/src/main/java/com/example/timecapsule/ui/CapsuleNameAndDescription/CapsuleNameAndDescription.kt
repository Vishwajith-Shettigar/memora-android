package com.example.timecapsule.ui.CapsuleNameAndDescription

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.timecapsule.R
import com.example.timecapsule.ui.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.selecttime.NavigationRow
import com.example.timecapsule.ui.util.DeviceType

@Composable
fun CapsuleNameAndDescription(onNavigate: (NavigationAddCapsule) -> Unit) {

  val isTablet = DeviceType.isTablet()

  var capsuleName by remember {
    mutableStateOf("")
  }

  var capsuleDescription by remember {
    mutableStateOf("")
  }

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.primary)
      .padding(top = 30.dp),
    containerColor = MaterialTheme.colorScheme.primary,
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(
          start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
          end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
          top = innerPadding.calculateTopPadding()
        )
    ) {

      Column(
        Modifier
          .fillMaxWidth()
          .padding(16.dp)
          .align(Alignment.Center)
      ) {
        Text(
          text = "Please name your capsule and provide a brief description.",
          style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 20.dp)
        )
        CustomTextField(
          value = capsuleName,
          hint = "Family capsule",
          icon = R.drawable.ic_bulb,
          isTablet = isTablet,
          characterLimit = 13
        ) {
          capsuleName = it
        }
        CustomTextField(
          value = capsuleDescription,
          hint = "Hi, this is Robert. I’m eagerly waiting...",
          icon = R.drawable.ic_subtitles,
          isTablet = isTablet,
          characterLimit = 60
        ) {
          capsuleDescription = it
        }
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(0.dp)
          .align(Alignment.BottomCenter)
          .zIndex(2f)
      ) {
        NavigationRow() { navigationFlow ->
          onNavigate(navigationFlow)
        }
      }
    }
  }
}

@Preview
@Composable
fun showPreview() {
  CapsuleNameAndDescription({})
  CustomTextField(
    value = "",
    hint = "Family capsule",
    icon = R.drawable.ic_bulb,
    characterLimit = 13
  ) {

  }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
  value: String,
  hint: String,
  icon: Int,
  modifier: Modifier = Modifier,
  isTablet: Boolean = false,
  characterLimit: Int,
  onValueChanged: (String) -> Unit
) {
  Column(
    modifier = if (!isTablet) {
      modifier
        .fillMaxWidth()
        .padding(vertical = 3.dp)
    } else {
      modifier
        .width(600.dp)
        .padding(vertical = 3.dp)
    }
  ) {
    TextField(
      value = value,
      onValueChange = {
        if (it.length <= characterLimit) {
          onValueChanged(it)
        }
      },
      placeholder = {
        Text(
          text = hint,
          fontSize = 16.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.5F),

          )
      },
      leadingIcon = {
        Image(
          painter = painterResource(id = icon),
          contentDescription = null
        )
      },
      shape = RoundedCornerShape(12.dp),
      colors = TextFieldDefaults.textFieldColors(
        containerColor = Color(0xFF60A5FA).copy(alpha = 0.2f),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant
      ),
      modifier = Modifier.fillMaxWidth()
    )

    // Character Counter
    Text(
      text = "${value.length} / $characterLimit",
      color = if (value.length == characterLimit) Color.Red else Color.Gray,
      fontSize = 12.sp,
      modifier = Modifier.align(Alignment.End)
    )
  }
}
