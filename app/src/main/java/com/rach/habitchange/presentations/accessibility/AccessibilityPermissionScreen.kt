package com.rach.habitchange.presentations.ui.enableAccessibility

import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rach.habitchange.presentations.AccessibilityUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilityPermissionScreen(
    onPermissionEnabled: () -> Unit
) {
    val context = LocalContext.current

    val serviceName =
        "${context.packageName}/com.rach.habitchange.presentations.accessibility.AppUsageAccessibilityService"

    var isEnabled by remember {
        mutableStateOf(
            AccessibilityUtil.isServiceEnabled(context, serviceName)
        )
    }

    // Re-check when coming back from settings
    LaunchedEffect(Unit) {
        while (!isEnabled) {
            isEnabled = AccessibilityUtil.isServiceEnabled(context, serviceName)
            kotlinx.coroutines.delay(500)
        }
        onPermissionEnabled()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Enable Usage Monitoring") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "To track app usage limits, please enable Accessibility Service.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    context.startActivity(
                        Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    )
                }
            ) {
                Text("Enable Service")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun showseeting(){
   AccessibilityPermissionScreen {  }
}