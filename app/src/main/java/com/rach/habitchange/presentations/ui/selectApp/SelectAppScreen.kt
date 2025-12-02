package com.rach.habitchange.presentations.ui.selectApp

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rach.habitchange.R
import com.rach.habitchange.presentations.ui.selectApp.components.MultiSelectionList
import com.rach.habitchange.presentations.ui.selectApp.components.SelectAppSingleDesign
import com.rach.habitchange.presentations.uiComponents.CircularConfirmButton
import com.rach.habitchange.presentations.uiComponents.CustomTopAppBar
import com.rach.habitchange.presentations.viewModel.SelectAppViewModel
import com.rach.habitchange.theme.HabitChangeTheme


@Composable
fun SelectAppScreen(
    modifier: Modifier = Modifier,
    onNavBackClick: () -> Unit,
    selectedAppViewModel: SelectAppViewModel = hiltViewModel()
) {

    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        selectedAppViewModel.getInstallApps()
    }

    val uiState by selectedAppViewModel.uiState.collectAsState()
    val appPagingData by selectedAppViewModel.installApps.collectAsState()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let {
            snackBarHostState.showSnackbar(it)
            selectedAppViewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let {
            snackBarHostState.showSnackbar(it)
            selectedAppViewModel.clearSuccessMessage()
        }
    }

    BackHandler {
        if (uiState.isMultiSelectionModeEnabled) {
            selectedAppViewModel.disableMultiSelectionMode()
        } else {
            onNavBackClick()
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                title = "Select App",
                titleStyle = MaterialTheme.typography.titleLarge,
                titleFontWeight = FontWeight.Medium,
                onNavigationIconClick = onNavBackClick
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->


        Box(
            modifier = modifier
                .padding(top = paddingValues.calculateTopPadding())

        ) {

            MultiSelectionList(
                items = appPagingData,
                selectedItem = uiState.selectedItems,
                onClick = {
                    selectedAppViewModel.onItemClick(it)
                },
                onLongClick = {
                    selectedAppViewModel.onLongItemClick(it)
                },
                key = {
                    it.id
                },
                isMultiSelectionModeEnabled = uiState.isMultiSelectionModeEnabled,
                itemContent = { item ->
                    SelectAppSingleDesign(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        selected = uiState.selectedItems.contains(item),
                        packageName = item.packageName,
                        appName = item.name
                    )
                },
            )

            if (uiState.isMultiSelectionModeEnabled) {
                CircularConfirmButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(bottom = 50.dp),
                    onClick = {
                        selectedAppViewModel.onConfirmButtonClick()
                    },
                    imageContent = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(
                                dimensionResource(R.dimen.dimen_28dp)
                            )
                        )
                    }
                )
            }

            when {
                uiState.loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    HabitChangeTheme {
        SelectAppScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.normal_padding)),
            onNavBackClick = {}
        )
    }
}