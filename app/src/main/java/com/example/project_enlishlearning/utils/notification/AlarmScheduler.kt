package com.example.project_enlishlearning.utils.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar
import android.util.Log
import java.util.Date

object AlarmScheduler {

    private const val REQUEST_CODE = 1001
    private const val TAG = "ALARM_TEST"

    fun scheduleDailyReminder(
        context: Context,
        hour: Int,
        minute: Int
    ) {
        Log.d(TAG, "Scheduling alarm at $hour:$minute")

        cancelReminder(context)

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(
            context,
            ReminderReceiver::class.java
        ).setPackage(context.packageName)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // ===== FIXED TIME LOGIC =====
        val nowMillis = System.currentTimeMillis()

        val targetMillis = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        val triggerMillis = if (targetMillis <= nowMillis) {
            targetMillis + AlarmManager.INTERVAL_DAY
        } else {
            targetMillis
        }

        // ===== LOG DEBUG =====
        Log.d(TAG, "Now millis = $nowMillis")
        Log.d(TAG, "Target millis = $targetMillis")
        Log.d(TAG, "Trigger millis = $triggerMillis")
        Log.d(TAG, "Trigger time = ${Date(triggerMillis)}")

        val canScheduleExact = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            try {
                alarmManager.canScheduleExactAlarms()
            } catch (e: Exception) {
                false
            }
        } else {
            true
        }

        Log.d(TAG, "canScheduleExact = $canScheduleExact")

        if (canScheduleExact) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                pendingIntent
            )
        } else {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                pendingIntent
            )
        }
    }

    fun cancelReminder(
        context: Context
    ) {

        val intent = Intent(
            context,
            ReminderReceiver::class.java
        ).setPackage(context.packageName)

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.cancel(pendingIntent)
    }
}