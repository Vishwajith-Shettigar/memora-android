package com.example.timecapsule.ui.theme.letter

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.selecttime.NavigationRow
import com.example.timecapsule.ui.theme.util.DeviceType
import kotlin.math.floor

@Composable
fun LetterScreen() {
  Scaffold(containerColor = MaterialTheme.colorScheme.primary,
    bottomBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(0.dp)
      ) {
        NavigationRow()
      }
    }) { innerPadding ->
    Column(
      modifier = Modifier
        .background(MaterialTheme.colorScheme.primary)
        .padding(innerPadding),
    ) {
      LetterWritingScreen()
    }
  }
}

@Composable
fun LetterWritingScreen() {

  val isTablet = DeviceType.isTablet()

  val boxModifier = if (isTablet) {
    Modifier
      .width(500.dp)
      .height(500.dp)
  } else {
    Modifier
      .fillMaxWidth(0.9f)
      .height(500.dp)
  }

  val letterImage: Painter = painterResource(id = R.drawable.letterimage)
  LazyColumn(
    modifier = Modifier
      .fillMaxSize(),
    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center,
    ) {
    items(1) {
      Text(
        text = "Would you like to leave a letter? ",
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
          .padding(vertical = 20.dp, horizontal = 10.dp)
          .wrapContentSize()
      )
      Box(
        modifier = boxModifier
      ) {
        Image(
          painter = letterImage,
          contentDescription = "Letter Background",
          modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
          contentScale = ContentScale.Crop
        )
        LetterWritingWithLineLimit()
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun PreviewLetterWritingScreen() {
  LetterWritingScreen()
}

@Composable
fun LetterWritingWithLineLimit() {
  var userText by remember { mutableStateOf(TextFieldValue("Write letter here")) }

  val fontSize = 20.sp
  val maxHeight = 500.dp
  val lineHeight = 28.sp
  val textsize = userText.text.length;
  val maxLines = floor(500.dp.value / lineHeight.value).toInt()
  var currentLineCount by remember { mutableStateOf(1) }
  Box(
    modifier = Modifier.fillMaxSize()
  ) {
    BasicTextField(
      value = userText,
      onValueChange = { newText ->
        if (newText.text.length < textsize || currentLineCount < maxLines) {
          userText = newText
        }
      },
      textStyle = TextStyle(
        color = Color.Black,
        fontSize = fontSize,
        lineHeight = 28.sp,
        fontFamily = FontFamily.Cursive
      ),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
      onTextLayout = { textLayoutResult ->
        currentLineCount = textLayoutResult.lineCount
      },
      modifier = Modifier

        .align(Alignment.TopStart)
        .fillMaxSize()
        .padding(16.dp)
        .height(maxHeight),
    )
    Text(
      text = "$currentLineCount/$maxLines lines used",
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp),
      color = Color.Gray,
      fontSize = 12.sp
    )
  }
}
