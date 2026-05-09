package com.example.mmfood.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

class MenuNotificationScheduler(
    private val context: Context,
) {
    fun ensureChannels() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Menú diario",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Recordatorios de comida y cena"
        }
        notificationManager.createNotificationChannel(channel)
    }

    fun scheduleDailyNotifications() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        schedule(
            alarmManager = alarmManager,
            requestCode = REQUEST_CODE_LUNCH,
            mealType = MealType.LUNCH,
            triggerTime = LocalTime.of(10, 0),
        )
        schedule(
            alarmManager = alarmManager,
            requestCode = REQUEST_CODE_DINNER,
            mealType = MealType.DINNER,
            triggerTime = LocalTime.of(18, 0),
        )
    }

    private fun schedule(
        alarmManager: AlarmManager,
        requestCode: Int,
        mealType: MealType,
        triggerTime: LocalTime,
    ) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(context, MenuNotificationReceiver::class.java).apply {
                action = ACTION_NOTIFY_MENU
                putExtra(EXTRA_MEAL_TYPE, mealType.name)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        alarmManager.cancel(pendingIntent)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            nextTriggerMillis(triggerTime),
            AlarmManager.INTERVAL_DAY,
            pendingIntent,
        )
    }

    private fun nextTriggerMillis(triggerTime: LocalTime): Long {
        val now = LocalDateTime.now()
        val todayAtTime = now.toLocalDate().atTime(triggerTime)
        val next = if (todayAtTime.isAfter(now)) todayAtTime else todayAtTime.plusDays(1)
        return next.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    companion object {
        const val CHANNEL_ID = "daily_menu_channel"
        const val ACTION_NOTIFY_MENU = "com.example.mmfood.NOTIFY_MENU"
        const val EXTRA_MEAL_TYPE = "meal_type"
        private const val REQUEST_CODE_LUNCH = 1001
        private const val REQUEST_CODE_DINNER = 1002
    }
}

enum class MealType {
    LUNCH,
    DINNER,
}
