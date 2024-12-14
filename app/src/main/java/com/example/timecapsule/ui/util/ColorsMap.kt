package com.example.timecapsule.ui.util

import androidx.compose.ui.graphics.Color
import com.example.timecapsule.ui.theme.cardGreen
import com.example.timecapsule.ui.theme.cardViolet
import com.example.timecapsule.ui.theme.cardYellow

object ColorsMap {
  val colors: List<Color> = listOf(
    cardGreen,
    cardViolet, cardYellow
  )

  fun getColor(index: Int): Color {
    return colors[(index % colors.size)]
  }
}