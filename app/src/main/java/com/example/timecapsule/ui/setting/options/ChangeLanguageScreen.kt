package com.example.timecapsule.ui.setting.options

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.ui.util.languageList
import com.example.timecapsule.viewmodel.LanguageSelectionViewModel

private const val b = true

@Composable
fun ChangeLanguageScreen(
  viewModel: LanguageSelectionViewModel = hiltViewModel(),
  onBackClick: () -> Unit
) {
  val isTablet = DeviceType.isTablet()

  val code by viewModel.selectedLanguageCode.collectAsState()

  Column(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
        .padding(top = 20.dp),
    verticalArrangement = Arrangement.Top
  ) {
    BackRow() {
      onBackClick()
    }
    Column(
      modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight()
          .padding(10.dp),
      horizontalAlignment =
      if (isTablet)
        Alignment.CenterHorizontally
      else
        Alignment.Start
    ) {
      Text(
        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
        text = "Choose your language",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 30.sp,
          fontWeight = FontWeight.ExtraBold
        ),
        color = LightBlue
      )
      Text(
        modifier = Modifier.padding(bottom = 30.dp),
        text = "You could choose any of the available languages below.",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 15.sp,
          fontWeight = FontWeight.SemiBold,
          lineHeight = TextUnit(20.0F, TextUnitType.Sp)
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      languageList.forEach {
        LanguageOption(
          language = it.name,
          flagAssetPath = it.iconPath,
          selected = code == it.code,
          isTablet = isTablet
        ) {
          viewModel.setSelectedLanguageCode(it.code)
        }
      }
    }
  }
}

@Composable
fun LanguageOption(
  language: String,
  flagAssetPath: String,
  selected: Boolean,
  isTablet: Boolean,
  onClick: () -> Unit
) {

  val context = LocalContext.current

  val flagBitmap: Bitmap? = remember(flagAssetPath) {
    try {
      val inputStream = context.assets.open(flagAssetPath)
      BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
      null
    }
  }
  Row(
    modifier = Modifier
        .then(
            if (isTablet)
                Modifier.width(600.dp)
            else
                Modifier.fillMaxWidth()
        )
        .padding(vertical = 8.dp)
        .clip(RoundedCornerShape(15.dp))
        .background(if (selected) (LightBlue.copy(alpha = 0.2F)) else MaterialTheme.colorScheme.primaryContainer)
        .border(
            width = 2.dp,
            color = if (selected) LightBlue else Color.Gray,
            shape = RoundedCornerShape(15.dp)
        )
        .clickable { onClick() }
        .padding(vertical = 20.dp, horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    flagBitmap?.let {
      Image(
        bitmap = it.asImageBitmap(),
        contentDescription = null,
        modifier = Modifier.size(26.dp)
      )
    }
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = language,
      style = MaterialTheme.typography.titleLarge.copy(
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = TextUnit(20.0F, TextUnitType.Sp)
      ),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    Spacer(modifier = Modifier.weight(1f))

    Icon(
      painter =
      if (selected)
        painterResource(id = com.example.timecapsule.R.drawable.ic_check_circle)
      else
        painterResource(id = com.example.timecapsule.R.drawable.ic__radio_button_unchecked),
      contentDescription = "selection icon",
      tint =
      if (selected)
        LightBlue
      else
        Color.LightGray
    )
  }
}
