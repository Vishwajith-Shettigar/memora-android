package com.example.timecapsule.ui.review

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timecapsule.BuildConfig
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.ReviewScreenCommondColor
import com.example.timecapsule.ui.theme.RubikBubble
import com.example.timecapsule.ui.selecttime.NavigationAddCapsule
import com.example.timecapsule.ui.sharewithpeople.ShowSelectedPeople
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.uploadfiles.UploadedFileItem
import com.example.timecapsule.ui.util.DeviceType
import com.example.timecapsule.viewmodel.CapsuleCreationViewModel
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.stripe.android.paymentsheet.rememberPaymentSheet

enum class PaymentReadyStatus {
  READY,
  LOADING,
  FAIL,
  IDLE
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailsBottomSheet(
  viewModel: CapsuleCreationViewModel,
  paymentReady: PaymentReadyStatus,
  changeDialog: (Boolean) -> Unit,
  onConfirm: () -> Unit
) {
  val context = LocalContext.current
  val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  val name = viewModel.name.value
  val address = viewModel.address.value
  val postalCode = viewModel.postalCode.value
  val city = viewModel.city.value
  val state = viewModel.state.value
  val country = viewModel.country.value

  ModalBottomSheet(
    onDismissRequest = { changeDialog(false) },
    sheetState = bottomSheetState,
  ) {
    Column(
      modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(text = "Enter Details", style = MaterialTheme.typography.titleLarge)

      // Input Fields
      OutlinedTextField(
        value = name,
        onValueChange = { viewModel.updateName(it) },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
          focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
          focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
      OutlinedTextField(
        value = address,
        onValueChange = { viewModel.updateAddress(it) },
        label = { Text("Address Line 1") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
          focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
          focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
      OutlinedTextField(
        value = postalCode,
        onValueChange = {
          viewModel.updatePostalCode(it)
        },
        label = { Text("Postal Code") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
          focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
          focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
      OutlinedTextField(
        value = city,
        onValueChange = { viewModel.updateCity(it) },
        label = { Text("City") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
          focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
          focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
      OutlinedTextField(
        value = state,
        onValueChange = { viewModel.updateState(it) },
        label = { Text("State") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
          focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
          focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )
      OutlinedTextField(
        value = country,
        onValueChange = { viewModel.updateCountry(it) },
        label = { Text("Country") },
        modifier = Modifier.fillMaxWidth(),
        colors = TextFieldDefaults.outlinedTextFieldColors(
          focusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
          focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
      )

      Spacer(modifier = Modifier.height(16.dp))

      Button(
        colors = ButtonDefaults.buttonColors(containerColor = LightBlue),
        onClick = {
          if (paymentReady == PaymentReadyStatus.IDLE || paymentReady == PaymentReadyStatus.FAIL) {
            if (detailsInputValidation(viewModel))
              onConfirm()
            else
              Toast.makeText(context, "Please fill details", Toast.LENGTH_SHORT).show()
          }
        },
        modifier = Modifier.fillMaxWidth()
      ) {
        if (paymentReady == PaymentReadyStatus.IDLE)
          Text("Confirm")

        if (paymentReady == PaymentReadyStatus.LOADING)
          CircularProgressIndicator(
            modifier = Modifier.size(24.dp),
            color = Color.White,
            strokeWidth = 2.dp
          )
        if (paymentReady == PaymentReadyStatus.FAIL)
          Text("Something went wrong, try again")
      }
    }
  }
}

fun detailsInputValidation(viewModel: CapsuleCreationViewModel): Boolean {
  val name = viewModel.name.value
  val address = viewModel.address.value
  val postalCode = viewModel.postalCode.value
  val city = viewModel.city.value
  val state = viewModel.state.value
  val country = viewModel.country.value
  if (name.isEmpty() || address.isEmpty() || postalCode.isEmpty() || city.isEmpty() || state.isEmpty() || country.isEmpty()) {
    return false
  }
  return true
}

private fun presentPaymentSheet(
  paymentSheet: PaymentSheet,
  customerConfig: PaymentSheet.CustomerConfiguration,
  paymentIntentClientSecret: String
) {
  paymentSheet.presentWithPaymentIntent(
    paymentIntentClientSecret,
    PaymentSheet.Configuration(
      merchantDisplayName = "Time Capsule Android",
      customer = customerConfig,
    )
  )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ReviewScreen(
  viewModel: CapsuleCreationViewModel = hiltViewModel(),
  onNavigate: (NavigationAddCapsule) -> Unit = {}
) {
  val isTablet = DeviceType.isTablet()
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
  val bottomScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

  var paymentReady by remember {
    mutableStateOf(PaymentReadyStatus.IDLE)
  }

  var showDetails by remember {
    mutableStateOf(false)
  }

  fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
    when (paymentSheetResult) {
      is PaymentSheetResult.Canceled -> {
        paymentReady = PaymentReadyStatus.FAIL

      }

      is PaymentSheetResult.Failed -> {
        paymentReady = PaymentReadyStatus.FAIL
      }

      is PaymentSheetResult.Completed -> {
        onNavigate(NavigationAddCapsule.NEXT)
        paymentReady = PaymentReadyStatus.READY
      }
    }
  }

  val paymentSheet = rememberPaymentSheet(::onPaymentSheetResult)


  val amount = 500
  var customerConfig by remember { mutableStateOf<PaymentSheet.CustomerConfiguration?>(null) }
  var paymentIntentClientSecret by remember { mutableStateOf<String?>(null) }
  val context = LocalContext.current


  fun PaymentConfiguration(viewModel: CapsuleCreationViewModel) {

    val jsonBody = """
      {
        "amount": ${viewModel.amount * 100},  
        "name": "${viewModel.name.value}",  
        "address": {
          "line1": "${viewModel.address.value}",  
          "postal_code": "${viewModel.postalCode.value}",  
          "city": "${viewModel.city.value}",  
          "state": "${viewModel.state.value}",  
          "country": "${viewModel.country.value}" 
        }
      }
      """

    BuildConfig.STRIPE_SERVER_URL.httpPost()
      .header("Content-Type", "application/json").body(jsonBody).responseJson { _, _, result ->
        if (result is Result.Success) {
          val responseJson = result.get().obj()
          paymentIntentClientSecret = responseJson.getString("paymentIntent")
          customerConfig = PaymentSheet.CustomerConfiguration(
            id = responseJson.getString("customer"),
            ephemeralKeySecret = responseJson.getString("ephemeralKey")
          )
          val publishableKey = responseJson.getString("publishableKey")
          PaymentConfiguration.init(context, publishableKey)

          if (customerConfig != null && paymentIntentClientSecret != null) {
            presentPaymentSheet(paymentSheet, customerConfig!!, paymentIntentClientSecret!!)
          }
        } else {
          paymentReady = PaymentReadyStatus.FAIL
        }
      }
  }

  if (showDetails) {
    CustomerDetailsBottomSheet(
      viewModel,
      paymentReady = paymentReady,
      changeDialog = { showDetails = false }) {
      paymentReady = PaymentReadyStatus.LOADING
      PaymentConfiguration(viewModel = viewModel)
    }
  }

  Scaffold(
    modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primary),
    containerColor = MaterialTheme.colorScheme.primary,
    bottomBar = {
      BottomAppBar(
        containerColor = Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color.Transparent),
        content = {
          BottomRow(onNavigate) {
            showDetails = true
          }
        },
      )
    },
    topBar = {
      TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = {
          Text(
            modifier = Modifier.padding(vertical = 40.dp),
            text = stringResource(id = R.string.review_your_details),
            style = MaterialTheme.typography.titleLarge.copy(
              fontSize = if (isTablet) 26.sp else 20.sp,
              fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        },
        scrollBehavior = scrollBehavior
      )
    }
  ) { innerPadding ->

    LazyColumn(
      modifier = Modifier
          .padding(
              start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
              end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
              top = innerPadding.calculateTopPadding()
          )
          .padding(horizontal = 10.dp)
          .nestedScroll(scrollBehavior.nestedScrollConnection)
          .nestedScroll(bottomScrollBehavior.nestedScrollConnection),
    ) {
      item { SharedPeople() }
      item { DateAndTime() }
      item { SelectedCapsule() }
      item { SharedContent() }
    }
  }
}

@Composable
fun SharedPeople() {
  Column(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 10.dp)
  ) {
    Text(
      text = stringResource(id = R.string.shared_with),
      style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    ShowSelectedPeople(disableCrossBtn = true)
  }
}

@Composable
fun DateAndTime() {
  Column(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 10.dp)
  ) {
    Text(
      text = stringResource(id = R.string.date_and_time),
      style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
  ) {
    Text(
      modifier = Modifier.padding(10.dp),
      text = stringResource(id = R.string.date),
      style = MaterialTheme.typography.titleLarge.copy(
        fontSize = 25.sp,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = RubikBubble
      ),
      color = ReviewScreenCommondColor
    )
    Text(
      modifier = Modifier.padding(10.dp),
      text = stringResource(id = R.string.time),
      style = MaterialTheme.typography.titleLarge.copy(
        fontSize = 25.sp,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = RubikBubble
      ),
      color = ReviewScreenCommondColor
    )
  }
}

@Composable
fun SelectedCapsule() {
  Column(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 10.dp)
  ) {
    Text(
      text = stringResource(id = R.string.selected_capsule),
      style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Box(
        Modifier
            .height(200.dp)
            .width(200.dp)
            .align(Alignment.CenterHorizontally)
    ) {
      Image(
        painter = painterResource(id = R.drawable.testimg),
        contentDescription = stringResource(id = R.string.selected_capsule)
      )
    }
  }
}

@Composable
fun SharedContent() {
  Column(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(vertical = 10.dp)
  ) {
    Text(
      modifier = Modifier.padding(bottom = 10.dp),
      text = stringResource(id = R.string.shared_content),
      style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )

    UploadedFileItem(
      title = "Lorem ipsum",
      fileSize = "21.9 MB",
      icon = R.drawable.pdf,
      disableDeleteBtn = true
    )
    UploadedFileItem(
      title = "ispum ipsum",
      fileSize = "11.9 MB",
      icon = R.drawable.image,
      disableDeleteBtn = true
    )
    UploadedFileItem(
      title = "itahi emuah",
      fileSize = "51.9 MB",
      icon = R.drawable.videocamera,
      disableDeleteBtn = true
    )
    UploadedFileItem(
      title = "Lorem ipsum",
      fileSize = "21.9 MB",
      icon = R.drawable.pdf,
      disableDeleteBtn = true
    )
    UploadedFileItem(
      title = "poio ipsum",
      fileSize = "21.9 MB",
      icon = R.drawable.xls,
      disableDeleteBtn = true
    )
    UploadedFileItem(
      title = "Lorem ipsum",
      fileSize = "21.9 MB",
      icon = R.drawable.pdf,
      disableDeleteBtn = true
    )
  }
}

@Composable
fun BottomRow(onNavigate: (NavigationAddCapsule) -> Unit = {}, onClick: () -> Unit) {
  Row(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(Color.Transparent)
        .padding(horizontal = 30.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.Top
  ) {
    OutlinedButton(
      onClick = { onNavigate(NavigationAddCapsule.BACK) },
      shape = RoundedCornerShape(10.dp),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
      modifier = Modifier
          .wrapContentWidth()
          .height(46.dp),
      colors = ButtonDefaults.buttonColors(containerColor = ReviewScreenCommondColor.copy(alpha = 0.6F))

    ) {
      Text(
        text = stringResource(id = R.string.edit_button),
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp, color = Color.White),
      )
    }

    Button(
      onClick = {
        onClick()
//        onNavigate(NavigationAddCapsule.NEXT)
      },
      border = BorderStroke(1.dp, Color.Black),
      modifier = Modifier
          .wrapContentWidth()
          .height(46.dp),
      shape = RoundedCornerShape(10.dp),
      colors = ButtonDefaults.buttonColors(containerColor = ReviewScreenCommondColor)
    ) {
      Text(
        text = stringResource(id = R.string.done_button),
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp, color = Color.White),
        modifier = Modifier.background(ReviewScreenCommondColor)
      )
    }
  }
}
