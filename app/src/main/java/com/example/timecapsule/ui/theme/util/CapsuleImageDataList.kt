package com.example.timecapsule.ui.theme.util

import CapsuleImage
import com.example.timecapsule.R
import java.util.UUID

fun createCapsuleImageList(): List<CapsuleImage> {
  return (1..11).map {
    CapsuleImage(
      imageId = "capsule_image$it.png",
      imageName = getDrawableResourceById(it)
    )
  }
}

fun getDrawableResourceById(id: Int): Int {
  return when (id) {
    1 -> R.drawable.capsule_image1
    2 -> R.drawable.capsule_image2
    3 -> R.drawable.capsule_image3
    4 -> R.drawable.capsule_image4
    5 -> R.drawable.capsule_image5
    6 -> R.drawable.capsule_image6
    7 -> R.drawable.capsule_image7
    8 -> R.drawable.capsule_image8
    9 -> R.drawable.capsule_image9
    10 -> R.drawable.capsule_image10
    11 -> R.drawable.capsule_image11
    else -> throw IllegalArgumentException("Invalid image id")
  }
}