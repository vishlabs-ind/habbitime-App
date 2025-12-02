package com.rach.habitchange.presentations.model

data class LoadAppDataWithUsage(
    val id: Int,
    val name: String,
    val packageName: String,
    val todayUsageInMinutes: Long,
    val date: String? = null
)
