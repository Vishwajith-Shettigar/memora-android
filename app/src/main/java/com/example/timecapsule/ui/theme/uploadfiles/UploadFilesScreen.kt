package com.example.timecapsule.ui.theme.uploadfiles

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.NavigatioButtons
import com.example.timecapsule.ui.theme.SubTitleFontColor
import com.example.timecapsule.ui.theme.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.theme.selecttime.NavigationRow
import com.example.timecapsule.ui.theme.util.DeviceType

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UploadFilesScreen(onNavigate: (NavigationAddCapsule) -> Unit = {}) {
  val isTablet = DeviceType.isTablet()

  Scaffold(
    modifier = Modifier.background(MaterialTheme.colorScheme.primary),
    topBar = {
      TopAppBar(
        title = {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "Upload Files",
              style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
      )

    },
    containerColor = MaterialTheme.colorScheme.primary
  ) { innerPadding ->
    Box(modifier = Modifier
      .fillMaxSize()
      .padding(innerPadding))
    {
      LazyColumn(
        modifier = if (isTablet)
          Modifier
            .padding(horizontal = 10.dp)
            .width(800.dp)
            .align(Alignment.Center)
        else
          Modifier
            .padding(horizontal = 10.dp)
            .fillMaxSize(),
      ) {

        item { UploadFileCard() }
        item { OngoingUpload() }
        item { Uploaded() }

      }
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(0.dp)
          .align(Alignment.BottomCenter)
          .zIndex(2f)
      ) {
        NavigationRow() {
          onNavigate(it)
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun Uploaded() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .background(MaterialTheme.colorScheme.primary)
      .padding(start = 10.dp, end = 10.dp, top = 20.dp, bottom = 50.dp)
  )
  {
    Text(
      text = "Uploaded Files",
      style = MaterialTheme.typography.titleLarge.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 5.dp)
    ) {
      UploadedFileItem(title = "Lorem ipsum", "21.9 MB", R.drawable.pdf)
      UploadedFileItem(title = "Lorem ipsum puioka", "11.9 MB", R.drawable.xls)


    }

  }
}

@Preview(showBackground = true)
@Composable
fun OngoingUpload() {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .wrapContentHeight()
      .clip(shape = RoundedCornerShape(10.dp))
      .background(MaterialTheme.colorScheme.primaryContainer)
      .padding(horizontal = 10.dp, vertical = 20.dp)
  )
  {
    Text(
      text = "Uploading Files",
      style = MaterialTheme.typography.titleLarge.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 5.dp)
    ) {
      UploadingFileItem(
        title = "Family time", icon = R.drawable.videocamera, uploadProgress = 6F,
        isUploading = true, fileSize = "6.5 MB of 100.8 MB"
      )

      UploadingFileItem(
        title = "Pet shinzo", icon = R.drawable.image, uploadProgress = 61F,
        isUploading = true, fileSize = "5.5 MB of 4.8 MB"
      )
      UploadingFileItem(
        title = "Project report", icon = R.drawable.doc, uploadProgress = 11F,
        isUploading = true, fileSize = "0.5 MB of 4.8 MB"
      )
      UploadingFileItem(
        title = "Time management", icon = R.drawable.xls, uploadProgress = 97F,
        isUploading = true, fileSize = "11.5 MB of 11.8 MB"
      )
      UploadingFileItem(
        title = "SRS", icon = R.drawable.pdf, uploadProgress = 100F,
        isUploading = false, fileSize = "21.8 MB of 21.8 MB"
      )
    }

  }
}

@Preview
@Composable
fun UploadedFileItem(
  title: String = "Project Reports",
  fileSize: String = "21.8 MB of 21.8 MB",
  icon: Int = R.drawable.doc, disableDeleteBtn: Boolean = false,
  onDeleteClick: () -> Unit = {}
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .background(MaterialTheme.colorScheme.primary)
      .padding(5.dp)
  ) {
    Image(
      painter = painterResource(id = icon),
      contentDescription = "Document Icon",
      contentScale = ContentScale.Fit,
      modifier = Modifier.size(48.dp)
    )

    Spacer(modifier = Modifier.width(8.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = fileSize,
        fontSize = 14.sp,
        color = Color.Gray
      )
    }

    Spacer(modifier = Modifier.width(8.dp))

    if (!disableDeleteBtn)
      IconButton(
        onClick = onDeleteClick,
        modifier = Modifier
          .size(40.dp)
          .background(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6F),
            shape = CircleShape
          )
      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_delete), // Replace with your actual delete icon resource
          contentDescription = "Delete File",
          tint = Color.LightGray
        )
      }
  }
}

@Preview
@Composable
fun UploadingFileItem(
  title: String = "Project Reports", icon: Int = R.drawable.doc, uploadProgress: Float = 40F,
  isUploading: Boolean = true, fileSize: String = "6.5 MB of 9.8 MB"
) {

  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .background(MaterialTheme.colorScheme.primaryContainer)
      .padding(5.dp)
  ) {
    Image(
      painter = painterResource(id = icon),
      contentDescription = "Document Icon",
      contentScale = ContentScale.Fit,
      modifier = Modifier.size(48.dp)
    )

    Spacer(modifier = Modifier.width(8.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 15.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )

      Spacer(modifier = Modifier.height(8.dp))

      LinearProgressIndicator(
        progress = uploadProgress / 100,
        modifier = Modifier.fillMaxWidth(),
        color = LightBlue,
        trackColor = Color.LightGray
      )

      Spacer(modifier = Modifier.height(4.dp))
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(
          text = "$fileSize",
          fontSize = 12.sp,
          color = SubTitleFontColor
        )

        Text(
          text =
          if (isUploading) {
            "Uploading... ${(uploadProgress).toInt()}%"
          } else {
            "Uploaded"
          },
          fontSize = 12.sp,
          color = LightBlue
        )
      }

    }

    Spacer(modifier = Modifier.width(8.dp))


  }
}

@Preview(showBackground = true)
@Composable
fun UploadFileCard() {
  val stroke = Stroke(
    width = 2f,
    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
  )
  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
    shape = RoundedCornerShape(8.dp),
    modifier = Modifier
      .fillMaxWidth()
      .height(150.dp)
      .padding(horizontal = 3.dp, vertical = 10.dp)
      .drawBehind {
        drawRoundRect(
          color = LightBlue,
          style = stroke,
          cornerRadius = CornerRadius(8.dp.toPx())
        )
      }
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Icon(
        modifier = Modifier.size(50.dp),
        painter = painterResource(id = R.drawable.ic_upload_file),
        contentDescription = "upload files icon",
        tint = LightBlue
      )
      Text(
        text = "Upload your files here",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 10.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )

      Text(
        text = "Browse",
        color = LightBlue,
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 14.sp)

      )
    }
  }
}
