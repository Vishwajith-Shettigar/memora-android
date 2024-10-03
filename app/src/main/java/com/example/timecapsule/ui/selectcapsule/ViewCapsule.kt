package com.example.timecapsule.ui.selectcapsule

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.timecapsule.R
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.theme.Inter
import com.example.timecapsule.ui.theme.darkCardBackground
import com.example.timecapsule.ui.theme.darkPrimaryBackground
import com.example.timecapsule.ui.theme.overSeer
import com.example.timecapsule.ui.util.DeviceType
import com.example.util.modelIdMap
import com.mapbox.maps.extension.style.expressions.dsl.generated.heatmapDensity
import io.github.sceneview.Scene
import io.github.sceneview.SceneView
import io.github.sceneview.animation.Transition.animateRotation
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.model.Model
import io.github.sceneview.model.model
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberEnvironmentLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Preview
@Composable
fun ViewCapsule() {
  val isTablet = DeviceType.isTablet()
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
        BackRow(Modifier.padding(innerPadding))
        Display3DModel(Modifier.padding(innerPadding))
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
                text = "Model 100-M",
                style = MaterialTheme.typography.titleMedium.copy(
                  color = Color.White,
                  fontSize = 30.sp, fontFamily = overSeer
                )
              )
              Text(
                text = "The wizard capsule",
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
                  text = "500 MB",
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
                  text = "Free",
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
fun BackRow(modifier: Modifier) {
  Row(
    modifier = modifier
        .fillMaxWidth()
        .zIndex(10.0F)
        .padding(horizontal = 20.dp)
        .background(Color.Transparent),
    horizontalArrangement = Arrangement.Start
  ) {
    IconButton(
      onClick = { }, modifier =
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
  modifier: Modifier = Modifier,
  modelFileName: String = "testmodel.glb"
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
  ) {
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
              assetFileLocation = modelFileName
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
