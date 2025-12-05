package com.rach.habitchange.presentations.ui.appUsageDetailScreen

import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.rach.habitchange.AppPreview
import com.rach.habitchange.R
import com.rach.habitchange.presentations.ui.appUsageDetailScreen.components.CircularRoundedImage
import com.rach.habitchange.presentations.ui.appUsageDetailScreen.components.FiveDaysDataUiSection
import com.rach.habitchange.presentations.ui.appUsageDetailScreen.components.TodayUsageTextAppDetailsScreen
import com.rach.habitchange.presentations.uiComponents.CustomTopAppBar
import com.rach.habitchange.presentations.uiComponents.timeSelect.BaseDurationPicker
import com.rach.habitchange.presentations.uiComponents.timeSelect.TimeUtil
import com.rach.habitchange.presentations.viewModel.HomeViewModel
import com.rach.habitchange.theme.HabitChangeTheme


private const val MinimumTime = (1 * 60) // 1min
private const val MaximumTime = (22 * 60 * 60) + (45 * 60) // 22 hour 45 min max
private const val currentTime = "2:30:00"


@Composable
fun AppUsageDetailScreen(
    onBackClick: () -> Unit,
    packageName: String,
    appName: String,
    todayUsage: Long,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    var showTimerPicker by remember { mutableStateOf(false) }

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

//            TimePickerDialog(
//                showBottomSheet = showTimerPicker,
//                onDismissRequest = { showTimerPicker = false },
//                onConfirmRequest = { }
//            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {

    if (showBottomSheet) {
        ModalBottomSheet(
            modifier = modifier,
            onDismissRequest = onDismissRequest
        ) {

            BaseDurationPicker(
                modifier = Modifier.fillMaxWidth(),
                current = TimeUtil.convertTimeToDuration(currentTime),
                minimumSeconds = MinimumTime,
                maximumSeconds = MaximumTime,
                onConfirmClick = { timeInStrings ->
                    val timeInSeconds = convertTimeStringToSeconds(timeInStrings)
                    onConfirmRequest()
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


@AppPreview
@Composable
private fun Preview() {
    HabitChangeTheme {
        AppUsageDetailScreen(
            onBackClick = {},
            packageName = "ji",
            appName = "Telegram",
            todayUsage = 1000
        )
    }
}