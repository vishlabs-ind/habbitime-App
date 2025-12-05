package com.rach.habitchange.presentations.ui.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import android.graphics.Paint

import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.atan2
import kotlin.math.hypot
import android.graphics.Paint as AndroidPaint

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rach.habitchange.R
import com.rach.habitchange.presentations.model.LoadAppDataWithUsage
import com.rach.habitchange.presentations.ui.NoDataFound
import com.rach.habitchange.presentations.ui.homescreen.components.HomeAppItem
import com.rach.habitchange.presentations.uiComponents.CustomTopAppBar
import com.rach.habitchange.presentations.uiComponents.DrawerContent
import com.rach.habitchange.presentations.uiComponents.NavigationItem
import com.rach.habitchange.presentations.uiComponents.PermissionDialog
import com.rach.habitchange.presentations.viewModel.HomeViewModel
import com.rach.habitchange.theme.onPrimaryContainerLight

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel(),
    onFloatingButtonClicked: () -> Unit,
    onAppClick: (packageName: String, appName: String, todayUsage: Long) -> Unit
) {

    val context = LocalContext.current
    val uiState by homeViewModel.uiState.collectAsState()
    val showPermissionDialog by homeViewModel.showPermissionDialog.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            homeViewModel.clearErrorMessage()
        }
    }


    LaunchedEffect(Unit) {
        homeViewModel.loadTodayUsageData()
    }



    AnimatedVisibility(
        visible = showPermissionDialog,
        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
            animationSpec = tween(300),
            initialOffsetY = { it / 2 }
        ),
        exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(
            animationSpec = tween(300),
            targetOffsetY = { it / 2 }
        )
    ) {
        PermissionDialog(
            onDismissRequest = { homeViewModel.dismissDialog() },
            onConfirmButton = {
                homeViewModel.goToSettingsForRequestUsageStatsPermission()
            },
            title = stringResource(R.string.usage_access_required),
            text = stringResource(R.string.stats_perm_text)
        )
    }
    var selectedItem by remember { mutableStateOf("home") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navigationItems = remember {
        listOf(
            NavigationItem("home", "Home", Icons.Default.Home),
            NavigationItem("profile", "Profile", Icons.Default.Person),
            NavigationItem("messages", "Messages", Icons.Default.Email),
            NavigationItem("notifications", "Notifications", Icons.Default.Notifications),
            NavigationItem("settings", "Settings", Icons.Default.Settings)
        )
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.width(280.dp)
            ) {
                DrawerContent(
                    navigationItems = navigationItems,
                    selectedItem = selectedItem,
                    onItemClick = { itemId ->
                        selectedItem = itemId
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                CustomTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Home",
                    onNavigationIconClick = {
                        scope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            } else {
                                drawerState.open()
                            }
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButtonUi(onFloatingButtonClicked, showPermissionDialog)
            },
            floatingActionButtonPosition = FabPosition.Center,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) { paddingValues ->
            Box(
                modifier = modifier
                    .padding(paddingValues)

            ) {

                when {
                    uiState.loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

            when {
                uiState.loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.appsData.isEmpty() -> {
                    NoDataFound(
                        modifier = Modifier.fillMaxSize(),
                        text = "No App Found ",
                        text2 = "Please Add Apps"
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {

                        val totalApps = uiState.appsData.size

                        if (totalApps > 4) {
                            CircularGraph(uiState.appsData)

                            Spacer(modifier = Modifier.height(16.dp))  // space between graph & list
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(uiState.appsData) {
                                HomeAppItem(
                                    appName = it.name,
                                    packageName = it.packageName,
                                    rank = it.id,
                                    usageTime = minToHourMinute(it.todayUsageInMinutes),
                                    onClick = {
                                        onAppClick(it.packageName, it.name, it.todayUsageInMinutes)
                                    }
                                )

                                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.dimen_12dp)))
                            }
                        }
                    }
                }

            }
        }}
    }

