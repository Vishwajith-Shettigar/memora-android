package com.example.util

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import kotlin.random.Random

fun bytesToMegabytes(bytes: Long): Double {
  return bytes / (1024.0 * 1024.0) // Divide by 1024 twice
}

// Function to get the size of the file
 fun getFileSizeAndName(fileUri: Uri,context:Context): Pair<Long, String> {

  var size: Long = 0
  var name: String = Random.nextLong(0, 100000).toString() + "xp1233op"
  // Get the file size using ContentResolver
  context.contentResolver.query(fileUri, null, null, null, null)?.use { cursor ->
    if (cursor.moveToFirst()) {
      val sizeColumnIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
      val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
      if (sizeColumnIndex != -1) {
        size = cursor.getLong(sizeColumnIndex)
      } else {
        Log.e("File Size", "Column index for size not found.")
      }
      if (nameIndex != -1) {
        name = cursor.getString(nameIndex)
      }
    }
  }
  return Pair(size, name)
}