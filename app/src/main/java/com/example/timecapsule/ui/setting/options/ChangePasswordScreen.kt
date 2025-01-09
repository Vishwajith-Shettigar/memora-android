package com.example.timecapsule.ui.setting.options

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.timecapsule.ui.login.TitleSubtitleWithOkayButtonDialog
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.signup.CustomTextField
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.util.DeviceType
import com.example.timecapsule.viewmodel.ResetPasswordState
import com.example.timecapsule.viewmodel.ResetPasswordViewModel
import com.example.util.Response


@Composable
fun ChangePasswordScreen(
  viewModel: ResetPasswordViewModel = hiltViewModel(),
  onBackClick: () -> Unit
) {

  val isTablet = DeviceType.isTablet()

  val context = LocalContext.current

  val isEmailError = viewModel.email is Response.Error

  val resetPasswordState by viewModel.resetPasswordState.collectAsState()

  var showSuccessDialog by remember {
    mutableStateOf(false)
  }

  var showResetLimitExceededDialog by remember {
    mutableStateOf(false)
  }

  var emailInput by remember {
    mutableStateOf("")
  }

  var isEmailMissMatch by remember {
    mutableStateOf(false)
  }

  LaunchedEffect(resetPasswordState) {

    if (resetPasswordState is ResetPasswordState.Success) {
      emailInput = ""
      showSuccessDialog = true
      return@LaunchedEffect
    }
    if (resetPasswordState is ResetPasswordState.Error && (resetPasswordState as ResetPasswordState.Error).message != null) {
      showResetLimitExceededDialog = true
      return@LaunchedEffect
    }
  }



  LaunchedEffect(isEmailError) {
    if (isEmailError) {
      Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
      onBackClick()
    }
  }

  if (showSuccessDialog) {
    TitleSubtitleWithOkayButtonDialog(
      title = "Email has Sent",
      subtitle = "An email has been sent to your email address. Please follow the instructions to reset your password."
    ) {
      showSuccessDialog = false
    }
  }

  if (showResetLimitExceededDialog) {
    TitleSubtitleWithOkayButtonDialog(
      title = "Limit Exceeded",
      subtitle = (resetPasswordState as ResetPasswordState.Error).message ?: ""
    ) {
      showResetLimitExceededDialog = false
    }
  }

  Column(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary).systemBarsPadding()
        .padding(top = 20.dp),
    verticalArrangement = Arrangement.Top
  ) {
    BackRow() {
      onBackClick()
    }
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
        text = "Below enter your registered email address.",
        style = MaterialTheme.typography.titleLarge.copy(
          fontSize = 15.sp,
          fontWeight = FontWeight.SemiBold,
          lineHeight = TextUnit(20.0F, TextUnitType.Sp)
        ),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      CustomTextField(
        modifier = Modifier.padding(vertical = 20.dp),
        value = emailInput,
        hint = "Enter your email address",
        icon = com.example.timecapsule.R.drawable.ic_email,
        isTablet = isTablet,
        isError = isEmailMissMatch,
        errorText = if (isEmailMissMatch) "Email doesn't match with registered email" else null,
        onValueChanged = {
          emailInput = it
          if (isEmailMissMatch)
            isEmailMissMatch = false
        }
      )

      Button(
        onClick = {
          if (emailInput.trim().equals((viewModel.email as Response.Success).data)) {
            viewModel.sendPasswordResetEmail()
          } else {
            isEmailMissMatch = true
          }

        }, colors =
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

        if (resetPasswordState is ResetPasswordState.Loading) {
          CircularProgressIndicator(
            modifier = Modifier
              .size(19.dp),
            color = Color.White,
            strokeWidth = 2.dp,
            backgroundColor = MaterialTheme.colorScheme.onSurfaceVariant
          )
        } else
          Text(
            text =
            if ((resetPasswordState is ResetPasswordState.Success) || (resetPasswordState is ResetPasswordState.Idle)) {
              "Send Reset Link"
            } else {
              "Something went wrong! Try again"
            },
            style = MaterialTheme.typography.titleLarge.copy(
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
          )
      }
    }
  }
}
