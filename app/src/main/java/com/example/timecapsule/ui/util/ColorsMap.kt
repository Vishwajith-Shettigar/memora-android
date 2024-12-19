package com.example.timecapsule.ui.util

import androidx.compose.ui.graphics.Color
import com.example.timecapsule.ui.theme.cardGreen
import com.example.timecapsule.ui.theme.cardOrange
import com.example.timecapsule.ui.theme.cardViolet
import com.example.timecapsule.ui.theme.cardYellow
import com.example.timecapsule.ui.theme.model1Color
import com.example.timecapsule.ui.theme.model2Color
import com.example.timecapsule.ui.theme.model3Color
import com.example.timecapsule.ui.theme.model4Color

object ColorsMap {
  val colors: List<Color> = listOf(
    cardGreen,
    cardViolet, cardYellow, cardOrange
  )

  fun getColor(index: Int): Color {
    return colors[(index % colors.size)]
  }
}

val ModelIdColorsMap = mapOf(
  "100" to model1Color,
  "200" to model2Color,
  "300" to model3Color,
  "400" to model4Color
)

fun getModelColor(modelId: String): Color {
  return ModelIdColorsMap.get(modelId) ?: model1Color
}
