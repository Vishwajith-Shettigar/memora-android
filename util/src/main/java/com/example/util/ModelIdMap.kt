package com.example.util

import java.util.HashMap

private val modelIdMap = mapOf(
  "100" to "testmodel.glb",
  "200" to "testmodel.glb",
  "300" to "testmodel.glb",
  "400" to "testmodel.glb",
)

fun getModel(id:String):String{
  return modelIdMap.get(id)?:"testmodel.glb"
}
