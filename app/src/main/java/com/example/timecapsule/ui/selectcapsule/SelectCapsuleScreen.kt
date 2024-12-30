package com.example.timecapsule.ui.selectcapsule

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.model.CapsuleAsset
import com.example.timecapsule.ui.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.selecttime.NavigationRow
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel
import com.example.timecapsule.viewmodel.CapsuleSelectionState


fun getCapsuleImageUrl(capsuleModelId: String, capsuleAssets: List<CapsuleAsset>): String? {
  capsuleAssets.forEach { assets ->
    if (assets.capsule_id == capsuleModelId)
      return assets.imageUrl
  }
  return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SelectCapsuleScreen(
  modifier: Modifier = Modifier, viewModel: CapsuleCreationViewModel = hiltViewModel(),
  onNavigate: (NavigationAddCapsule) -> Unit = {}, onViewCapsuleClick: (CapsuleAsset) -> Unit = {}
) {
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
  val isTablet = DeviceType.isTablet()

  val capsuleAssets = remember {
    mutableStateListOf<CapsuleAsset>()
  }
  val capsuleAssetsState by viewModel.capsuleSelectionState.collectAsState()

  val selectedCapsuleId by viewModel.selectedCapsuleModelId.collectAsState()

  var isLoading: Boolean = capsuleAssetsState is CapsuleSelectionState.Loading
  var isSuccess: Boolean = capsuleAssetsState is CapsuleSelectionState.Success

  val context = LocalContext.current

  LaunchedEffect(key1 = Unit) {
    viewModel.getCapsuleAssets()
  }

  LaunchedEffect(key1 = capsuleAssetsState) {
    when (capsuleAssetsState) {
      is CapsuleSelectionState.Success -> {
        capsuleAssets.clear()
        capsuleAssets.addAll((capsuleAssetsState as CapsuleSelectionState.Success).data)
      }

      is CapsuleSelectionState.Error -> {
      }

      CapsuleSelectionState.Loading -> {}
    }
  }

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
              Modifier.padding(vertical = 18.dp, horizontal = 10.dp)
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
          .padding(
              start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
              end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
              top = innerPadding.calculateTopPadding()
          )
          .fillMaxSize()

    ) {
      if (isLoading)
        Column(
          modifier = Modifier.fillMaxSize(),
          verticalArrangement = Arrangement.Center,
          horizontalAlignment = Alignment.CenterHorizontally
        ) {

          CircularProgressIndicator(
            modifier = Modifier
              .size(44.dp),
            color = Color.White,
            strokeWidth = 2.dp,
            backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

      if (isSuccess)
        CapsuleList(
          Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
          selectedCapsuleId,
          capsuleAssets,
          onViewCapsuleClick
        ) { capsuleModelId, imageUrl, cost, storage ->
          viewModel.selectedCapsuleModelId.value = capsuleModelId
          viewModel.selectedCapsuleImageUrl = imageUrl
          viewModel.amount = cost
          viewModel.capsuleSizeInMB = storage
        }

      Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp)
            .align(Alignment.BottomCenter)
            .zIndex(2f)
      ) {
        NavigationRow { navigationFlow ->
          if (viewModel.selectedCapsuleModelId.value == null)
            Toast.makeText(context, "Please select a capsule", Toast.LENGTH_SHORT).show()
          else
            onNavigate(navigationFlow)
        }
      }
    }
  }
}

@Composable
fun CapsuleList(
  modifier: Modifier,
  selectedCapsuleModelId: String? = null,
  capsuleAssets: List<CapsuleAsset>,
  onViewCapsuleClick: (CapsuleAsset) -> Unit = {},
  setCapsuleModelIdAndImageUrlAmount: (String, String, Int, Double) -> Unit
) {
  val isTablet = DeviceType.isTablet()
  if (isTablet) {
    CapsuleListTablet(
      modifier = modifier,
      capsuleAssets,
      onViewCapsuleClick,
      setCapsuleModelIdAndImageUrlAmount
    )
  } else {
    CapsuleListMobile(
      modifier = modifier,
      selectedCapsuleModelId,
      capsuleAssets,
      onViewCapsuleClick,
      setCapsuleModelIdAndImageUrlAmount
    )
  }
}

@Composable
fun CapsuleListMobile(
  modifier: Modifier = Modifier,
  selectedCapsuleModelId: String? = null,
  capsuleAssets: List<CapsuleAsset>,
  onViewCapsuleClick: (CapsuleAsset) -> Unit = {},
  setCapsuleModelIdAndImageUrl: (String, String, Int, Double) -> Unit
) {

  LazyVerticalStaggeredGrid(
    modifier = modifier
      .background(Color.Transparent),
    columns = StaggeredGridCells.Fixed(2),
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    verticalItemSpacing = 8.dp,
    content = {
      items(capsuleAssets) {
        Capsule(
          it,
          selectedCapsuleModelId == it.capsule_id,
          onViewCapsuleClick = onViewCapsuleClick
        ) {

          val imageUrl = it.capsule_id.let { getCapsuleImageUrl(it, capsuleAssets) }
          if (imageUrl != null) {
            setCapsuleModelIdAndImageUrl(
              it.capsule_id,
              imageUrl,
              it.cost.toInt(),
              it.storage.toDouble()
            )
          }
        }
      }
    }
  )
}

@Composable
fun CapsuleListTablet(
  modifier: Modifier = Modifier,
  capsuleAssets: List<CapsuleAsset>,
  onViewCapsuleClick: (CapsuleAsset) -> Unit = {},
  setCapsuleModelIdAndImageUrl: (String, String, Int, Double) -> Unit
) {
  var selectedCapsuleId by rememberSaveable { mutableStateOf<String>("") }

  LaunchedEffect(selectedCapsuleId) {

    val capsule = capsuleAssets.find { it.capsule_id == selectedCapsuleId }
    val cost = capsule!!.cost
    val storage = capsule!!.storage
    val imageUrl = getCapsuleImageUrl(selectedCapsuleId, capsuleAssets)
    if (imageUrl != null) {
      setCapsuleModelIdAndImageUrl(selectedCapsuleId, imageUrl, cost.toInt(), storage.toDouble())
    }

  }

  LazyVerticalStaggeredGrid(
    columns = StaggeredGridCells.Adaptive(minSize = 400.dp),
    modifier = modifier
        .fillMaxSize()
        .background(Color.Transparent),
    contentPadding = PaddingValues(8.dp),
    horizontalArrangement = Arrangement.spacedBy(10.dp),
    verticalItemSpacing = 8.dp,
    content = {
      items(capsuleAssets) {
        Capsule(it, selectedCapsuleId == it.capsule_id, onViewCapsuleClick = onViewCapsuleClick) {
          selectedCapsuleId = it.capsule_id
        }
      }
    }
  )
}

@Composable
fun Capsule(
  capsuleAssets: CapsuleAsset,
  isSelected: Boolean = false,
  onViewCapsuleClick: (CapsuleAsset) -> Unit = {}, onSelect: () -> Unit = {}
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
      AsyncImage(
        modifier = Modifier.heightIn(min = 200.dp),
        model = capsuleAssets.imageUrl,
        contentDescription = "capsule 1",
      )
      OutlinedButton(
        onClick = { onViewCapsuleClick(capsuleAssets) },
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
