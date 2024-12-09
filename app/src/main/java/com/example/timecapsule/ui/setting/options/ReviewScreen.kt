package com.example.timecapsule.ui.setting.options

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.theme.fiveStarColor
import com.example.timecapsule.ui.theme.threeStarColor
import com.example.timecapsule.ui.theme.zeroStarColor
import com.example.timecapsule.ui.util.DeviceType

/**
 * An object that provides rating expressions and colors based on the given rating.
 * This object helps in selecting a visual representation (stars) and color based on the rating value.
 */
object RatingsArtMapper {

  /**
   * Returns a composable function that represents the rating expression (stars) for a given rating.
   *
   * This function maps the rating value to a corresponding composable expression:
   * - 0.0f to 0.19f: ZeroStarExpression (No stars)
   * - 0.2f to 0.4f: ThreeStarExpression (Three stars)
   * - 0.4f and above: FiveStarExpression (Five stars)
   *
   * @param rating The rating value used to determine the expression.
   * @return A composable function representing the rating expression (ZeroStar, ThreeStar, or FiveStar).
   */
  @Composable
  fun getRatingExpression(rating: Float): @Composable () -> Unit {
    Log.e("pokemon", rating.toString())
    return when (rating) {
      in 0.0f..0.19f -> {
        { ZeroStarExpression() }
      }

      in 0.2f..0.4f -> {
        { ThreeStarExpression() }
      }

      else -> {
        { FiveStarExpression() }
      }
    }
  }

