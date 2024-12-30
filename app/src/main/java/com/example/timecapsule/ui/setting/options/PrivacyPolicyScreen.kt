package com.example.timecapsule.ui.setting.options

import android.webkit.WebView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.timecapsule.ui.selecttime.BackRow
import com.example.timecapsule.ui.theme.LightBlue
import com.example.timecapsule.ui.util.DeviceType

@Composable
fun PrivacyPolicyScreen(onBackClick: () -> Unit) {
  val isTablet = DeviceType.isTablet()
  LazyColumn(
    modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colorScheme.primary).systemBarsPadding()
        .padding(top = 16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {

    item {
      Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(top = 20.dp),
        verticalArrangement = Arrangement.Top
      ) {
        BackRow {
          onBackClick()
        }
      }
    }

    item {
      Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(10.dp),
        horizontalAlignment =
        if (isTablet) Alignment.CenterHorizontally else Alignment.Start
      ) {
        Text(
          modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
          text = "Privacy Policy",
          style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
          ),
          color = LightBlue
        )

      }
    }
    // Add a scrollable view for the Privacy Policy text

    item {
      Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 10.dp)
            .clip(
                RoundedCornerShape(20.dp)
            )
            .background(MaterialTheme.colorScheme.onSecondaryContainer)
            .border(
                2.dp,
                color = LightBlue.copy(alpha = 0.4F),
                shape = RoundedCornerShape(20.dp)
            )
      ) {

        AndroidView(
          modifier = Modifier.fillMaxSize(),
          factory = { context ->
            WebView(context).apply {
              loadDataWithBaseURL(
                null,
                getPrivacyPolicyHtml(),
                "text/html",
                "utf-8",
                null
              )
            }
          }
        )
      }
    }

  }
}


fun getPrivacyPolicyHtml(): String {
  return """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
                body {
                    font-family: Arial, sans-serif;
                    line-height: 1.6;
                    margin: 16px;
                    color: #333;
                }
                h1 {
                    color: #0277bd;
                }
                h2 {
                    color: #01579b;
                }
            </style>
        </head>
        <body>
            <p>This Privacy Policy explains how your personal data is handled by <b>memora</b> ("we", "us", "our"), an Android app currently in testing. By using this app, you consent to the collection, use, and storage of your information as described below.</p>
            
            <h2>1. Information We Collect</h2>
            <ul>
                <li><b>Personal Information:</b> If you provide it voluntarily (e.g., email address, user name).</li>
                <li><b>Usage Data:</b> This includes information about how you use the app, including your interactions, device type, and general usage patterns.</li>
            </ul>
            <p>We do not collect sensitive personal information such as financial data, health information, or social security numbers.</p>
            
            <h2>2. Use of Information</h2>
            <p>The data we collect is used for the purpose of improving the app, troubleshooting issues, and enhancing the user experience. We may also use your data for internal analysis to improve the service.</p>
            
            <h2>3. Data Storage and Retention</h2>
            <p>Please note that <b>memora</b> is currently in a testing phase and does not have guaranteed long-term storage. Your time capsules (data) may be deleted or become inaccessible at any time without notice. We do not guarantee the preservation or accessibility of your data beyond the testing period.</p>
            
            <h2>4. Third-Party Services</h2>
            <p>We do not currently use third-party services for data collection, advertising, or analytics. However, this may change in the future, and if it does, we will update this Privacy Policy accordingly.</p>
            
            <h2>5. Data Security</h2>
            <p>While we strive to protect your information, please note that, as an app in development, we cannot guarantee absolute security. Use the app at your own risk.</p>
            
            <h2>6. Changes to the Privacy Policy</h2>
            <p>We may update this Privacy Policy from time to time. When we do, we will post the revised version within the app with the updated date. We encourage you to review this Privacy Policy periodically to stay informed.</p>
            
            <h2>7. Contact Us</h2>
            <p>If you have any questions or concerns about this Privacy Policy, please contact us at:</p>
            <ul>
                <li>Email: memorabeta@gmail.com</li>
            </ul>
        </body>
        </html>
    """.trimIndent()
}