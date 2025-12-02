package com.rach.core.data.repoImp

import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.util.Log
import com.rach.core.domain.repo.UsageStatsRepository
import java.util.Calendar
import java.util.TimeZone
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UsageStatsRepositoryImpl @Inject constructor(
    private val context: Context
) : UsageStatsRepository {

    override fun getUsageStatsForRange(
        startTimeInMillis: Long,
        endTimeInMills: Long,
        selectedPackageName: List<String>
    ): Map<String, Long> {

        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

        val events = usageStatsManager.queryEvents(startTimeInMillis, endTimeInMills)

        val foregroundTimes = mutableMapOf<String, Long>()
        val lastForegroundTime = mutableMapOf<String, Long>()

        val event = UsageEvents.Event()

        while (events.hasNextEvent()) {
            events.getNextEvent(event)
            val pkg = event.packageName
            if (!selectedPackageName.contains(pkg)) continue

            when (event.eventType) {
                UsageEvents.Event.MOVE_TO_FOREGROUND -> {
                    lastForegroundTime[pkg] = event.timeStamp
                }
                UsageEvents.Event.MOVE_TO_BACKGROUND -> {
                    val start = lastForegroundTime[pkg]
                    if (start != null) {
                        // ✅ Cross-day fix: truncate within range
                        val adjustedStart = maxOf(start, startTimeInMillis)
                        val adjustedEnd = minOf(event.timeStamp, endTimeInMills)
                        val duration = adjustedEnd - adjustedStart

                        if (duration > 0) {
                            val totalSoFar = foregroundTimes[pkg] ?: 0L
                            foregroundTimes[pkg] = totalSoFar + duration
                        }

                        lastForegroundTime.remove(pkg)
                    }
                }
            }
        }

        val result = foregroundTimes.mapValues { (_, duration) ->
            TimeUnit.MILLISECONDS.toMinutes(duration)
        }

        Log.d("UsageStats", "Events-based usage: $result")
        return result
    }

    override fun getTodayUsage(): Pair<Long, Long> {
        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()
        Log.d("UsageStats", "Today Time Range: $startTime - $endTime")
        return Pair(startTime, endTime)
    }

    override fun getFiveDaysUsage(day: Int): Pair<Long, Long> {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY,0)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND,0)
            set(Calendar.MILLISECOND,0)
        }

        calendar.add(Calendar.DAY_OF_YEAR, -(day+1))
        val startTime = calendar.timeInMillis

        //End time: next day ka 0:00 hoga
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endTime = calendar.timeInMillis
        return Pair(startTime, endTime)

    }
}
