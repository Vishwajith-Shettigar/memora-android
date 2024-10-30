package com.example.timecapsule.ui.opencapsule

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.timecapsule.R
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.ImageDecoderDecoder
import coil.decode.GifDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.example.timecapsule.routes.Screen
import com.example.timecapsule.viewmodel.DisplayCapsuleDetailsState
import com.example.timecapsule.viewmodel.OpenCapsuleViewModel

@Preview
@Composable
fun CapsuleLoadingScreen(
  viewModel: OpenCapsuleViewModel = hiltViewModel(), capsuleId: String,
  navigate: (String) -> Unit = {}, popBack: () -> Unit = {}
) {
  val context = LocalContext.current
  val combinedState by viewModel.combinedState.collectAsState()

  LaunchedEffect(Unit) {
    viewModel.getScreenCheckPoint(capsuleId)
    viewModel.getCapsuleDetails(capsuleId)
  }

  LaunchedEffect(combinedState) {
    if (combinedState != null) {
      val checkpoint = combinedState!!.checkpoint
      val capsuleDetailsState = combinedState!!.capsuleDetailsState
      if (capsuleDetailsState is DisplayCapsuleDetailsState.Success) {
        navigate(checkpoint!!)
      } else {
        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
        popBack()
      }
    }
  }

  Column(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    GifImage(
      context = context,
      url = R.drawable.capsule_loading,
      Modifier
        .size(230.dp)
    )
    Text(
      text = "Please wait a moment.",
      style = MaterialTheme.typography.titleLarge.copy(fontSize = 10.sp),
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 30.dp)
    )
  }
}

@Composable
fun GifImage(
  context: Context,
  url: Int, modifier: Modifier
) {
  val imageLoader = ImageLoader.Builder(context)
    .components {
      if (android.os.Build.VERSION.SDK_INT >= 28) {
        add(ImageDecoderDecoder.Factory())
      } else {
        add(GifDecoder.Factory())
      }
    }
    .build()

  AsyncImage(
    model = ImageRequest.Builder(context)
      .data(url)
      .size(Size.ORIGINAL)
      .crossfade(true)
      .build(),
    contentDescription = "Animated GIF",
    modifier = modifier,
    imageLoader = imageLoader
  )
}
