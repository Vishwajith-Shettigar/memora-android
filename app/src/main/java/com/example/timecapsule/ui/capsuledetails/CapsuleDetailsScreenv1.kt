package com.example.timecapsule.ui.capsuledetails


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.model.CapsuleDetails
import com.example.timecapsule.ui.review.MapPreviewCard
import com.example.timecapsule.ui.review.SharedWithALlIcon
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.sharewithpeople.Profile
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.overSeer
import com.example.timecapsule.ui.util.getModelColor
import com.example.timecapsule.viewmodel.DisplayCapsuleDetailsState
import com.example.timecapsule.viewmodel.DisplayCapsuleDetailsViewModel
import com.example.util.getModelImage
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

@Composable
fun CapsuleDetailsScreenv1(
  capsuleId: String = "",
  viewModel: DisplayCapsuleDetailsViewModel = hiltViewModel(),
  onBack: () -> Unit = {}
) {

  val capsuleDetailsState by viewModel.capsuleDetailsState.collectAsState()

  val isLoading = capsuleDetailsState is DisplayCapsuleDetailsState.Loading

  val isSuccess = capsuleDetailsState is DisplayCapsuleDetailsState.Success

  var capsuleDetails: CapsuleDetails? by remember {
    mutableStateOf(null)
  }

  LaunchedEffect(Unit) {
    viewModel.getCapsuleDetails(capsuleId)
  }

  LaunchedEffect(capsuleDetailsState) {
    when (capsuleDetailsState) {
      is DisplayCapsuleDetailsState.Success -> {
        capsuleDetails =
          (capsuleDetailsState as DisplayCapsuleDetailsState.Success).capsuleDetails
      }

      is DisplayCapsuleDetailsState.Error -> {}
      is DisplayCapsuleDetailsState.Loading -> {}
    }
  }
  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(androidx.compose.material3.MaterialTheme.colorScheme.primary)
  ) {
    LazyColumn(
      modifier = Modifier
          .fillMaxSize()
          .zIndex(0f),
      contentPadding = PaddingValues(horizontal = 1.dp, vertical = 10.dp),
    ) {
      item {
        BackRow(onBack)
      }
      item {
        CapsuleDetailsSection(capsuleDetails, isSuccess, isLoading)
      }
    }
  }
}

@Composable
fun TopImageSection(isSuccess: Boolean, modelId: Int) {

  if (isSuccess) {
    val imagePath: String by remember {
      mutableStateOf(getModelImage(modelId.toString()))
    }

    val context = LocalContext.current

    val flagBitmap: Bitmap? = remember(imagePath) {
      try {
        val inputStream = context.assets.open(imagePath)
        BitmapFactory.decodeStream(inputStream)
      } catch (e: Exception) {
        null
      }
    }
    Row(
      modifier = Modifier
          .fillMaxWidth()
          .wrapContentHeight()
          .padding(horizontal = 5.dp, vertical = 15.dp)
          .padding(bottom = 25.dp),
      horizontalArrangement = Arrangement.SpaceBetween

    ) {

      Box(
        modifier = Modifier
            .fillMaxWidth(0.6F)
            .height(300.dp)
            .drawBehind {
                val shadowColor = getModelColor(modelId = modelId.toString())
                val paint = android.graphics
                    .Paint()
                    .apply {
                        color = shadowColor.toArgb()
                        setShadowLayer(35f, 0f, 0f, color)
                    }
                drawContext.canvas.nativeCanvas.apply {
                    save()
                    drawRoundRect(
                        0f,
                        0f,
                        size.width,
                        size.height,
                        30f,
                        30f,
                        paint
                    )
                    restore()
                }
            }
      ) {
        Card(
          elevation = 0.dp,
          shape = RoundedCornerShape(16.dp),
          modifier = Modifier
              .fillMaxSize()
              .zIndex(3.0f),
          backgroundColor = Color.Black
        ) {
          if (flagBitmap != null) {
            Image(
              bitmap = flagBitmap.asImageBitmap(),
              contentDescription = "Capsule Image",
              contentScale = ContentScale.Fit,
              modifier = Modifier.fillMaxSize()
            )
          } else {
            Box(
              contentAlignment = Alignment.Center,
              modifier = Modifier.fillMaxSize()
            ) {
              Text(text = "Image not found", color = Color.White)
            }
          }
        }
      }
      Card(
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 10.dp)
            .fillMaxWidth(0.8F)
            .height(100.dp)
      ) {
        Box(
          modifier = Modifier
              .fillMaxSize()
              .background(Color.Black),
          contentAlignment = Alignment.Center
        ) {
          androidx.compose.material3.Text(
            text = "Model ${modelId}",
            style = androidx.compose.material3.MaterialTheme.typography.titleMedium.copy(
              color = Color.White,
              fontSize = 20.sp, fontFamily = overSeer
            )
          )
        }
      }
    }
  }
}

