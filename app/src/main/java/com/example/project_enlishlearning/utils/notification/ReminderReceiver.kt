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

        // 1. Show notification
        NotificationHelper.showDailyReminder(context)

        // 2. Lấy lại giờ đã lưu (hoặc fallback default)
        val prefs = NotificationPreferences(context)

        // chạy coroutine vì Flow
        CoroutineScope(Dispatchers.IO).launch {

            val hour = prefs.reminderHour.first()
            val minute = prefs.reminderMinute.first()

            // 3. schedule lại cho ngày mai
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