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
import androidx.compose.ui.unit.dp
import com.rach.habitchange.presentations.AccessibilityUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessibilityPermissionScreen(
    onPermissionEnabled: () -> Unit
) {
    val context = LocalContext.current
    Log.d("PermissionUi","entered on the screen")
    val serviceName =
        "${context.packageName}/com.rach.habitchange.presentations.accessibility.AppUsageAccessibilityService"

    var isEnabled by remember {

        mutableStateOf(
            AccessibilityUtil.isServiceEnabled(context, serviceName)
        )
    }
Log.d("PermissionUi","entered remember")
    LaunchedEffect(Unit) {
        isEnabled = AccessibilityUtil.isServiceEnabled(context, serviceName)
    }

    if (isEnabled) {
        onPermissionEnabled()
        return
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
                "To track usage limits correctly, you must enable Accessibility Monitoring.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    context.startActivity(intent)
                    Toast.makeText(
                        context,
                        "Find HabitChange → Enable Accessibility Service",
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enable Service")
            }
        }
    }
}