  /**
   * Returns the color associated with the given rating.
   *
   * This function maps the rating value to a corresponding color:
   * - 0.0f to 0.19f: `zeroStarColor` (representing no stars)
   * - 0.2f to 0.4f: `threeStarColor` (representing three stars)
   * - 0.4f and above: `fiveStarColor` (representing five stars)
   *
   * @param rating The rating value used to determine the color.
   * @return The color associated with the rating.
   */
  fun getRatingColor(rating: Float): Color {
    return when (rating) {
      in 0.0f..0.19f -> zeroStarColor
      in 0.2f..0.4f -> threeStarColor
      else -> fiveStarColor
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen() {
  var sliderPosition by remember { mutableStateOf(0.0f) }

  val isTablet = DeviceType.isTablet()

  var bgColor by remember {
    mutableStateOf(RatingsArtMapper.getRatingColor(sliderPosition))
  }

  LaunchedEffect(sliderPosition) {
    bgColor = RatingsArtMapper.getRatingColor(sliderPosition)
  }

  Column(
    modifier = Modifier
        .fillMaxSize()
        .background(bgColor)
        .padding(top = 10.dp),
  ) {
    BackRow()
    Column(
      modifier = Modifier
          .fillMaxSize()
          .background(bgColor)
          .padding(16.dp),
      horizontalAlignment =
      Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceAround
    ) {

      Text(
        text = "Hi! Rate Us.",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black
      )

      RatingsArtMapper.getRatingExpression(rating = sliderPosition)()

      Slider(modifier =
      Modifier
          .padding(horizontal = 16.dp)
          .then(
              if (isTablet)
                  Modifier.width(400.dp)
              else
                  Modifier.fillMaxWidth()
          ),
        value = sliderPosition,
        onValueChange = { sliderPosition = it },
        valueRange = 0f..0.5f,
        colors = SliderDefaults.colors(
          thumbColor = Color.Black,
          activeTrackColor = LightBlue,
          inactiveTrackColor = Color.Gray,
          activeTickColor = Color.Green,
          inactiveTickColor = Color.LightGray
        ),
        thumb = {
          Box(
            modifier = Modifier
                .size(30.dp)
                .background(
                    color = Color.Black,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    5.dp,
                    brush = Brush.horizontalGradient(listOf(LightBlue, Color.Gray)),
                    shape = RoundedCornerShape(10.dp)
                )
          )
        }
      )


      Button(
        onClick = { },
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
      ) {
        Icon(
          painter = painterResource(id = com.example.timecapsule.R.drawable.ic_reviews),
          contentDescription = "review icon",
          tint = LightBlue
        )
        Spacer(modifier = Modifier.width(10.dp))

        Text(
          text = "Add a comment",
          style = MaterialTheme.typography.labelLarge.copy(
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            fontSize = 17.sp
          )
        )
      }
    }
  }
}

@Composable
fun FiveStarExpression() {
  Canvas(modifier = Modifier.size(150.dp)) {
    val faceCenter = center

    val eyeWidth = size.width * 0.40f
    val eyeHeight = size.height * 0.60f
    val eyeOffsetX = size.width * 0.3f
    val eyeCenterY = faceCenter.y - size.height * 0.3f

// Draw left eye (oval shape)
    drawOval(
      color = Color.Black,
      topLeft = Offset(faceCenter.x - eyeOffsetX - eyeWidth / 2, eyeCenterY - eyeHeight / 2),
      size = Size(eyeWidth, eyeHeight),
      style = Stroke(width = 5f)
    )

// Draw right eye (oval shape)
    drawOval(
      color = Color.Black,
      topLeft = Offset(
        faceCenter.x + eyeOffsetX - (eyeWidth) / 2,
        eyeCenterY - (eyeHeight - 50) / 2
      ),
      size = Size(eyeWidth, eyeHeight - 50),
      style = Stroke(width = 7f)
    )


    // Draw eye pupils
    val pupilRadius = size.minDimension * 0.070f
    drawCircle(
      color = Color.Black,
      radius = pupilRadius,
      center = Offset(faceCenter.x - eyeOffsetX, eyeCenterY)
    )
    drawCircle(
      color = Color.Black,
      radius = pupilRadius + 20.0F,
      center = Offset(faceCenter.x + eyeOffsetX, eyeCenterY)
    )

    // Draw mouth (wavy line)
    val mouthWidth = size.width * 0.99f
    val mouthHeight = size.height * 0.6f
    val mouthStartX = faceCenter.x - mouthWidth / 2
    val mouthStartY = faceCenter.y + size.height * 0.2f

    val path = Path().apply {
      moveTo(mouthStartX, mouthStartY)
      cubicTo(
        mouthStartX, mouthStartY,
        mouthStartX + mouthWidth / 2, mouthStartY + mouthHeight,
        mouthStartX + mouthWidth, mouthStartY,
      )
    }
    drawPath(
      path = path,
      color = Color.Black,
      style = Stroke(width = 13f, cap = StrokeCap.Round)
    )
  }
}

@Composable
fun ZeroStarExpression() {
  Canvas(modifier = Modifier.size(150.dp)) {
    val faceCenter = center

    val eyeWidth = size.width * 0.30f
    val eyeHeight = size.height * 0.40f
    val eyeOffsetX = size.width * 0.3f
    val eyeCenterY = faceCenter.y - size.height * 0.3f

// Draw left eye (oval shape)
    drawOval(
      color = Color.Black,
      topLeft = Offset(faceCenter.x - eyeOffsetX - eyeWidth / 2, eyeCenterY - eyeHeight / 2),
      size = Size(eyeWidth, eyeHeight),
      style = Stroke(width = 5f)
    )

// Draw right eye (oval shape)
    drawOval(
      color = Color.Black,
      topLeft = Offset(faceCenter.x + eyeOffsetX - eyeWidth / 2, eyeCenterY - eyeHeight / 2),
      size = Size(eyeWidth, eyeHeight),
      style = Stroke(width = 5f)
    )


    // Draw eye pupils
    val pupilRadius = size.minDimension * 0.070f
    drawCircle(
      color = Color.Black,
      radius = pupilRadius,
      center = Offset(faceCenter.x - eyeOffsetX, eyeCenterY)
    )
    drawCircle(
      color = Color.Black,
      radius = pupilRadius,
      center = Offset(faceCenter.x + eyeOffsetX, eyeCenterY)
    )

    // Draw mouth (wavy line)
    val mouthWidth = size.width * 0.90f
    val mouthHeight = size.height * 0.6f
    val mouthStartX = faceCenter.x - mouthWidth / 2
    val mouthStartY = faceCenter.y + size.height * 0.2f

    val path = Path().apply {
      moveTo(mouthStartX, mouthStartY)
      cubicTo(
        mouthStartX, mouthStartY + mouthHeight,
        mouthStartX + mouthWidth / 2, mouthStartY + -mouthHeight,
        mouthStartX + mouthWidth, mouthStartY + mouthHeight / 2,
      )
    }
    drawPath(
      path = path,
      color = Color.Black,
      style = Stroke(width = 8f, cap = StrokeCap.Round)
    )
  }
}

@Composable
fun ThreeStarExpression() {
  Canvas(modifier = Modifier.size(150.dp)) {
    val faceCenter = center

    val eyeWidth = size.width * 0.30f
    val eyeHeight = size.height * 0.60f
    val eyeOffsetX = size.width * 0.3f
    val eyeCenterY = faceCenter.y - size.height * 0.3f

    drawOval(
      color = Color.Black,
      topLeft = Offset(faceCenter.x - eyeOffsetX - eyeWidth / 2, eyeCenterY - eyeHeight / 2),
      size = Size(eyeWidth, eyeHeight),
      style = Stroke(width = 15f)
    )

    drawOval(
      color = Color.Black,
      topLeft = Offset(faceCenter.x + eyeOffsetX - eyeWidth / 2, eyeCenterY - eyeHeight / 2),
      size = Size(eyeWidth, eyeHeight),
      style = Stroke(width = 15f)
    )


    val pupilRadius = size.minDimension * 0.090f
    drawCircle(
      color = Color.Black,
      radius = pupilRadius,
      center = Offset(faceCenter.x - eyeOffsetX, eyeCenterY - eyeHeight / 2 + pupilRadius)
    )
    drawCircle(
      color = Color.Black,
      radius = pupilRadius,
      center = Offset(faceCenter.x + eyeOffsetX, eyeCenterY - eyeHeight / 2 + pupilRadius)
    )

    val mouthWidth = size.width * 0.90f
    val mouthHeight = size.height * 0.6f
    val mouthStartX = faceCenter.x - mouthWidth / 2
    val mouthStartY = faceCenter.y + size.height * 0.3f

    val path = Path().apply {
      moveTo(mouthStartX, mouthStartY)

      cubicTo(
        mouthStartX + mouthWidth / 3,
        mouthStartY,
        mouthStartX + 2 * mouthWidth / 3,
        mouthStartY + mouthHeight,
        mouthStartX + mouthWidth,
        mouthStartY
      )
    }
    drawPath(
      path = path,
      color = Color.Black,
      style = Stroke(width = 8f, cap = StrokeCap.Round)
    )
  }
}
