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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.model.DownloadFile
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.uploadfiles.UploadedFileItem
import com.example.timecapsule.viewmodel.OpenCapsuleViewModel

@Preview
@Composable
fun ShowContentScreen(viewModel: OpenCapsuleViewModel = hiltViewModel()) {
  val fileUrls = ArrayList<DownloadFile>()
  fileUrls.add(
    DownloadFile(
      url = "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/uploads%2F0BG4KYWTzzNXXsDFOikZRJON9vj1%2F3cp5fynhf5%2F_document_image%3A1000016814?alt=media&token=908f55c8-743f-43ea-9b58-787d7a2dcf5e",
      fileType = "jpeg", name = "IMG-20241102-WA0037.jpg"
    ),
  )
  fileUrls.add(
    DownloadFile(
      url = "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/uploads%2F0BG4KYWTzzNXXsDFOikZRJON9vj1%2F3cp5fynhf5%2F_document_image%3A1000016814?alt=media&token=908f55c8-743f-43ea-9b58-787d7a2dcf5e",
      fileType = "jpeg", name = "IMG-20241102-WA0037.jpg"
    ),
  )
  fileUrls.add(
    DownloadFile(
      url = "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/uploads%2F0BG4KYWTzzNXXsDFOikZRJON9vj1%2F3cp5fynhf5%2F_document_image%3A1000016814?alt=media&token=908f55c8-743f-43ea-9b58-787d7a2dcf5e",
      fileType = "jpeg", name = "IMG-20241102-WA0037.jpg"
    ),
  )
  fileUrls.add(
    DownloadFile(
      url = "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/uploads%2F0BG4KYWTzzNXXsDFOikZRJON9vj1%2F3cp5fynhf5%2F_document_image%3A1000016814?alt=media&token=908f55c8-743f-43ea-9b58-787d7a2dcf5e",
      fileType = "jpeg", name = "IMG-20241102-WA0037.jpg"
    ),
  )
  fileUrls.add(
    DownloadFile(
      url = "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/uploads%2F0BG4KYWTzzNXXsDFOikZRJON9vj1%2F3cp5fynhf5%2F_document_image%3A1000016814?alt=media&token=908f55c8-743f-43ea-9b58-787d7a2dcf5e",
      fileType = "jpeg", name = "IMG-20241102-WA0037.jpg"
    ),
  )
  fileUrls.add(
    DownloadFile(
      url = "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/uploads%2F0BG4KYWTzzNXXsDFOikZRJON9vj1%2F3cp5fynhf5%2F_document_image%3A1000016814?alt=media&token=908f55c8-743f-43ea-9b58-787d7a2dcf5e",
      fileType = "jpeg", name = "IMG-20241102-WA0037.jpg"
    ),
  )
  fileUrls.add(
    DownloadFile(
      url = "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/uploads%2F0BG4KYWTzzNXXsDFOikZRJON9vj1%2F3cp5fynhf5%2F_document_image%3A1000016814?alt=media&token=908f55c8-743f-43ea-9b58-787d7a2dcf5e",
      fileType = "jpeg", name = "IMG-20241102-WA0037.jpg"
    ),
  )
  fileUrls.add(
    DownloadFile(
      url = "https://firebasestorage.googleapis.com/v0/b/time-capsule-android.appspot.com/o/uploads%2F0BG4KYWTzzNXXsDFOikZRJON9vj1%2F3cp5fynhf5%2F_document_image%3A1000016814?alt=media&token=908f55c8-743f-43ea-9b58-787d7a2dcf5e",
      fileType = "jpeg", name = "IMG-20241102-WA0037.jpg"
    ),
  )

  val progress by viewModel.progress.collectAsState()

  var isDownloadClicked by remember {
    mutableStateOf(false)
  }

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
          UploadedFileItem(disableDeleteBtn = true)
        }
      }

      OutlinedButton(modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(20.dp),
        colors = ButtonDefaults.outlinedButtonColors(
          containerColor = LightBlue
        ), onClick = {
          viewModel.startDownloadService(fileUrls = fileUrls)
          isDownloadClicked = true
        }) {


        Text(text = if (isDownloadClicked) progress.toString() else "Download All")
      }
    }
  }
}