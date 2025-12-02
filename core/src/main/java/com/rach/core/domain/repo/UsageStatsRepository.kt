package com.rach.core.domain.repo

interface UsageStatsRepository {

    fun getUsageStatsForRange(
        startTimeInMillis:Long,
        endTimeInMills:Long,
        selectedPackageName:List<String>
    ):Map<String,Long>

    fun getTodayUsage():Pair<Long,Long>
    fun getFiveDaysUsage(day:Int):Pair<Long,Long>

}