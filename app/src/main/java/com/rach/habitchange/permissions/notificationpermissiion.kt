package com.rach.habitchange.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun NotiicationPermission(
    content : @Composable () -> Unit
){
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        isGranted ->
    }

    LaunchedEffect(Unit) {
        Log.d("NotiPermUI", "LaunchedEffect called")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            Log.d("NotiPermUI", "Android 13+ device")

            val isgranted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            Log.d("NotiPermUI", "Granted = $isgranted")

            if (!isgranted){
                Log.d("NotiPermUI", "Launching permission dialog")

                permissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            }
        }
    }


    content()
    }
