package com.rach.habitchange.presentations.workmanager.roomsetup.worker

import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rach.habitchange.presentations.workmanager.roomsetup.RepoUsage

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import androidx.hilt.work.HiltWorker
import com.rach.habitchange.presentations.workmanager.roomsetup.NotificationLimit


@HiltWorker
class CheckUsageWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: RepoUsage
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val packageName = inputData.getString("packageName") ?: return Result.failure()
        val limit = repository.getLimit(packageName) ?: return Result.failure()
        val currentUsage = getTodayUsage(applicationContext, packageName)
        Log.d("CheckWorker", "Running check → pkg=$packageName usage=$currentUsage limit=$limit")

        if (currentUsage > limit) {
            Log.d("CheckWorker", "Limit exceeded → sending notification")

            NotificationLimit(applicationContext).showNotification(packageName, currentUsage, limit)
        }
        return Result.success()
    }
    private fun getTodayUsage(applicationContext: Context, packageName: String): Long {
        val usageStatsManger = applicationContext.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val end = System.currentTimeMillis()
        val start = end - 24 * 60 * 60 * 1000

        val stats = usageStatsManger.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY, start, end
        )
        val appStats = stats.find {
            it.packageName == packageName
        }
        return appStats?.totalTimeInForeground ?: 0
    }
}