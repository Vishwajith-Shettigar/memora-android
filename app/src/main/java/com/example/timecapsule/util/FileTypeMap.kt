package com.example.timecapsule.util

import android.util.Log
import com.example.timecapsule.R

val fileImageMap: Map<String, Int> = mapOf(
  "docx" to R.drawable.doc,
  "pdf" to R.drawable.pdf,
  "img" to R.drawable.img,
  "mp4" to R.drawable.videocamera,
  "mp3" to R.drawable.playlist,
  "xls" to R.drawable.xls
)

fun getFileImageID(type: String): Int {
  Log.e("#", type)
  return fileImageMap[type] ?: R.drawable.ic_launcher_foreground
}