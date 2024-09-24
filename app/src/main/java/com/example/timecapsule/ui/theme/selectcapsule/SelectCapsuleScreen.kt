package com.example.timecapsule.ui.theme.selectcapsule

import CapsuleImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.review.SelectedCapsule
import com.example.timecapsule.ui.theme.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.theme.selecttime.NavigationRow
import com.example.timecapsule.ui.theme.util.DeviceType
import com.example.timecapsule.ui.theme.util.createCapsuleImageList

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SelectCapsuleScreen(
  modifier: Modifier = Modifier,
  onNavigate: (NavigationAddCapsule) -> Unit = {}
) {
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
  val isTablet = DeviceType.isTablet()

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    containerColor = MaterialTheme.colorScheme.primary,
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Select a capsule",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 30.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            modifier =
            if (isTablet) {
              Modifier.padding(vertical = 38.dp, horizontal = 20.dp)

            } else {
              Modifier.padding(vertical = 28.dp, horizontal = 10.dp)
            }
          )
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.primary)
      )

    }
  ) { innerPadding ->
    Box(
      modifier = modifier
          .padding(innerPadding)
          .fillMaxSize()

    ) {
      CapsuleList(Modifier.nestedScroll(scrollBehavior.nestedScrollConnection))
      Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .align(Alignment.BottomCenter)
            .zIndex(2f)
      ) {
        NavigationRow { navigationFlow ->
          onNavigate(navigationFlow)
        }
      }
    }
  }
}


@Composable
fun CapsuleList(modifier: Modifier) {
  val isTablet = DeviceType.isTablet()
  if (isTablet) {
    CapsuleListTablet(modifier = modifier)
  } else {
    CapsuleListMobile(modifier = modifier)
  }
}

@Composable
fun CapsuleListMobile(modifier: Modifier = Modifier) {
  var selectedCapsuleId by rememberSaveable { mutableStateOf<String>("") }
  LazyVerticalStaggeredGrid(
    modifier = modifier
      .background(Color.Transparent),
    columns = StaggeredGridCells.Fixed(2),
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalItemSpacing = 8.dp,
    content = {
      items(createCapsuleImageList()) {
        Capsule(it, selectedCapsuleId == it.imageId) {
          selectedCapsuleId = it.imageId
        }
      }
    }
  )
}

@Composable
fun CapsuleListTablet(modifier: Modifier = Modifier) {
  var selectedCapsuleId by remember { mutableStateOf<String>("") }

  LazyVerticalStaggeredGrid(
    columns = StaggeredGridCells.Adaptive(minSize = 400.dp),
    modifier = modifier
        .fillMaxSize()
        .background(Color.Transparent),
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    verticalItemSpacing = 8.dp,
    content = {
      items(createCapsuleImageList()) { it ->
        Capsule(it, selectedCapsuleId == it.imageId) {
          selectedCapsuleId = it.imageId
        }
      }
    }
  )
}

@Composable
fun Capsule(
  capsuleImage: CapsuleImage = CapsuleImage("capsule_image1", R.drawable.capsule_image1),
  isSelected: Boolean = false,
  onSelect: () -> Unit = {}
) {
  Card(
    colors =
    if (isSelected) {
      CardDefaults.cardColors(Color.Green.copy(alpha = 0.3f))
    } else {
      CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)

    },
    modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth(),
    elevation = CardDefaults.cardElevation(4.dp),
    shape = RoundedCornerShape(6.dp),
    onClick = {
      onSelect()
    }
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier
          .padding(vertical = 15.dp)
          .fillMaxSize()
          .clip(shape = RoundedCornerShape(6.dp))
          .background(Color.Transparent)
    ) {
      Image(
        painter = painterResource(id = capsuleImage.imageName),
        contentDescription = "capsule 1",
      )
      OutlinedButton(
        onClick = { },
        modifier = Modifier
            .height(40.dp)
            .width(100.dp),
        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
      ) {
        Text(
          text = "View", modifier = Modifier.align(Alignment.CenterVertically),
          style = MaterialTheme.typography.titleSmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}
