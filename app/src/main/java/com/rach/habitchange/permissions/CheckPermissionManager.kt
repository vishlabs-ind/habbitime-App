package com.rach.habitchange.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.rach.habitchange.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CheckPermissionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

/**
 * Build.VERSION_CODES.TIRAMISU This refers Greater Than Android 12
 * Means we don't need to take permission in Less Android 13 means 12 ,11
 * mtlb 12 or uske niche ke liye permission lene ki jarut nhi thi phle
 */
    fun hasPermissionGranted(): Boolean{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }else{
            true
        }
    }


    fun showPermissionRationale(activity: MainActivity): Boolean{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }else{
           false
        }
    }
}