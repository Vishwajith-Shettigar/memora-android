package com.example.timecapsule.ui.uploadfiles

import android.app.Activity
import android.content.Intent
import android.content.pm.LauncherActivityInfo
import android.content.pm.LauncherApps
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.model.FileUploadProgress
import com.example.model.FileUploaded
import com.example.timecapsule.ui.login.TitleSubtitleWithOkayButtonDialog
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.NavigatioButtons
import com.example.timecapsule.ui.theme.SubTitleFontColor
import com.example.timecapsule.ui.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.selecttime.NavigationRow
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.util.getFileImageID
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel
import com.example.timecapsule.viewmodel.StorageWarningState
import com.example.util.bytesToMegabytes

lateinit var filePickerLauncher: ManagedActivityResultLauncher<Intent, *>

@Composable
fun FilePicker(viewModel: CapsuleCreationViewModel) {
  // State to store selected URIs
  val context = LocalContext.current
  val selectedUris = remember { mutableStateListOf<Uri>() }

  // Create a launcher for the file selection activity
  filePickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
  ) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
      val data: Intent? = result.data

      // Check if multiple files were selected
      if (data?.clipData != null) {
        val clipData = data.clipData
        for (i in 0 until clipData!!.itemCount) {
          val fileUri = clipData.getItemAt(i).uri
          viewModel.uploadFiles(uri = fileUri)
        }
      } else if (data?.data != null) {
        // Single file selected
        val fileUri = data.data
        fileUri?.let {
          viewModel.uploadFiles(uri = it)
        }
      }
    }
  }
}

// Function to trigger the file picker
fun openFilePicker() {
  val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
    addCategory(Intent.CATEGORY_OPENABLE)
    type = "*/*"
    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
  }
  filePickerLauncher.launch(intent)
}

@Composable
fun StorageWarningDialog(
  onDismiss: () -> Unit = {}
) {
  // The dialog content with the message and buttons
  AlertDialog(
    onDismissRequest = onDismiss, // Close the dialog when dismissed
    title = {
      Text(
        text = "Capsule Storage full",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    },
    text = {
      Text(
        text = "Your capsule is full already, if you want to add more files please use other capsules.",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 16.sp,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
    },
    confirmButton = {
    },
    dismissButton = {
      Button(colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
        onClick = {
          onDismiss()
        }
      ) {
        Text(
          "Ok", style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
      }
    }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun UploadFilesScreen(
  viewModel: CapsuleCreationViewModel = hiltViewModel(),
  onNavigate: (NavigationAddCapsule) -> Unit = {}
) {

  LaunchedEffect(Unit) {
    viewModel.getFileStatus()
  }
  val fileUploadProgress by viewModel.fileProgrerssState.collectAsState()
  val fileUploaded by viewModel.fileUploadedState.collectAsState()

  val storageWarningState by viewModel.storageWarningState.collectAsState()

  if (storageWarningState == StorageWarningState.Warning) {
    StorageWarningDialog() {
      viewModel.setStorageNoWaringState()
    }
  }

  if (viewModel.showSensitiveFileDialog){
    TitleSubtitleWithOkayButtonDialog(
      title = "Attention!!",
      subtitle = "Please avoid sharing sensitive information. This app is currently in the testing phase."
    ) {
      viewModel.showSensitiveFileDialog = false
    }
  }

  val isTablet = DeviceType.isTablet()
  FilePicker(viewModel)
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
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(
          start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
          end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
          top = innerPadding.calculateTopPadding()
        )
    )
    {
      LazyColumn(
        modifier = if (isTablet)
          Modifier
            .padding(horizontal = 10.dp)
            .width(600.dp).fillMaxHeight()
            .align(Alignment.Center)
        else
            Modifier
                .padding(horizontal = 10.dp)
                .fillMaxSize(),
      ) {
        item { UploadFileCard() }
        item {
          OngoingUpload(fileUploadProgress) { uri ->
            viewModel.cancelFileUploading(uri)
          }
        }
        item {
          Uploaded(fileUploaded) { uri, fileUri ->
            viewModel.deleteUploadedFile(uri, fileUri)
          }
        }
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

@Composable
fun Uploaded(fileUploaded: List<FileUploaded>, onDeleteClick: (Uri, Uri) -> Unit) {
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
      fileUploaded.forEach {
        UploadedFileItem(
          uri = it.uri,
          fileUri = it.fileUri,
          title = it.fileName,
          fileSize = "${String.format("%.2f", bytesToMegabytes(it.totalSize))} MB",
          getFileImageID(it.fileType),
          onDeleteClick = onDeleteClick
        )
      }
    }
  }
}

@Composable
fun OngoingUpload(
  fileUploadProgress: List<FileUploadProgress>,
  onDeleteClick: (uri: Uri) -> Unit = {}
) {
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
      fileUploadProgress.forEach {
        UploadingFileItem(
          uri = it.uri,
          title = it.fileName,
          icon = getFileImageID(it.fileType),
          uploadProgress = it.progress.toFloat(),
          isUploading = true,
          fileSize = "${
            String.format(
              "%.2f",
              bytesToMegabytes(it.uploadedSize)
            )
          } MB  of ${String.format("%.2f", bytesToMegabytes(it.totalSize))} MB",
          onDeleteClick
        )
      }
    }
  }
}

@Preview
@Composable
fun UploadedFileItem(
  uri: Uri = Uri.EMPTY,
  fileUri: Uri = Uri.EMPTY,
  title: String = "Project Reports",
  fileSize: String = "21.8 MB of 21.8 MB",
  icon: Int = R.drawable.doc, disableDeleteBtn: Boolean = false,
  onDeleteClick: (Uri, Uri) -> Unit = { _, _ -> }
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
        onClick = { onDeleteClick(uri, fileUri) },
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
  uri: Uri = Uri.EMPTY,
  title: String = "Project Reports",
  icon: Int = R.drawable.doc,
  uploadProgress: Float = 40F,
  isUploading: Boolean = true,
  fileSize: String = "6.5 MB of 9.8 MB",
  onDeleteClick: (Uri) -> Unit = {}
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

    Column(modifier = Modifier.weight(0.6f)) {
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
            "${(uploadProgress).toInt()}%"
          } else {
            "Uploaded"
          },
          fontSize = 12.sp,
          color = LightBlue
        )
      }
    }

    Spacer(modifier = Modifier.width(8.dp))
    IconButton(
      onClick = {
        onDeleteClick(uri)
      },
      modifier = Modifier
          .size(40.dp)
          .background(
              color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6F),
              shape = CircleShape
          )
    ) {
      Icon(
        painter = painterResource(id = R.drawable.ic_delete),
        contentDescription = "Delete File",
        tint = Color.Blue.copy(alpha = 0.5F)
      )
    }

  }
}

@Preview(showBackground = true)
@Composable
fun UploadFileCard() {
  val context = LocalContext.current
  val activity = context as? Activity

  val stroke = Stroke(
    width = 2f,
    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
  )
  Card(onClick = {
    activity?.let {
      openFilePicker()
    }
  },
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
