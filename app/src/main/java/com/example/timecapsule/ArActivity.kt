package com.example.timecapsule

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.doOnAttach
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import com.example.timecapsule.viewmodel.ArViewModel
import com.google.ar.core.Config
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.ar.ARSceneView
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.getDescription
import io.github.sceneview.ar.node.AnchorNode
import io.github.sceneview.node.ModelNode
import java.io.File
import java.nio.channels.FileChannel
import javax.inject.Inject
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArActivity : AppCompatActivity() {

  @Inject
  lateinit var viewModel: ArViewModel

  lateinit var sceneView: ARSceneView
  lateinit var loadingView: View
  lateinit var instructionText: TextView

  var isLoading = false
    set(value) {
      field = value
      loadingView.isGone = !value
    }

  var anchorNode: AnchorNode? = null
    set(value) {
      if (field != value) {
        field = value
        updateInstructions()
      }
    }

  var trackingFailureReason: TrackingFailureReason? = null
    set(value) {
      if (field != value) {
        field = value
        updateInstructions()
      }
    }

  fun updateInstructions() {
    instructionText.text = trackingFailureReason?.let {
      it.getDescription(this)
    } ?: if (anchorNode == null) {
      "Point your phone down"
    } else {
      null
    }
  }

  override fun onDestroy() {
    super.onDestroy()
    sceneView.destroy()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(com.example.timecapsule.R.layout.activity_ar)

    // Example of calling the model load with a sample ID

    val modelId = intent.getStringExtra("modelId") ?: "100"
    viewModel.loadModel(modelId)

    setFullScreen(
      findViewById(com.example.timecapsule.R.id.rootView),
      fullScreen = true,
      hideSystemBars = false,
      fitsSystemWindows = false
    )

    instructionText = findViewById(com.example.timecapsule.R.id.instructionText)
    loadingView = findViewById(com.example.timecapsule.R.id.loadingView)

    sceneView = findViewById<ARSceneView>(com.example.timecapsule.R.id.sceneView).apply {
      lifecycle = this@ArActivity.lifecycle
      planeRenderer.isEnabled = true
      configureSession { session, config ->
        config.depthMode = when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
          true -> Config.DepthMode.AUTOMATIC
          else -> Config.DepthMode.DISABLED
        }
        config.instantPlacementMode = Config.InstantPlacementMode.DISABLED
        config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
      }
      onSessionUpdated = { _, frame ->
        if (anchorNode == null) {
          frame.getUpdatedPlanes()
            .firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }
            ?.let { plane ->
              if (viewModel.modelPath.value != null)
                addAnchorNode(viewModel.modelPath.value!!)
            }
        }
      }
      onTrackingFailureChanged = { reason ->
        this@ArActivity.trackingFailureReason = reason
      }
    }

  }

  fun addAnchorNode(modelPath: String) {

    // Ensure there is a valid Plane to create an Anchor
    val plane = sceneView.frame?.getUpdatedPlanes()
      ?.firstOrNull { it.type == Plane.Type.HORIZONTAL_UPWARD_FACING }

    plane?.let {
      // Create anchor using the plane's center pose
      val anchor = it.createAnchor(it.centerPose)

      // Create the AnchorNode using the anchor
      sceneView.addChildNode(
        AnchorNode(sceneView.engine, anchor).apply {
          isEditable = true
          lifecycleScope.launch {
            isLoading = true

            val fileBuffer =
              File(modelPath).inputStream().channel.map(
                FileChannel.MapMode.READ_ONLY,
                0,
                File(modelPath).length()
              )

            val modelInstance = sceneView.modelLoader.createInstancedModel(fileBuffer, 1)
            addChildNode(
              ModelNode(
                modelInstance = modelInstance.get(0),
                scaleToUnits = 1f,  // Scale to fit the model
                autoAnimate = true
              ).apply {
                isEditable = true
              }
            )

            isLoading = false
          }
          anchorNode = this
        }
      )
    }
  }
}

fun Activity.setFullScreen(
  rootView: View,
  fullScreen: Boolean = true,
  hideSystemBars: Boolean = true,
  fitsSystemWindows: Boolean = true
) {
  rootView.doOnApplyWindowInsets { _ ->
    WindowCompat.setDecorFitsSystemWindows(window, fitsSystemWindows)
    WindowInsetsControllerCompat(window, rootView).apply {
      if (hideSystemBars) {
        if (fullScreen) {
          hide(
            WindowInsetsCompat.Type.statusBars() or
              WindowInsetsCompat.Type.navigationBars()
          )
        } else {
          show(
            WindowInsetsCompat.Type.statusBars() or
              WindowInsetsCompat.Type.navigationBars()
          )
        }
        systemBarsBehavior =
          WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
      }
    }
  }
}

fun View.doOnApplyWindowInsets(action: (systemBarsInsets: Insets) -> Unit) {
  doOnAttach {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
      action(insets.getInsets(WindowInsetsCompat.Type.systemBars()))
      WindowInsetsCompat.CONSUMED
    }
  }
}