@Composable
fun CircularGraph(appsData: List<LoadAppDataWithUsage>) {
    if (appsData.isEmpty()) return

    // Group < 30 min into Other
    val mainApps = appsData.filter { it.todayUsageInMinutes >= 30 }
    val otherApps = appsData.filter { it.todayUsageInMinutes < 30 }
    val totalOthers = otherApps.sumOf { it.todayUsageInMinutes }

    val finalList = buildList {
        addAll(mainApps)
        if (totalOthers > 0) {
            add(
                LoadAppDataWithUsage(
                    id = -1,
                    name = "Other",
                    packageName = "",
                    todayUsageInMinutes = totalOthers
                )
            )
        }
    }

    val totalUsage = finalList.sumOf { it.todayUsageInMinutes }

    val colors = listOf(
        Color(0xFF0091EA), Color(0xFFE53935), Color(0xFFFFB300),
        Color(0xFF8E24AA), Color(0xFF43A047), Color(0xFFFB8C00)
    )

    val chartSize = 160.dp   // chart size (half size)
    Row {
        Spacer(Modifier.weight(1f))

    Box(
        modifier = Modifier
            .padding(40.dp)
            .size(chartSize)
            .aspectRatio(1f), // perfect circle
        contentAlignment = Alignment.Center
    ) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            val strokeWidth = 26f
            val outerRadius = size.minDimension / 2
            val innerRadius = outerRadius - strokeWidth * 1.1f

            val labelRadius = outerRadius + 58f   // FIXED: adjusted for perfect equal distance

            var startAngle = -90f

            finalList.forEachIndexed { index, app ->

                val sweep = (app.todayUsageInMinutes.toFloat() / totalUsage) * 360f

                drawArc(
                    color = colors[index % colors.size],
                    startAngle = startAngle,
                    sweepAngle = sweep,
                    useCenter = false,
                    style = Stroke(width = strokeWidth)
                )

                // Shorten name if long
                val cleanName = if (app.name.length > 8)
                    app.name.take(5) + "…"
                else app.name

                // Mid angle for label position
                val midAngle = startAngle + sweep / 2
                val rad = Math.toRadians(midAngle.toDouble())

                val labelX = center.x + labelRadius * cos(rad).toFloat()
                val labelY = center.y + labelRadius * sin(rad).toFloat()

                // Text Paint
                val textPaint = AndroidPaint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 22f
                    textAlign = AndroidPaint.Align.CENTER
                    isAntiAlias = true
                }

                // ⭐⭐ IMPORTANT FIX — baseline adjustment ⭐⭐
                val textOffset = (textPaint.descent() + textPaint.ascent()) / 2

                drawContext.canvas.nativeCanvas.drawText(
                    cleanName,
                    labelX,
                    labelY - textOffset,   // <<< THIS FIXES UNEVEN LABEL SPACING
                    textPaint
                )

                startAngle += sweep
            }

            // Center white donut
            drawCircle(
                color = Color.White,
                radius = innerRadius
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("TODAY", color = Color.Gray, fontSize = 10.sp)
            Text(minToHourMinute(totalUsage), color = Color.Black, fontSize = 14.sp)
        }}
        Spacer(Modifier.weight(1f))

    }

}
fun minToHourMinute(min: Long): String {
    return when {
        min < 1 -> "0 min"
        min < 60 -> "$min min"
        else -> {
            val hours = min / 60
            val minutes = min % 60

            if (minutes == 0L) {
                "$hours hr"
            } else {
                "$hours hr $minutes min"
            }
        }
    }
}


@Composable
private fun FloatingActionButtonUi(
    onFloatingButtonClicked: () -> Unit,
    showPermissionDialog: Boolean
) {

    IconButton(
        onClick = {
            onFloatingButtonClicked()
        },
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = onPrimaryContainerLight.copy(alpha = 0.9f)
        ),
        modifier = Modifier.size(dimensionResource(R.dimen.dimen_50dp)),
        enabled = !showPermissionDialog
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(R.string.floating_action_button),
            modifier = Modifier.size(dimensionResource(R.dimen.dimen_30dp)),
            tint = Color.White
        )
    }

}

