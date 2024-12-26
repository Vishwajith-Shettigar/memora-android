package com.example.timecapsule.ui.ar

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.timecapsule.viewmodel.ArViewModel
import com.example.timecapsule.viewmodel.Load3dModelState
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import com.mapbox.maps.extension.style.model.model
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.model.model
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView
import java.io.File
import java.nio.channels.FileChannel

@Composable
fun ArScreen(modelId: String, viewModel: ArViewModel = hiltViewModel()) {

  LaunchedEffect(Unit) {
    viewModel.loadModel(modelId)
  }

  val state by viewModel.loadingLoad3dModelState.collectAsState()

  LaunchedEffect(state) {
    if (state is Load3dModelState.Success)
  }

  val engine = rememberEngine()
  val modelLoader = rememberModelLoader(engine)
  val materialLoader = rememberMaterialLoader(engine)
  val cameraNode = rememberARCameraNode(engine)
  val childNodes = rememberNodes()
  val view = rememberView(engine)
  val collisionSystem = rememberCollisionSystem(view)

  var planeRenderer by remember { mutableStateOf(true) }

  val modelInstances = remember { mutableListOf<ModelInstance>() }
  var trackingFailureReason by remember {
    mutableStateOf<TrackingFailureReason?>(null)
  }

  var frame by remember { mutableStateOf<Frame?>(null) }


  Scaffold { innerPadding ->


    if (state is Load3dModelState.Success)
      ARScene(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        childNodes = childNodes,
        engine = engine,
        view = view,
        modelLoader = modelLoader,
        collisionSystem = collisionSystem,
        sessionConfiguration = { session, config ->
          config.depthMode =
            when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
              true -> Config.DepthMode.AUTOMATIC
              else -> Config.DepthMode.DISABLED
            }
          config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
          config.lightEstimationMode =
            Config.LightEstimationMode.ENVIRONMENTAL_HDR
        },
        cameraNode = cameraNode,
        planeRenderer = planeRenderer,
        onTrackingFailureChanged = {
          trackingFailureReason = it
        },
        onSessionUpdated = { session, updatedFrame ->
          frame = updatedFrame

          if (childNodes.isEmpty()) {
            updatedFrame.getUpdatedPlanes()
              .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
              ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
                childNodes += createAnchorNode(
                  engine = engine,
                  modelLoader = modelLoader,
                  materialLoader = materialLoader,
                  modelInstances = modelInstances,
                  anchor = anchor,
                  path = (state as Load3dModelState.Success).path
                )
              }
          }
        },
        onGestureListener = rememberOnGestureListener(
          onSingleTapConfirmed = { motionEvent, node ->
            if (node == null) {
              val hitResults = frame?.hitTest(motionEvent.x, motionEvent.y)
              hitResults?.firstOrNull {
                it.isValid(
                  depthPoint = false,
                  point = false
                )
              }?.createAnchorOrNull()
                ?.let { anchor ->
                  planeRenderer = false
                  childNodes += createAnchorNode(
                    engine = engine,
                    modelLoader = modelLoader,
                    materialLoader = materialLoader,
                    modelInstances = modelInstances,
                    anchor = anchor,
                    path = (state as Load3dModelState.Success).path
                  )
                }
            }
          })
      )
  }
}

fun createAnchorNode(
  engine: Engine,
  modelLoader: ModelLoader,
  materialLoader: MaterialLoader,
  modelInstances: MutableList<ModelInstance>,
  anchor: Anchor,
  path: String
): AnchorNode {
  val anchorNode = AnchorNode(engine = engine, anchor = anchor)

  try {
    // Convert ByteArray to Buffer
    val fileBuffer =
      File(path).inputStream().channel.map(FileChannel.MapMode.READ_ONLY, 0, File(path).length())

    val modelInstance = modelLoader.createInstancedModel(fileBuffer, 1) // 1 = Max model instances
    modelInstances.addAll(modelInstance)

    val modelNode = ModelNode(
      modelInstance = modelInstances.removeLast(),
      scaleToUnits = 0.5f
    ).apply {
      isEditable = true
    }

    val boundingBoxNode = CubeNode(
      engine,
      size = modelNode.extents,
      center = modelNode.center,
      materialInstance = materialLoader.createColorInstance(Color.White.copy(alpha = 0.5f))
    ).apply {
      isVisible = false
    }

    modelNode.addChildNode(boundingBoxNode)
    anchorNode.addChildNode(modelNode)

    listOf(modelNode, anchorNode).forEach {
      it.onEditingChanged = { editingTransforms ->
        boundingBoxNode.isVisible = editingTransforms.isNotEmpty()
      }
    }
  } catch (e: Exception) {
  }

  return anchorNode
}

