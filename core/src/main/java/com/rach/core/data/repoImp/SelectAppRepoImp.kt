package com.rach.core.data.repoImp

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.rach.core.data.model.SelectedAppInfoCore
import com.rach.core.domain.repo.AppRepository
import javax.inject.Inject

class SelectAppRepoImp @Inject constructor(
    private val context: Context
) : AppRepository {
    override suspend fun getAllInstalledApps(): Result<List<SelectedAppInfoCore>> {
        return try {
            val packageManager = context.packageManager

            // List of allowed system apps (must match <queries> in manifest)
            val allowedSystemApps = listOf(
                "com.google.android.youtube",
                "com.android.chrome",
                "com.facebook.katana",
                "com.instagram.android",
                "com.twitter.android"
            )

            // Query installed applications based on <queries> configuration
            val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                .filter {
                    val isSystemApp = (it.flags and ApplicationInfo.FLAG_SYSTEM) != 0
                    val isUserApp = !isSystemApp
                    val isAllowedSystemApp = allowedSystemApps.contains(it.packageName)
                    val hasLaunchIntent =
                        packageManager.getLaunchIntentForPackage(it.packageName) != null

                    // Include user apps or allowed system apps with a launch intent
                    hasLaunchIntent && (isUserApp || isAllowedSystemApp)
                }
                .mapIndexed { index, applicationInfo ->
                    SelectedAppInfoCore(
                        id = index + 1,
                        name = applicationInfo.loadLabel(packageManager).toString(),
                        packageName = applicationInfo.packageName
                    )
                }

            // Sort apps by name for consistent UI (optional)
            val sortedApps = apps.sortedBy { it.name }

            Result.success(sortedApps)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
