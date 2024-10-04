package com.example.util

import com.example.timecapsule.R

val fileImageMap: Map<String, String> = mapOf(
  "docx" to "R.drawable.doc",
  "pdf" to "R.drawable.pdf",
  "img" to "R.drawable.img",
  "mp4" to "R.drawable.videocamera",
  "mp3" to "R.drawable.playlist",
  "xls" to "R.drawable.xls"
)

fun getFileImageID(type: String): Int {
  return fileImageMap[type]?.toInt()!!
}