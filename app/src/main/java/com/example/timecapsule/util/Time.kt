package com.example.timecapsule.util

import androidx.compose.runtime.Composable
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DisplayTimestamp(timestamp: Timestamp) :String{
  val date = timestamp.toDate()

  val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

  val formattedDate = formatter.format(date)

return formattedDate
}
