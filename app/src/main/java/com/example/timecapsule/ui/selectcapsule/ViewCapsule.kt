package com.example.timecapsule.ui.selectcapsule

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.model.CapsuleAsset
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.darkPrimaryBackground
import com.example.timecapsule.ui.theme.overSeer
import com.example.timecapsule.util.DeviceType
import com.example.timecapsule.viewmodel.Load3dModelState
import com.example.timecapsule.viewmodel.ViewCapsuleViewModel
import com.example.util.getModelImage
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import java.io.File

@Composable
fun ViewCapsule(
  capsuleAsset: CapsuleAsset,
  viewModel: ViewCapsuleViewModel = hiltViewModel(),
  onBackClick: () -> Unit = {}
) {
  val isTablet = DeviceType.isTablet()

  val loading3dModelState by viewModel.loadingLoad3dModelState.collectAsState()

  Scaffold(
    modifier = Modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.primary)
      .padding(vertical = 30.dp),
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.onSecondaryContainer),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(400.dp)
          .shadow(
            2.dp,
            ambientColor = Color.White,
            spotColor = Color.White,
            shape = RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp)
          )
          .clip(shape = RoundedCornerShape(bottomEnd = 50.dp, bottomStart = 50.dp))
      )
      {
        BackRow(Modifier.padding(innerPadding), onBackClick)
        Display3DModel(capsuleAsset, loading3dModelState) {
          viewModel.load3dModel(capsuleAsset.capsule_id)
        }
      }

      val colMod = if (isTablet) {
          Modifier
              .fillMaxHeight()
              .width(500.dp)
      } else {
        Modifier.fillMaxSize()
      }

      Column(
        modifier = colMod
          .padding(vertical = 30.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

      ) {
        Row(
          modifier = Modifier
              .fillMaxWidth()
              .wrapContentHeight()
        ) {
          ElevatedCard(
            modifier = Modifier
                .height(190.dp)
                .weight(1.3F)
                .shadow(
                    10.dp,
                    RoundedCornerShape(10.dp),
                    ambientColor = Color.White,
                    spotColor = Color.White
                )
                .clip(RoundedCornerShape(10.dp)),
            colors = CardDefaults.elevatedCardColors(darkPrimaryBackground),
          ) {
            Column(
              modifier = Modifier
                  .fillMaxSize()
                  .background(darkPrimaryBackground),
              verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = capsuleAsset.capsuleName,
                style = MaterialTheme.typography.titleMedium.copy(
                  color = Color.White,
                  fontSize = 25.sp, fontFamily = overSeer
                )
              )
              Text(
                text = capsuleAsset.description,
                style = MaterialTheme.typography.titleMedium.copy(
                  color = Color.White,
                  fontSize = 10.sp,
                )
              )
            }
          }

          Column(
            modifier = Modifier
                .padding(start = 6.dp)
                .height(190.dp)
                .weight(1.0F),
            verticalArrangement = Arrangement.SpaceBetween
          ) {
            ElevatedCard(
              modifier = Modifier
                  .fillMaxWidth()
                  .height(90.dp)
                  .shadow(
                      2.dp,
                      RoundedCornerShape(5.dp),
                      ambientColor = Color.White,
                      spotColor = Color.White
                  )
                  .clip(RoundedCornerShape(5.dp)),
              colors = CardDefaults.elevatedCardColors(darkPrimaryBackground),
              shape = RoundedCornerShape(5.dp)
            ) {
              Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize(), verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
              ) {
                Text(
                  text = "${capsuleAsset.storage} MB",
                  style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                  )
                )
                Text(
                  text = "Storage",
                  style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold
                  )
                )
              }
            }
            ElevatedCard(
              modifier = Modifier
                  .fillMaxWidth()
                  .height(90.dp)
                  .shadow(
                      2.dp,
                      RoundedCornerShape(5.dp),
                      ambientColor = Color.White,
                      spotColor = Color.White
                  )
                  .clip(RoundedCornerShape(5.dp)),
              colors = CardDefaults.elevatedCardColors(darkPrimaryBackground),
              shape = RoundedCornerShape(5.dp)
            ) {
              Column(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxSize(), verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
              ) {
                Text(
                  text = if (capsuleAsset.isPaid) "${capsuleAsset.cost} $" else "Free",
                  style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                  )
                )
                Text(
                  text = "Cost",
                  style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold
                  )
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun BackRow(modifier: Modifier, onBackClick: () -> Unit = {}) {
  Row(
    modifier = modifier
        .fillMaxWidth()
        .zIndex(10.0F)
        .padding(horizontal = 20.dp)
        .background(Color.Transparent),
    horizontalArrangement = Arrangement.Start
  ) {
    IconButton(
      onClick = { onBackClick() }, modifier =
        Modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(1.dp, Color.Gray, CircleShape)
    ) {
      Icon(
        painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "back",
        tint = Color.Gray
      )
    }
  }
}

@Composable
fun Display3DModel(
  capsuleAsset: CapsuleAsset,
  loading3dModelState: Load3dModelState, onViewIn3dClick: () -> Unit
) {

  val context = LocalContext.current

  Box(
    modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
  ) {

    if (!(loading3dModelState is Load3dModelState.Success))
      Load3dModelButton(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .zIndex(10.0F)
            .padding(5.dp), loading3dModelState, onViewIn3dClick
      )

    if (loading3dModelState is Load3dModelState.Idle
      || loading3dModelState is Load3dModelState.Error
      || loading3dModelState is Load3dModelState.Loading
    ) {
      val flagBitmap: Bitmap? = remember() {
        try {
          val inputStream =
            context.assets.open(getModelImage(capsuleAsset.capsule_id))
          BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
          null
        }
      }
      Box(
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center)
      ) {
        if (flagBitmap != null) {
          Image(
            modifier = Modifier.align(Alignment.Center),
            bitmap = flagBitmap.asImageBitmap(),
            contentDescription = "capsule_image"
          )
        }
      }
    } else {
      val modelFilePath = (loading3dModelState as Load3dModelState.Success).path

      val modelFile = File(modelFilePath)
      val engine = rememberEngine()
      val modelLoader = rememberModelLoader(engine)

      val cameraNode = rememberCameraNode(engine).apply {
        position = Position(z = 4.0f)
      }
      val centerNode = rememberNode(engine).apply {
        addChildNode(cameraNode)
      }
      Scene(
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
        engine = engine,
        modelLoader = modelLoader,
        cameraNode = cameraNode,
        childNodes = listOf(
          centerNode,
          rememberNode {
            ModelNode(
              modelInstance = modelLoader.createModelInstance(
                file = modelFile
              ),
              scaleToUnits = 1.7f
            )
          }
        ),
        onFrame = {
          cameraNode.lookAt(centerNode)
        }
      )
    }
  }
}


@Composable
fun Load3dModelButton(
  modifier: Modifier,
  loading3dModelState: Load3dModelState,
  onViewIn3dClick: () -> Unit,
) {

  Button(
    modifier = modifier, colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
    onClick = { onViewIn3dClick() }) {
    Row {
      Icon(
        modifier = Modifier.padding(horizontal = 5.dp), tint = LightBlue,
        painter = painterResource(id = com.example.timecapsule.R.drawable.icon_view_in_ar),
        contentDescription = "view 3d icon"
      )
      Text(
        text =
        if (loading3dModelState is Load3dModelState.Loading) "Loading..."
        else if (loading3dModelState is Load3dModelState.Error) "Retry"
        else
          "View 3D", color = Color.White
      )
    }
  }
}