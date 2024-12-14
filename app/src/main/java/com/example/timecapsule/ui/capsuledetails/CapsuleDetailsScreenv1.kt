package com.example.timecapsule.ui.capsuledetails


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlin.math.max
import kotlin.math.min

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.cardViolet

@Composable
fun CapsuleDetailsScreenv1() {
  SwipeToRevealCapsuleScreen()
}

//@Composable
//fun EnhancedCapsuleDetailScreen() {
//  Column(
//    modifier = Modifier
//      .fillMaxSize().background(cardViolet)
//      .padding(16.dp)
//      .verticalScroll(rememberScrollState())
//  ) {
//    // Capsule Image
//    Box(
//      modifier = Modifier
//          .fillMaxWidth()
//          .padding(top = 16.dp),
//      contentAlignment = Alignment.Center
//    ) {
//      Image(
//        painter = painterResource(id = com.example.timecapsule.R.drawable.capsule_image3), // Replace with your image resource
//        contentDescription = "Capsule Image",
//        contentScale = ContentScale.Crop,
//        modifier = Modifier
//          .size(200.dp)
//          .clip(CircleShape)
//          .background(Color.LightGray)
//          .shadow(8.dp, CircleShape)
//      )
//    }
//
//    Spacer(modifier = Modifier.height(16.dp))
//
//    // Capsule Title and Description
//    Text(
//      text = "Time Capsule Title",
//      fontSize = 28.sp,
//      fontWeight = FontWeight.Bold,
//      color = MaterialTheme.colors.primary,
//      modifier = Modifier
//          .fillMaxWidth()
//          .padding(horizontal = 16.dp)
//    )
//
//    Spacer(modifier = Modifier.height(8.dp))
//
//    Card(
//      shape = RoundedCornerShape(16.dp),
//      elevation = 4.dp,
//      modifier = Modifier.fillMaxWidth()
//    ) {
//      Text(
//        text = "This is the description of the capsule. It provides a detailed explanation of its contents and significance.",
//        fontSize = 16.sp,
//        modifier = Modifier
//            .padding(16.dp)
//            .background(MaterialTheme.colors.surface)
//      )
//    }
//
//    Spacer(modifier = Modifier.height(16.dp))
//
//    // Opening Time
//    Row(
//      modifier = Modifier
//          .fillMaxWidth()
//          .padding(horizontal = 16.dp)
//          .background(
//              MaterialTheme.colors.primary.copy(alpha = 0.1f),
//              shape = RoundedCornerShape(50)
//          )
//          .padding(16.dp),
//      verticalAlignment = Alignment.CenterVertically
//    ) {
//      Icon(
//        painter = painterResource(id = com.example.timecapsule.R.drawable.ic_time_range), // Replace with your clock icon
//        contentDescription = "Clock Icon",
//        tint = MaterialTheme.colors.primary,
//        modifier = Modifier.size(24.dp)
//      )
//      Spacer(modifier = Modifier.width(8.dp))
//      Text(
//        text = "Opens: 12 Dec 2024, 10:00 AM",
//        fontSize = 16.sp,
//        color = MaterialTheme.colors.primary
//      )
//    }
//
//    Spacer(modifier = Modifier.height(16.dp))
//
//    // Shared Profiles
//    Column(modifier = Modifier.fillMaxWidth()) {
//      Text(
//        text = "Shared With:",
//        fontSize = 20.sp,
//        fontWeight = FontWeight.Medium,
//        modifier = Modifier.padding(horizontal = 16.dp)
//      )
//
//      Spacer(modifier = Modifier.height(8.dp))
//
//      Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(horizontal = 16.dp),
//        horizontalArrangement = Arrangement.spacedBy(12.dp)
//      ) {
//        repeat(5) {
//          Image(
//            painter = painterResource(id = com.example.timecapsule.R.drawable.testimg1), // Replace with actual images
//            contentDescription = "Profile Picture",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .size(60.dp)
//                .clip(CircleShape)
//                .background(Color.Gray)
//          )
//        }
//      }
//    }
//
//    Spacer(modifier = Modifier.height(16.dp))
//
//    // Google Map Location
//    Text(
//      text = "Location:",
//      fontSize = 20.sp,
//      fontWeight = FontWeight.Medium,
//      modifier = Modifier.padding(horizontal = 16.dp)
//    )
//
//    Spacer(modifier = Modifier.height(8.dp))
//
//    Card(
//      shape = RoundedCornerShape(16.dp),
//      elevation = 4.dp,
//      modifier = Modifier
//          .fillMaxWidth()
//          .height(200.dp)
//          .padding(horizontal = 16.dp)
//    ) {
//      Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.LightGray)
//      ) {
//        Text(
//          text = "Google Map Preview",
//          modifier = Modifier.align(Alignment.Center),
//          fontSize = 16.sp,
//          fontWeight = FontWeight.Bold,
//          color = Color.White
//        )
//      }
//    }
//
//    Spacer(modifier = Modifier.height(16.dp))
//
//    // Action Buttons
//    Row(
//      modifier = Modifier
//          .fillMaxWidth()
//          .padding(horizontal = 16.dp),
//      horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//      Button(onClick = { /* TODO: Open Capsule */ }) {
//        Text(text = "Open Capsule")
//      }
//
//      Button(onClick = { /* TODO: Share Location */ }) {
//        Text(text = "Share Location")
//      }
//    }
//  }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun EnhancedCapsuleDetailScreenPreview() {
//  EnhancedCapsuleDetailScreen()
//}

