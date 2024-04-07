package com.erendogan6.dotoday.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.erendogan6.dotoday.R

class ReminderBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("TODO_TITLE") ?: "ToDo Reminder"

        val notificationManager = ContextCompat.getSystemService(context,
                                                                 NotificationManager::class.java) as NotificationManager

        val channel = NotificationChannel("reminder_channel_id",
                                          "Reminder Channel",
                                          NotificationManager.IMPORTANCE_HIGH).apply {
            description = "Channel for ToDo reminder"
        }

        notificationManager.createNotificationChannel(channel)

        val notification =
            NotificationCompat.Builder(context, "reminder_channel_id").setSmallIcon(R.drawable.logo)
                .setContentTitle(title).setContentText("You have a task to complete.")
                .setPriority(NotificationCompat.PRIORITY_HIGH).setAutoCancel(true).build()

        notificationManager.notify(1, notification)
    }
}
