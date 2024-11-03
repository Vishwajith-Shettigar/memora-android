package com.example.timecapsule.ui.opencapsule

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue

@Preview
@Composable
fun ShowContentScreen() {
  Scaffold(
    containerColor = MaterialTheme.colorScheme.primary,
  ) { innerPadding ->
    Box(
      modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
    ) {
      LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 3.dp)
            .align(Alignment.TopStart)
      ) {
        item {
          Text(
            text = "Your capsule is packed with some exciting treasures! Dive in and download them all!",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
              .padding(horizontal = 8.dp, vertical = 20.dp)
          )
        }
        items(30) {
          UploadedFileItem()
        }
      }

      OutlinedButton(modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
          containerColor = LightBlue
        ), onClick = { /*TODO*/ }) {
        Text(text = "Download All")
      }
    }
  }
}

@Composable
fun UploadedFileItem(
  uri: Uri = Uri.EMPTY,
  fileUri: Uri = Uri.EMPTY,
  title: String = "Project Reports",
  fileSize: String = "21.8 MB of 21.8 MB",
  icon: Int = R.drawable.doc,
  onDownloadClick: (Uri, Uri) -> Unit = { _, _ -> }
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
    IconButton(
      onClick = { onDownloadClick(uri, fileUri) },
      modifier = Modifier
          .size(40.dp)
          .background(
              color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6F),
              shape = CircleShape
          )
    ) {
      Icon(
        painter = painterResource(id = R.drawable.ic_download),
        contentDescription = "Delete File",
        tint = Color.LightGray
      )
    }
  }
}
