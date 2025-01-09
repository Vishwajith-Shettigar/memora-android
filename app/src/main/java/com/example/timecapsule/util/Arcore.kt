package com.example.timecapsule.util

import android.content.Context
import android.widget.Toast
import com.google.ar.core.ArCoreApk

fun checkARCoreAvailability(context: Context): Boolean {
  val availability = ArCoreApk.getInstance().checkAvailability(context)
  when (availability) {
    ArCoreApk.Availability.SUPPORTED_INSTALLED -> {
      // Device supports ARCore and has it installed
      return true
    }

    ArCoreApk.Availability.SUPPORTED_NOT_INSTALLED -> {
      // ARCore is supported, but not installed
      Toast.makeText(
        context,
        "ARCore is supported, but not installed. Please install ARCore.",
        Toast.LENGTH_LONG
      ).show()

    }

    ArCoreApk.Availability.UNKNOWN_ERROR -> {
      // Unknown error, we can't determine if ARCore is supported
      Toast.makeText(context, "Unable to determine ARCore support.", Toast.LENGTH_SHORT).show()

    }

    ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE -> {
      // The device doesn't support ARCore
      Toast.makeText(context, "ARCore is not supported on this device.", Toast.LENGTH_LONG).show()

    }

    ArCoreApk.Availability.UNKNOWN_CHECKING -> {
      Toast.makeText(context, "ARCore is not supported on this device.", Toast.LENGTH_LONG).show()

    }

    ArCoreApk.Availability.UNKNOWN_TIMED_OUT -> {
      Toast.makeText(context, "ARCore is not supported on this device.", Toast.LENGTH_LONG).show()

    }

    ArCoreApk.Availability.SUPPORTED_APK_TOO_OLD -> {
      Toast.makeText(context, "ARCore is not supported on this device.", Toast.LENGTH_LONG).show()

    }
  }

  return false
}
