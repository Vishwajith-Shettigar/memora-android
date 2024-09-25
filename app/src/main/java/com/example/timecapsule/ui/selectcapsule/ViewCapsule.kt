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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.timecapsule.R
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
  Scaffold(
    modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
        .padding(vertical = 30.dp),
    containerColor = Color.Black,
    topBar = {
      BackRow()
    },
  ) { innerPadding ->
    Display3DModel(Modifier.padding(innerPadding))
  }
}

@Composable
fun BackRow() {
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
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
    Box(
      modifier = Modifier
        .align(Alignment.Center)
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
            .background(Color.Transparent),
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
              scaleToUnits = 0.3f
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
