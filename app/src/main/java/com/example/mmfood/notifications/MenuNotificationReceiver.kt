package com.example.mmfood.notifications

import android.Manifest
import android.app.PendingIntent
import android.graphics.BitmapFactory
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.mmfood.AppContainerFactory
import com.example.mmfood.MainActivity
import com.example.mmfood.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class MenuNotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val mealType = intent.getStringExtra(MenuNotificationScheduler.EXTRA_MEAL_TYPE)
                    ?.let(MealType::valueOf)
                    ?: MealType.LUNCH
                val catalog = AppContainerFactory(context.applicationContext as android.app.Application)
                    .create()
                    .menuCatalogDataSource
                    .getCatalog()
                val selection = catalog.selectionForDate(LocalDate.now())
                val mealNames = when (mealType) {
                    MealType.LUNCH -> selection.lunchOptions
                    MealType.DINNER -> selection.dinnerOptions
                }.map { it.name }
                val title = if (mealType == MealType.LUNCH) "Comida" else "Cena"
                val content = mealNames.ifEmpty { listOf("Sin opciones") }.joinToString(" · ")

                val launchIntent = Intent(context, MainActivity::class.java)
                val contentIntent = PendingIntent.getActivity(
                    context,
                    if (mealType == MealType.LUNCH) 2001 else 2002,
                    launchIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )

                val notification = NotificationCompat.Builder(context, MenuNotificationScheduler.CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                    .setContentTitle(title)
                    .setContentText(content)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(content))
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build()

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                    NotificationManagerCompat.from(context).notify(
                        if (mealType == MealType.LUNCH) 3001 else 3002,
                        notification,
                    )
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
