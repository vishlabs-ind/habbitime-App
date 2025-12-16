package com.rach.habitchange.presentations

import android.content.Context
import android.provider.Settings
import android.text.TextUtils
import android.util.Log

object AccessibilityUtil {

    fun isServiceEnabled(context: Context, serviceName: String): Boolean {
        Log.d("ServiceEnableUi","Entered")
        val enabled = try {
            Log.d("ServiceEnableUi","entered try")
            Settings.Secure.getInt(
                context.contentResolver,
                Settings.Secure.ACCESSIBILITY_ENABLED
            )
        }
     catch (e: Exception) {
        Log.d("ServiceEnableUi", "entered catch")
        return false
    }


    if (enabled == 1) {
            Log.d("ServiceEnableUi","entered if")
            val settingValue = Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
            ) ?: return false
Log.d("ServiceEnableUi","entered else")
            val splitter = TextUtils.SimpleStringSplitter(':')
            Log.d("ServiceEnableUi","entered splitter")
            splitter.setString(settingValue)
            Log.d("ServiceEnableUi","entered splitter")

            while (splitter.hasNext()) {
                Log.d("ServiceEnableUi","entered while")
                val accessibilityService = splitter.next()
                Log.d("ServiceEnableUi","entered accessibility")
                if (accessibilityService.contains(serviceName)) {
                    Log.d("ServiceEnableUi","entered if accessibility")
                    return true
                }
            }
        }
        Log.d("ServiceEnableUi","entered else return")
        return false
    }
}
