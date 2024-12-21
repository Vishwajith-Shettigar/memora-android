package com.example.util

import android.util.Log
import java.util.HashMap

private val modelIdMap = mapOf(
  "100" to "testmodel.glb",
  "200" to "testmodel.glb",
  "300" to "testmodel.glb",
  "400" to "testmodel.glb",
)

private val modelIdImageMap = mapOf(
  "100" to "capsule_images/model1.png",
  "200" to "capsule_images/model2.png",
  "300" to "capsule_images/model3.png",
  "400" to "capsule_images/model4.png",
)

fun getModel(id: String): String {
  return modelIdMap.get(id) ?: "testmodel.glb"
}

fun getModelImage(id: String): String {
  return modelIdImageMap.get(id) ?: "capsule_images/model1.png"
}

private val modelIdMapIconsMap = mapOf(
  "100" to "capsulesmap_icons/model1_map_icon.png",
  "200" to "capsulesmap_icons/model2_map_icon.png",
  "300" to "capsulesmap_icons/model3_map_icon.png",
  "400" to "capsulesmap_icons/model4_map_icon.png",
)

fun getModelMapIcon(id: String): String {
  return modelIdMapIconsMap.get(id) ?: "capsulesmap_icons/model1_map_icon.png"
}
