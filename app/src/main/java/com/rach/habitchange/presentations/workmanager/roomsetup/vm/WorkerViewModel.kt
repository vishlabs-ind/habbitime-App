package com.rach.habitchange.presentations.workmanager.roomsetup.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.rach.habitchange.presentations.workmanager.roomsetup.RepoUsage
import com.rach.habitchange.presentations.workmanager.roomsetup.worker.CheckUsageWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class WorkerViewModel @Inject constructor(private val repo : RepoUsage,
                                          private val workManager: WorkManager): ViewModel() {



    fun saveLimit(packageName: String, limitInSec: Long){
        Log.d("WorkerVMUI", "saveLimit called: $packageName → $limitInSec sec")
        viewModelScope.launch {
            Log.d("WorkerVMUI", "saveLimit launch called: $packageName → $limitInSec sec")
            repo.saveLimit(packageName, limitInSec)
            Log.d("WorkerVMUI", "Limit saved: $packageName → $limitInSec sec")
        }
    }

//    fun scheduleUsageCheckWorker(packageName: String){
//        Log.d("WorkerVMUI", "scheduleUsageCheckWorker called: $packageName")
//        val data = workDataOf("packageName" to packageName)
//
//        val request = PeriodicWorkRequestBuilder<CheckUsageWorker>(
//            15, java.util.concurrent.TimeUnit.MINUTES
//        ).setInputData(data).build()
//        workManager.enqueueUniquePeriodicWork(
//            "CheckUsageWorker",
//            androidx.work.ExistingPeriodicWorkPolicy.UPDATE,
//            request
//        )
//        Log.d("WorkerVM", "Worker scheduled for: $packageName")
//    }

    fun scheduleUsageCheckOnce(packageName: String) {

        Log.d("WorkerVMUI", "scheduleUsageCheckOnce called: $packageName")

        val data = workDataOf(
            "packageName" to packageName
        )

        val request = OneTimeWorkRequestBuilder<CheckUsageWorker>()
            .setInitialDelay(2, TimeUnit.MINUTES)   // ⏱️ run once after 2 min
            .setInputData(data)
            .build()

        workManager.enqueueUniqueWork(
            "CheckUsageWorker_$packageName",         // unique per app
            ExistingWorkPolicy.REPLACE,              // replace if rescheduled
            request
        )

        Log.d("WorkerVM", "One-time worker scheduled for: $packageName")
    }

}