@Composable
fun CapsuleDetailsSection(capsuleDetails: CapsuleDetails?, isSuccess: Boolean, isLoading: Boolean) {
  Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 5.dp, horizontal = 10.dp)
  ) {
    if (isSuccess)
      capsuleDetails?.let {
        TopImageSection(isSuccess, capsuleDetails.modelId.toInt())
      }

    if (isLoading) {
      Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)

            .padding(horizontal = 16.dp, vertical = 15.dp)
            .padding(bottom = 25.dp)
            .clip(RoundedCornerShape(20.dp))
            .placeholder(
                visible = isLoading,
                color = Color.Gray.copy(alpha = 0.1f),
                highlight = PlaceholderHighlight.shimmer()
            )
      )
    }

    if (isSuccess)
      capsuleDetails?.let {
        Text(
          text = capsuleDetails.title,
          fontSize = 28.sp,
          fontWeight = FontWeight.Bold,
          color = LightBlue
        )
      }

    if (isLoading) {
      Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(10.dp))
            .placeholder(
                visible = isLoading,
                color = Color.Gray.copy(alpha = 0.1f),
                highlight = PlaceholderHighlight.shimmer()
            )
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    if (isSuccess) {
      capsuleDetails?.let {
        Text(
          text = capsuleDetails.description,
          fontSize = 16.sp,
          color = Color.Gray
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Opening Time
    CapsuleOpeningTime(isSuccess, capsuleDetails?.time)

    if (isLoading) {
      Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(60.dp))
            .placeholder(
                visible = isLoading,
                color = Color.Gray.copy(alpha = 0.1f),
                highlight = PlaceholderHighlight.shimmer()
            )
      )
    }

    Spacer(modifier = Modifier.height(26.dp))

    if (isLoading)
      LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        items(4) {
          Box(
            modifier = Modifier
                .size(70.dp)
                .placeholder(
                    visible = isLoading,
                    shape = CircleShape,
                    highlight = PlaceholderHighlight.shimmer(),
                    color = Color.Gray.copy(alpha = 0.3f),
                )
                .clip(shape = CircleShape)
          )
          Spacer(modifier = Modifier.width(10.dp))
        }
      }

    // Shared Profiles Section
    SharedProfilesSection(isSuccess, capsuleDetails?.isSharedWithAll, capsuleDetails?.users)

    Spacer(modifier = Modifier.height(26.dp))

    // Map Section
    GoogleMapPreview(isSuccess, capsuleDetails?.location)
  }
}

@Composable
fun CapsuleOpeningTime(isSuccess: Boolean, timestamp: Timestamp?) {

  if (isSuccess)
    timestamp?.let {
      Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                LightBlue.copy(alpha = 0.1f),
                shape = RoundedCornerShape(50)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          painter = painterResource(id = com.example.timecapsule.R.drawable.ic_time_range),  // Replace with clock icon
          contentDescription = "Clock Icon",
          tint = LightBlue,
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "Opens: " + timestamp.toDate().toString(),
          fontSize = 16.sp,
          color = LightBlue
        )
      }
    }
}

@Composable
fun SharedProfilesSection(
  isSuccess: Boolean,
  isSharedWithAll: Boolean?,
  users: List<Map<String, Any>>?
) {
  if (isSuccess) {
    users?.let {
      Text(
        text = "Shared With:",
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {

        users.let {
          items(it) { user ->
            Profile(
              isOwner = user["isOwner"] as Boolean,
              userName = user["userName"] as String,
              imageUrl = user["imageUrl"] as String,
              disableCrossBtn = true,
              remove = {})
          }
        }
        if (isSharedWithAll == true) {
          item {
            SharedWithALlIcon(modifier = Modifier.size(70.dp))
          }
        }
      }
    }
  }
}

@Composable
fun GoogleMapPreview(isSuccess: Boolean, geoPoint: GeoPoint?) {
  if (isSuccess) {

    geoPoint?.let {
      Text(
        text = "Location:",
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))


      MapPreviewCard(
        latlang = com.google.android.gms.maps.model.LatLng(
          geoPoint.latitude,
          geoPoint.longitude
        )
      )
    }
  }
}
