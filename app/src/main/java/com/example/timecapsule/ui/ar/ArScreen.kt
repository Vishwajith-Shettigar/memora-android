package com.example.timecapsule.ui.ar

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.filament.Engine
import com.google.ar.core.Anchor
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.loaders.MaterialLoader
import io.github.sceneview.loaders.ModelLoader
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.node.CubeNode
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberOnGestureListener
import io.github.sceneview.rememberView

@Composable
fun ArScreen() {
//  val engine = rememberEngine()
//  val modelLoader = rememberModelLoader(engine)

//  val cameraNode = rememberARCameraNode(engine).apply {
//    position = Position(z = 4.0f)
//  }
//  val centerNode = rememberNode(engine).apply {
//    addChildNode(cameraNode)
//  }
  Scaffold { innerPadding ->
//    ARScene(modifier = Modifier.padding(innerPadding),
//
//      engine = engine,
//      modelLoader = modelLoader,
//      cameraNode = cameraNode,
//      childNodes = listOf(
//        centerNode,
//        rememberNode {
//          ModelNode(
//            modelInstance = modelLoader.createModelInstance(
//              assetFileLocation = "testmodel2folder/scene.gltf"
//            ),
//            scaleToUnits = 0.3f
//          )
//        }
//      ),
//
//      // Fundamental session features that can be requested.
//      sessionFeatures = setOf(),
//      // The camera config to use.
//      // The config must be one returned by [Session.getSupportedCameraConfigs].
//      // Provides details of a camera configuration such as size of the CPU image and GPU texture.
//      sessionCameraConfig = null,
//      // Configures the session and verifies that the enabled features in the specified session config
//      // are supported with the currently set camera config.
//      sessionConfiguration = { session, config ->
//        config.depthMode =
//          when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
//            true -> Config.DepthMode.AUTOMATIC
//            else -> Config.DepthMode.DISABLED
//          }
//        config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
//        config.lightEstimationMode =
//          Config.LightEstimationMode.ENVIRONMENTAL_HDR
//      },
//      planeRenderer = true,
//      // The [ARCameraStream] to render the camera texture.
//      // Use it to control if the occlusion should be enabled or disabled.
////      cameraStream = rememberARCameraStream(materialLoader),
//      // The session is ready to be accessed.
//      onSessionCreated = { session ->
//      },
//      // The session has been resumed.
//      onSessionResumed = { session ->
//      },
//      // The session has been paused
//      onSessionPaused = { session ->
//      },
//      // Updates of the state of the ARCore system.
//      // This includes: receiving a new camera frame, updating the location of the device, updating
//      // the location of tracking anchors, updating detected planes, etc.
//      // This call may update the pose of all created anchors and detected planes. The set of updated
//      // objects is accessible through [Frame.getUpdatedTrackables].
//      // Invoked once per [Frame] immediately before the Scene is updated.
//      onSessionUpdated = { session, updatedFrame ->
//      },
//      // Invoked when an ARCore error occurred.
//      // Registers a callback to be invoked when the ARCore Session cannot be initialized because
//      // ARCore is not available on the device or the camera permission has been denied.
//      onSessionFailed = { exception ->
//      },
//      // Listen for camera tracking failure.
//      // The reason that [Camera.getTrackingState] is [TrackingState.PAUSED] or `null` if it is
//      // [TrackingState.TRACKING]
//      onTrackingFailureChanged = { trackingFailureReason ->
//      }
//    )

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
                anchor = anchor
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
                  anchor = anchor
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
  anchor: Anchor
): AnchorNode {
  val kModelFile = "testmodel.glb"
  val kMaxModelInstances = 1
  val anchorNode = AnchorNode(engine = engine, anchor = anchor)
  val modelNode = ModelNode(
    modelInstance = modelInstances.apply {
      if (isEmpty()) {
        this += modelLoader.createInstancedModel(kModelFile, kMaxModelInstances)
      }
    }.removeLast(),
    // Scale to fit in a 0.5 meters cube
    scaleToUnits = 0.5f
  ).apply {
    // Model Node needs to be editable for independent rotation from the anchor rotation
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
  return anchorNode
}
