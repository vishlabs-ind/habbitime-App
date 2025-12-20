package com.rach.habitchange.presentations.accessibility

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import com.rach.habitchange.presentations.workmanager.roomsetup.NotificationLimit
import com.rach.habitchange.presentations.workmanager.roomsetup.RepoUsage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AppUsageAccessibilityService : AccessibilityService() {

    @Inject lateinit var repo: RepoUsage

    private var currentPackage = ""
    private var startTime = 0L
    private var accumulatedUsage = 0L
    private var usageJob: Job? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.d("A11yServiceUI", "Service Connected")

        usageJob = CoroutineScope(Dispatchers.Main.immediate)
            .launch {
            while (isActive) {
                delay(1000)
                Log.d("A11yServiceUI", "Checking limit")
                checkLimitExceeded()
                Log.d("A11yServiceUI", "Limit checked")
            }
        }
    }


override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        if (event?.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return

        val pkg = event.packageName?.toString() ?: return
        if (pkg == currentPackage) return

        if (currentPackage.isNotBlank()) {
            val now = System.currentTimeMillis()
            accumulatedUsage += (now - startTime)
        }

        currentPackage = pkg
        startTime = System.currentTimeMillis()

        Log.d("A11yServiceUI", "Switched to → $pkg")
    }


    override fun onInterrupt() {}

    private suspend fun checkLimitExceeded() {

        val limitInSec = repo.getLimit(currentPackage) ?: return

        val now = System.currentTimeMillis()
        val sessionUsage = now - startTime
        val totalUsage = accumulatedUsage + sessionUsage

        Log.d(
            "A11yServiceUI",
            "[${currentPackage}] totalUsage=${totalUsage / 1000}s, limit=${limitInSec}s"
        )

        if ((totalUsage / 1000) >= limitInSec) {

            NotificationLimit(applicationContext).showNotification(
                currentPackage,
                totalUsage,
                limitInSec
            )
            accumulatedUsage = 0L
            startTime = now

        }
    }
    override fun onDestroy() {
        usageJob?.cancel()
        Log.d("A11yServiceUI", "Service Destroyed")
        super.onDestroy()
    }
}
