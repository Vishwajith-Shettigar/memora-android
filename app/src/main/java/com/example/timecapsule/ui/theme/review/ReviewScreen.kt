package com.example.timecapsule.ui.theme.review

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timecapsule.R
import com.example.timecapsule.ui.theme.ReviewScreenCommondColor
import com.example.timecapsule.ui.theme.RubikBubble
import com.example.timecapsule.ui.theme.sharewithpeople.ShowSelectedPeople
import com.example.timecapsule.ui.theme.uploadfiles.UploadedFileItem
import com.example.timecapsule.ui.theme.util.DeviceType

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ReviewScreen() {
  val isTablet = DeviceType.isTablet()
  val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
  val bottomScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior()

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
        content = { BottomRow() },
        scrollBehavior = bottomScrollBehavior
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
        .padding(innerPadding)
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

    UploadedFileItem(title = "Lorem ipsum", "21.9 MB", R.drawable.pdf, true)
    UploadedFileItem(title = "ispum ipsum", "11.9 MB", R.drawable.image, true)
    UploadedFileItem(title = "itahi emuah", "51.9 MB", R.drawable.videocamera, true)
    UploadedFileItem(title = "Lorem ipsum", "21.9 MB", R.drawable.pdf, true)
    UploadedFileItem(title = "poio ipsum", "21.9 MB", R.drawable.xls, true)
    UploadedFileItem(title = "Lorem ipsum", "21.9 MB", R.drawable.pdf, true)
  }
}

@Composable
fun BottomRow() {
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
      onClick = { },
      shape = RoundedCornerShape(10.dp),
      border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurfaceVariant),
      modifier = Modifier
        .wrapContentWidth()
        .height(46.dp)
    ) {
      Text(
        text = stringResource(id = R.string.edit_button),
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 15.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    Button(
      onClick = { },
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
