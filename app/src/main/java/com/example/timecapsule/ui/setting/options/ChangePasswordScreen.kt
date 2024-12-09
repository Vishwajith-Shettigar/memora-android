package com.example.timecapsule.ui.setting.options

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.R
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.signup.CustomTextField
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.util.DeviceType


@Composable
fun ChangePasswordScreen() {

  val isTablet = DeviceType.isTablet()

  Scaffold { innerPadding ->
    Column(
      modifier = Modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.primary)
          .padding(innerPadding)
          .padding(top = 20.dp),
      verticalArrangement = Arrangement.Top
    ) {
      BackRow()
      Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalAlignment =
        if (isTablet)
          Alignment.CenterHorizontally
        else
          Alignment.Start
      ) {
        Text(
          modifier = Modifier.padding(vertical = 10.dp),
          text = "Reset Password",
          style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
          ),
          color = LightBlue
        )
        Text(
          text = "Your new password must be different from previous used password.",
          style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = TextUnit(20.0F, TextUnitType.Sp)
          ),
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CustomTextField(
          modifier = Modifier.padding(vertical = 20.dp),
          value = "",
          hint = "Enter current password",
          icon = R.drawable.ic_password1,
          isTablet = isTablet,
          trailingIcon = R.drawable.ic_eye,
          onTrailingIconClicked = {},
          onValueChanged = {}
        )

        CustomTextField(
          value = "",
          hint = "Enter new password",
          icon = R.drawable.ic_password1,
          isTablet = isTablet,
          trailingIcon = R.drawable.ic_eye,
          onTrailingIconClicked = {},
          onValueChanged = {}
        )

        CustomTextField(
          value = "",
          hint = "Confirm new password",
          icon = R.drawable.ic_cnpassword,
          isTablet = isTablet,
          trailingIcon = R.drawable.ic_eye,
          onTrailingIconClicked = {},
          onValueChanged = {}
        )

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
                  .wrapContentHeight()
                  .padding(vertical = 20.dp)
          else
              Modifier
                  .width(600.dp)
                  .wrapContentHeight()
                  .padding(vertical = 20.dp)

        ) {
          Text(
            text = "Reset Password",
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
}
