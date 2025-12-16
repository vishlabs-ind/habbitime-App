package com.rach.habitchange.presentations.workmanager.roomsetup

import android.util.Log
import javax.inject.Inject

class RepoUsage @Inject constructor(
    private val db: Appdatabase
) {

    suspend fun saveLimit( packageName: String, limitInMin: Long){
        Log.d("RepoUsageUI", "saveLimit called: $packageName → $limitInMin min")
        db.daolimit().insertLimit(EntityAppDC(packageName, limitInMin))
        Log.d("RepoUsageUI", "Limit saved: $packageName → $limitInMin min")

    }

    suspend fun getLimit(packageName: String): Long?{
        return db.daolimit().getLimit(packageName)?.limitInMin
    }
}