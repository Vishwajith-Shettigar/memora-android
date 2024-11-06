package com.example.timecapsule.util

import android.util.Log
import com.example.timecapsule.R

val fileImageMap: Map<String, Int> = mapOf(
  "docx" to R.drawable.doc,
  "pdf" to R.drawable.pdf,
  "img" to R.drawable.image,
  "jpeg" to R.drawable.image,
  "jpg" to R.drawable.image,
  "png" to R.drawable.image,
  "mp4" to R.drawable.videocamera,
  "mp3" to R.drawable.playlist,
  "xls" to R.drawable.xls
)

fun getFileImageID(type: String): Int {
  return fileImageMap.getOrDefault(type, R.drawable.ic_upload_file)
}
