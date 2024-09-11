//package com.example.timecapsule.ar
//
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.example.timecapsule.BuildConfig
//import com.example.timecapsule.R
//import com.example.timecapsule.ui.theme.TimeCapsuleTheme
//import com.google.ar.sceneform.ux.ArFragment
//import com.mapbox.common.MapboxOptions
//
//
//class ArActivity : AppCompatActivity() {
//  var arFragment: ArFragment? = null
//
//  override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//    enableEdgeToEdge()
//    setContentView(R.layout.activity_ar)
//    arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment?
//    arFragment?.setOnTapPlaneGlbModel("testmodel.glb")
//  }
//}
