package com.rach.habitchange.presentations.workmanager.roomsetup

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.rach.habitchange.R

class NotificationLimit(private val context: Context) {

    private val channelId = "usage_limit_channel_debug"

    fun showNotification(packageName: String, usage: Long, limit: Long) {

        Log.d("NotifyUI", "======== Notification Start ========")
        Log.d("NotifyUI", "Context: $context")
        Log.d("NotifyUI", "packageName: $packageName")
        Log.d("NotifyUI", "usage(ms): $usage | limit(sec): $limit")

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        Log.d("NotifyUI", "NotificationManager obtained")

        // -------- CREATE CHANNEL --------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("NotifyUI", "Creating notification channel...")

            val channel = NotificationChannel(
                channelId,
                "Usage Limit Alerts Debug",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                setShowBadge(true)
                lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
            }

            try {
                manager.createNotificationChannel(channel)
                Log.d("NotifyUI", "Channel created successfully")
            } catch (e: Exception) {
                Log.e("NotifyUI", "Channel creation FAILED → ${e.localizedMessage}")
            }
        }

        // -------- BUILD NOTIFICATION --------
        Log.d("NotifyUI", "Building notification object...")

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.baseline_add_alert_24)
            .setContentTitle("Usage Limit Reached")
            .setContentText("Limit reached for $packageName")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(
                        "Your usage: ${(usage / 1000)} sec\n" +
                                "Limit: ${(limit)} sec"
                    )
            )
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setAutoCancel(true)
            .build()

        Log.d("NotifyUI", "Notification object created")

        // -------- SEND NOTIFICATION --------
        val notifyId = packageName.hashCode()
        Log.d("NotifyUI", "Posting notification with ID = $notifyId")

        Handler(Looper.getMainLooper()).post {
            try {
                manager.notify(notifyId, notification)
                Log.d("NotifyUI", "notify() SUCCESS — Notification should appear!")
            } catch (e: Exception) {
                Log.e("NotifyUI", "notify() FAILED → ${e.localizedMessage}")
            }
        }

        Log.d("NotifyUI", "======== Notification End ========")
    }
}
