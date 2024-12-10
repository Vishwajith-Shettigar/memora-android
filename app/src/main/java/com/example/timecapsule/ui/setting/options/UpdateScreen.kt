package com.example.timecapsule.ui.setting.options

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.util.DeviceType

val featureDescriptions = listOf(
  "UI changes",
  "Bux fixes and improvements",
  "Google auth added"
)

@Composable
fun UpdateScreen(onBackClick: () -> Unit) {

  val isTablet = DeviceType.isTablet()

  Column(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary)
        .padding(top = 20.dp),
    verticalArrangement = Arrangement.Top
  ) {
    BackRow() {
      onBackClick()
    }
    Column(
      modifier = Modifier
          .fillMaxSize()
          .padding(20.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        modifier = Modifier.padding(vertical = 20.dp),
        text = "Your app is up to date.",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 25.sp,
          fontWeight = FontWeight.ExtraBold
        ),
        color = LightBlue
      )

      Column(
        modifier = Modifier
            .then(
                if (isTablet)
                    Modifier.width(600.dp)
                else
                    Modifier.fillMaxWidth()
            )
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(10.dp)
      ) {
        Text(
          text = "Update Information:",
          style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
          text = "version: v1.0",
          style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = "Whats new:",
          style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )
        featureDescriptions.forEach {
          Text(
            text = it,
            style = MaterialTheme.typography.titleLarge.copy(
              fontSize = 15.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }
      }

      Button(
        onClick = { /*TODO*/ }, colors =
        ButtonDefaults.buttonColors(
          containerColor = LightBlue
        ),
        contentPadding = PaddingValues(vertical = 15.dp),
        shape = RoundedCornerShape(10.dp),
        modifier =

        if (!isTablet)
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(vertical = 20.dp)
        else
            Modifier
                .width(600.dp)
                .height(100.dp)
                .padding(vertical = 20.dp)
      ) {
        Text(
          modifier = Modifier.padding(horizontal = 10.dp),
          text = "Update",
          style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
          ),
          color = MaterialTheme.colorScheme.primary
        )
      }
    }
  }
}