@Composable
fun SwipeToRevealCapsuleScreen() {
  val maxHeight = 300.dp  // Full height for the image at the start
  val minHeight = 100.dp  // Height of the image when collapsed at the top
  var scrollOffset by remember { mutableStateOf(0f) }  // Track scroll offset for animation

  // Collapsing height logic
  val imageHeight = lerp(
    start = maxHeight,
    stop = minHeight,
    fraction = min(1f, scrollOffset / 300f)
  )

  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(Color.White)
  ) {
    // LazyColumn for Scrollable Content
    LazyColumn(
      modifier = Modifier
          .fillMaxSize()
          // Add dynamic padding based on image height
          .zIndex(0f),
      contentPadding = PaddingValues(horizontal = 16.dp),
    ) {
      // Capsule Details
      item {
        CapsuleDetailsSection()
      }
    }
  }
}

@Composable
fun TopImageSection(imageHeight: Dp) {
  Box(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 16.dp)
  ) {
    // Left Card
    Card(
      elevation = 8.dp,
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
          .padding(vertical = 10.dp)
          .fillMaxWidth(0.3F)
          .height(100.dp)
          .align(Alignment.TopEnd)
    ) {
      Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
      ) {
        Text("Right Card", color = Color.Black, fontSize = 16.sp)
      }
    }

    // Center Card with Capsule Image
    Card(
      elevation = 12.dp,
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
          .height(300.dp)
          .fillMaxWidth(0.7F)
          .align(Alignment.TopStart)
          .zIndex(3.0F)
          .padding(horizontal = 5.dp),
      backgroundColor = Color.LightGray
    ) {
      Image(
        painter = painterResource(id = com.example.timecapsule.R.drawable.capsule_image3), // Replace with your image
        contentDescription = "Capsule Image",
        contentScale = ContentScale.Fit,
        modifier = Modifier.fillMaxSize()
      )
    }

    // Right Card
    Card(
      elevation = 8.dp,
      shape = RoundedCornerShape(16.dp),
      modifier = Modifier
          .padding(vertical = 10.dp)
          .fillMaxWidth(0.3F)
          .height(100.dp)
          .align(Alignment.BottomEnd)
    ) {
      Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
      ) {
        Text("Right Card", color = Color.Black, fontSize = 16.sp)
      }
    }
  }
}

@Composable
fun CapsuleDetailsSection() {
  Column(
    modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 16.dp)
  ) {
    TopImageSection(imageHeight = 150.dp)

    // Title
    Text(
      text = "Time Capsule Title",
      fontSize = 28.sp,
      fontWeight = FontWeight.Bold,
      color = MaterialTheme.colors.primary
    )

    Spacer(modifier = Modifier.height(8.dp))

    // Description
    Text(
      text = "This is a detailed description of the capsule. It explains the content and significance.",
      fontSize = 16.sp,
      color = Color.Gray
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Opening Time
    CapsuleOpeningTime()

    Spacer(modifier = Modifier.height(16.dp))

    // Shared Profiles Section
    SharedProfilesSection()

    Spacer(modifier = Modifier.height(16.dp))

    // Map Section
    GoogleMapPreview()
  }
}

@Composable
fun CapsuleOpeningTime() {
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .background(
            MaterialTheme.colors.primary.copy(alpha = 0.1f),
            shape = RoundedCornerShape(50)
        )
        .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      painter = painterResource(id = com.example.timecapsule.R.drawable.ic_time_range),  // Replace with clock icon
      contentDescription = "Clock Icon",
      tint = MaterialTheme.colors.primary,
      modifier = Modifier.size(24.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
      text = "Opens: 12 Dec 2024, 10:00 AM",
      fontSize = 16.sp,
      color = MaterialTheme.colors.primary
    )
  }
}

@Composable
fun SharedProfilesSection() {
  Text(
    text = "Shared With:",
    fontSize = 20.sp,
    fontWeight = FontWeight.Medium
  )

  Spacer(modifier = Modifier.height(8.dp))

  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    repeat(5) {
      Image(
        painter = painterResource(id = com.example.timecapsule.R.drawable.img),  // Replace with real images
        contentDescription = "Profile Picture",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Color.Gray)
      )
    }
  }
}

@Composable
fun GoogleMapPreview() {
  Text(
    text = "Location:",
    fontSize = 20.sp,
    fontWeight = FontWeight.Medium
  )

  Spacer(modifier = Modifier.height(8.dp))

  Card(
    shape = RoundedCornerShape(16.dp),
    elevation = 4.dp,
    modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
  ) {
    Box(
      modifier = Modifier
          .fillMaxSize()
          .background(Color.LightGray)
    ) {
      Text(
        text = "Google Map Preview",
        modifier = Modifier.align(Alignment.Center),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
fun SwipeToRevealCapsuleScreenPreview() {
  SwipeToRevealCapsuleScreen()
}
