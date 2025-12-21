package com.rach.habitchange.presentations.ui.appUsageDetailScreen

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rach.habitchange.AppPreview
import com.rach.habitchange.R
import com.rach.habitchange.presentations.AccessibilityUtil
import com.rach.habitchange.presentations.navigation.Screens
import com.rach.habitchange.presentations.ui.appUsageDetailScreen.components.CircularRoundedImage
import com.rach.habitchange.presentations.ui.appUsageDetailScreen.components.FiveDaysDataUiSection
import com.rach.habitchange.presentations.ui.appUsageDetailScreen.components.TodayUsageTextAppDetailsScreen
import com.rach.habitchange.presentations.uiComponents.CustomTopAppBar
import com.rach.habitchange.presentations.uiComponents.timeSelect.BaseDurationPicker
import com.rach.habitchange.presentations.uiComponents.timeSelect.TimeUtil
import com.rach.habitchange.presentations.viewModel.HomeViewModel
import com.rach.habitchange.presentations.workmanager.roomsetup.vm.WorkerViewModel
import com.rach.habitchange.presentations.workmanager.roomsetup.worker.CheckUsageWorker
import com.rach.habitchange.theme.HabitChangeTheme


private const val MinimumTime = (1 * 60) // 1min
private const val MaximumTime = (22 * 60 * 60) + (45 * 60) // 22 hour 45 min max
private const val currentTime = "1:10:00"


@Composable
fun AppUsageDetailScreen(
    navController: NavController,

    onBackClick: () -> Unit,
    packageName: String,
    appName: String,
    todayUsage: Long,
    homeViewModel: HomeViewModel = hiltViewModel(),
    workerViewModel: WorkerViewModel = hiltViewModel()

) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current
var diologuebox by remember { mutableStateOf(false) }
var showTimerPicker by remember { mutableStateOf(false) }
    Log.d("Ui","entered on the screen")

    LaunchedEffect(Unit) {
        homeViewModel.fiveDayDataUsage(packageName)
    }

    val uiState by homeViewModel.uiState.collectAsState()

    val appImage = remember(packageName) {
        try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: Exception) {
            e.localizedMessage?.let { Log.d("error", it) }
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = "Usage Data",
                onNavigationIconClick = {
                    onBackClick()
                }
            )
        }
    ) { paddingValues ->
        if (uiState.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.normal_padding))
                    .padding(paddingValues)
                    .verticalScroll(scrollState)
            ) {
                if (appImage != null) {
                    CircularRoundedImage(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.CenterHorizontally),
                        radius = 120.dp,
                        appIcon = appImage
                    )
                }
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_8dp)))
                Log.d("ui", "app name shown")
                Text(
                    text = appName, // app Name
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_20dp)))

                //Today App Usage Composable
                TodayUsageTextAppDetailsScreen(
                    modifier = Modifier.fillMaxWidth(),
                    todayUsage = todayUsage,
                    onSetLimitClick = {
                        showTimerPicker = true
                    }
                )

                //Five Days Section Or In Future You need to implement Graph
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_15dp)))
                FiveDaysDataUiSection(
                    modifier = Modifier,
                    fiveDaysAppUsageData = uiState.appsData,
                    todayUsage
                )
            }
            TimePickerDialog(
                showBottomSheet = showTimerPicker,
                onDismissRequest = { showTimerPicker = false },

                onConfirmRequest = { timeInSeconds: Long ->

                    Log.d("UI", "User pressed confirm")




                    val serviceName =
                        "${context.packageName}/com.rach.habitchange.presentations.accessibility.AppUsageAccessibilityService"

                    // 🔍 Check accessibility permission
                    if (!AccessibilityUtil.isServiceEnabled(context, serviceName)) {

                        Log.d("UI", "Accessibility not enabled")

                        Toast.makeText(
                            context,
                            "Enable Accessibility permission to track app usage",
                            Toast.LENGTH_LONG
                        ).show()

                        diologuebox = true


                        return@TimePickerDialog
                    }

                    // ✅ Permission granted → proceed
                    Log.d("UI", "Accessibility enabled")

                    workerViewModel.saveLimit(packageName, timeInSeconds)
                    Log.d("UI", "Limit saved → $packageName = $timeInSeconds seconds")

                    Toast.makeText(
                        context,
                        "Limit set successfully!",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("UI", "Toast shown")



                    showTimerPicker = false
                    Log.d("UI", "Timer picker closed")
                }
            )

            if (diologuebox) {
                AccessibilityInfoDialog(
                    onDismiss = {
                        diologuebox = false
                    },
                    onDoneClick = {
                        diologuebox = false
                        context.startActivity(
                            Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        )
                    }
                )
            }



        }
    }

}

@Composable
fun AccessibilityInfoDialog(
    onDismiss: () -> Unit,
    onDoneClick: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Enable Accessibility")
        },
        text = {
            Column {
                Text(
                    text = "Why this permission is needed:",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text =
                        "Habitime uses Accessibility Service only to detect the currently active app " +
                                "and calculate usage time.\n\n" +

                                "We do not read messages, passwords, or personal data. " +
                                "All data stays on your device.",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Steps to enable:",
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text =
                        "1. Tap Allow\n" +
                                "2. Open Downloaded apps\n" +
                                "3. Select Habitime: Screen Time Tracker\n" +
                                "4. Toggle Accessibility ON",
                    style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDoneClick) {
                Text("Allow")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


fun isNotificationEnabled(context: Context): Boolean {
    return NotificationManagerCompat.from(context).areNotificationsEnabled()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (Long) -> Unit
) {

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissRequest
        ) {
            Log.d("UI", "Modal Bottom Sheet")

            BaseDurationPicker(
                modifier = Modifier.fillMaxWidth(),
                current = TimeUtil.convertTimeToDuration(currentTime),
                minimumSeconds = MinimumTime,
                maximumSeconds = MaximumTime,
                onConfirmClick = { timeInStrings: String ->
                    val timeInSeconds = convertTimeStringToSeconds(timeInStrings)
                    onConfirmRequest(timeInSeconds)
                }
            )
        }
    }
}

private fun convertTimeStringToSeconds(timeString: String): Long {
    val parts = timeString.split(":").map { it.toIntOrNull() ?: 0 }
    val (hours, minutes, seconds) = parts + List(3 - parts.size) { 0 }
    return (hours * 3600 + minutes * 60 + seconds).toLong()
}


