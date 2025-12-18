package com.rach.habitchange.presentations.navigation

sealed class Screens(
    val route: String
) {
    object SplashScreen : Screens("SplashScreen")
    object HomeScreen : Screens("HomeScreen")
    object SelectAppScreen : Screens("SelectAppScreen")
    object AccessibilityPermissionScreen :
        Screens("AccessibilityPermissionScreen")

    object AppUsageDetailScreen : Screens("AppUsageDetailScreen") {
        const val PATTERN = "AppUsageDetailScreen/{packageName}/{appName}/{todayUsage}"

        fun createRoute(packageName: String, todayUsage: Long, appName: String): String {
            return "AppUsageDetailScreen/$packageName/$appName/$todayUsage"
        }
    }

}