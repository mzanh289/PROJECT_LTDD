package com.example.project_enlishlearning.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        Log.d("ALARM_TEST", "RECEIVER FIRED")

        NotificationHelper.showDailyReminder(context)

        val prefs = NotificationPreferences(context)

        CoroutineScope(Dispatchers.IO).launch {

            val hour = prefs.reminderHour.first()
            val minute = prefs.reminderMinute.first()

            val calendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, 1)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            AlarmScheduler.scheduleDailyReminder(
                context,
                hour,
                minute
            )
        }
    }
}