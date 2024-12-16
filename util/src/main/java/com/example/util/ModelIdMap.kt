package com.example.util

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

