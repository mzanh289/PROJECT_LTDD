package com.example.project_enlishlearning.utils.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationChannelHelper {

    const val CHANNEL_ID = "study_reminder_channel"

    fun createChannel(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Study Reminder",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Daily reminder to study vocabulary"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}