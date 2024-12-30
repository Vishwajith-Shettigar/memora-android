package com.example.timecapsule

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.timecapsule.ui.theme.LightBlue

class PermissionManager(private val context: Context) {
  private val sharedPreferences: SharedPreferences =
    context.getSharedPreferences("PermissionPrefs", Context.MODE_PRIVATE)
  private val editor: SharedPreferences.Editor = sharedPreferences.edit()

  private val locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
  private val notificationPermission = android.Manifest.permission.POST_NOTIFICATIONS

  // Check if permissions are granted
  fun arePermissionsGranted(): Boolean {
    val locationGranted = ContextCompat.checkSelfPermission(
      context,
      locationPermission
    ) == PackageManager.PERMISSION_GRANTED
    val notificationGranted = ContextCompat.checkSelfPermission(
      context,
      notificationPermission
    ) == PackageManager.PERMISSION_GRANTED
    return locationGranted && notificationGranted
  }

  // Check if the user has previously denied the permissions and selected "Don't Ask Again"
  fun shouldAskForPermissions(): Boolean {
    return !sharedPreferences.getBoolean("permission_denied", false)
  }

  // Mark that the user denied permissions with "Don't Ask Again"
  fun markPermissionDenied() {
    editor.putBoolean("permission_denied", true)
    editor.apply()
  }
}

@Composable
fun PermissionHandler(
  onPermissionsGranted: () -> Unit,
  onPermissionsDenied: () -> Unit
) {
  val context = androidx.compose.ui.platform.LocalContext.current
  val permissionManager = remember { PermissionManager(context) }
  var showSettingsDialog by remember { mutableStateOf(false) }

  val permissionsLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    val deniedPermissions = permissions.filterValues { !it }.keys.toList()

    if (deniedPermissions.isEmpty()) {
      // All permissions granted
      onPermissionsGranted()
    } else {
      showSettingsDialog = true
      // At least one permission denied
      onPermissionsDenied()
    }
  }

  // Handle permission request logic
  LaunchedEffect(permissionManager.shouldAskForPermissions()) {
    if (!permissionManager.arePermissionsGranted() && permissionManager.shouldAskForPermissions()) {
      permissionsLauncher.launch(
        arrayOf(
          android.Manifest.permission.ACCESS_FINE_LOCATION,
          android.Manifest.permission.POST_NOTIFICATIONS
        )
      )
    }
  }

  // Show settings dialog if permissions are denied
  if (showSettingsDialog) {
    PermissionSettingsDialog(
      onDismiss = { showSettingsDialog = false },
      onOpenSettings = { openAppSettings(context) },
      onNeverAskAgain = {
        permissionManager.markPermissionDenied()
        showSettingsDialog = false
      }
    )
  }
}

@Composable
fun PermissionSettingsDialog(
  onDismiss: () -> Unit,
  onOpenSettings: () -> Unit,
  onNeverAskAgain: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      shape = MaterialTheme.shapes.extraLarge,
      tonalElevation = 4.dp,
      color = MaterialTheme.colorScheme.primary
    ) {
      Column(
        modifier = Modifier
          .padding(16.dp)
          .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // Title
        Text(
          text = "Permission Required",
          style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 20.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )

        // Explanation Text
        Text(
          text = "Some features requires location and notification permissions to function properly.",
          style = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        )

        // Open settings button
        Button(
          onClick = {
            onOpenSettings()
            onDismiss()
          },
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(containerColor = LightBlue)
        ) {
          Text(
            text = "Open Settings",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }

        // Don't ask again button
        TextButton(onClick = {
          onNeverAskAgain()
        }, modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "Don't Ask Again",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }

        // Cancel button
        TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
          Text(
            text = "Cancel",
            style = MaterialTheme.typography.bodyMedium.copy(
              fontSize = 14.sp,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          )
        }
      }
    }
  }
}

fun openAppSettings(context: Context) {
  val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
    data = android.net.Uri.fromParts("package", context.packageName, null)
  }
  context.startActivity(intent)
}
