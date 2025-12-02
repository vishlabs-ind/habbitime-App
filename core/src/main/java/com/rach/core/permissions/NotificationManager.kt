package com.rach.core.permissions


const val CHANNEL_ID = "HabitTime"
const val NOTIFICATION_ID = 1
const val HABIT_TIME_NOTIFICATION_CHANNEL_DESCRIPTION = "Show notifications whenever work starts"
//
//@Singleton
//class NotificationManagerClass @Inject constructor(
//    private val context: Context
//) {
//    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
//    fun notification(notificationModel: SendNotificationModel) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "Habit Time working",
//                NotificationManager.IMPORTANCE_HIGH
//            ).apply {
//                description = HABIT_TIME_NOTIFICATION_CHANNEL_DESCRIPTION
//            }
//
//            val notificationManager =
//                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_launcher_round)
//            .setContentTitle(notificationModel.notificationTitle)
//            .setContentText(notificationModel.message)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setVibrate(LongArray(0))
//
//        val notificationManager = NotificationManagerCompat.from(context)
//
//        /**
//         * यह लाइन चेक कर रही है कि डिवाइस Android 13 (TIRAMISU, API level 33) या उससे ऊपर पर चल रहा है या नहीं।
//         * क्यों ज़रूरी है ये चेक?
//         * Android 13 (TIRAMISU) में कुछ नए permissions और behaviors आए हैं, जैसे:
//         *
//         * POST_NOTIFICATIONS permission – अब notifications भेजने से पहले runtime permission लेनी पड़ती है।
//         *
//         * Photo picker, improved privacy, आदि features।
//
//         */
//
//        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
//
//    }
//}