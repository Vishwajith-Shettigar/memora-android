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
fun TermsAndServiceScreen(onBackClick: () -> Unit) {
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
          text = "Terms & Services",
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
                getTermsAndServicesHtml(),
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


fun getTermsAndServicesHtml(): String {
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
                    font-size: 24px;
                }
                h2 {
                    color: #01579b;
                    font-size: 20px;
                    margin-top: 20px;
                }
                p {
                    margin-bottom: 12px;
                }
                ul {
                    margin: 10px 0;
                    padding-left: 20px;
                }
                ul li {
                    margin-bottom: 6px;
                }
            </style>
        </head>
        <body>
         
            <p>By downloading, installing, or using the <strong>Time Capsule</strong> app ("the App"), you agree to be bound by the following Terms and Conditions. These terms are subject to change, and we recommend that you review them periodically.</p>

            <h2>1. Purpose of the App</h2>
            <p>The <strong>Time Capsule</strong> app is a mobile application intended for users to store digital "time capsules" that they can access in the future. However, please be aware that this app is currently in testing, and its functionality may change or be discontinued at any time.</p>

            <h2>2. Testing Phase Disclaimer</h2>
            <p>As the <strong>Time Capsule</strong> app is still in the testing phase, we do not guarantee that the service will remain operational, or that time capsules will be preserved. The app may undergo modifications, and your data could be deleted permanently without warning.</p>
            <p>By using this app, you acknowledge and accept that:</p>
            <ul>
                <li>Your time capsules (data) may be deleted at any time, even during the testing phase.</li>
                <li>We do not promise long-term availability of the service or your data.</li>
            </ul>

            <h2>3. User Account and Access</h2>
            <p>If the app requires you to create an account:</p>
            <ul>
                <li>You agree to provide accurate and up-to-date information.</li>
                <li>You are responsible for maintaining the confidentiality of your account credentials and ensuring that your account is not accessed by unauthorized persons.</li>
            </ul>

            <h2>4. No Liability for Service Interruptions</h2>
            <p>We are not responsible for any data loss, service interruptions, or errors that occur while using the app. Since the app is in a testing phase, there may be unforeseen bugs or failures. We are under no obligation to fix these issues, though we will strive to improve the service.</p>

            <h2>5. Restrictions</h2>
            <p>You may not:</p>
            <ul>
                <li>Reverse engineer, decompile, or otherwise attempt to extract the source code of the app.</li>
                <li>Use the app for unlawful purposes or in a manner that violates applicable laws.</li>
            </ul>

            <h2>6. Modifications and Termination</h2>
            <p>We reserve the right to modify, suspend, or discontinue the app at any time, with or without notice. This includes the potential deletion of user data or the cessation of service altogether.</p>

            <h2>7. No Guarantees</h2>
            <p>While we aim to offer a functional and useful app, there are no guarantees regarding the performance, longevity, or data storage capabilities of the <strong>Time Capsule</strong> app. It is possible that the app will be shut down at any point, and your data may be permanently lost.</p>

            <h2>8. Limitation of Liability</h2>
            <p>We are not liable for any direct, indirect, incidental, special, or consequential damages arising out of your use or inability to use the app, including but not limited to any loss of data, lost profits, or damage to your device.</p>

            <h2>9. Changes to the Terms and Conditions</h2>
            <p>We may update these Terms and Conditions at any time. If we make significant changes, we will notify users via the app or email (if applicable). By continuing to use the app after updates are made, you accept the new terms.</p>

            <h2>10. Governing Law</h2>
            <p>These Terms and Conditions shall be governed by and construed in accordance with the laws of [Your Country/State]. Any disputes will be subject to the jurisdiction of the courts in [Your Location].</p>

            <h2>11. Contact Us</h2>
            <p>If you have any questions about these Terms and Conditions, please contact us at:</p>
            <ul>
                <li>Email: timecapsuleshelp@gmail.com</li>
            </ul>
        </body>
        </html>
    """.trimIndent()
}