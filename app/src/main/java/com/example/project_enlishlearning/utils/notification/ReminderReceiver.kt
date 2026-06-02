package com.example.project_enlishlearning.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.project_enlishlearning.data.local.database.AppDatabase
import com.example.project_enlishlearning.utils.FirebaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        Log.d("ALARM_TEST", "RECEIVER FIRED")

        val prefs = NotificationPreferences(context)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dailyEnabled = prefs.dailyReminderEnabled.first()
                val reviewEnabled = prefs.reviewReminderEnabled.first()

                if (dailyEnabled) {
                    NotificationHelper.showDailyReminder(context)
                }

                if (reviewEnabled) {
                    val userId = FirebaseManager.auth.currentUser?.uid ?: "local_user"
                    val db = AppDatabase.getDatabase(context)
                    val dueCount = db.learningProgressDao().getReviewDueCount(userId)
                    
                    Log.d("ALARM_TEST", "User $userId has $dueCount due words for review")
                    if (dueCount > 0) {
                        NotificationHelper.showReviewReminder(context, dueCount)
                    }
                }

                // Reschedule for next day at the user-defined time
                val hour = prefs.reminderHour.first()
                val minute = prefs.reminderMinute.first()
                AlarmScheduler.scheduleDailyReminder(context, hour, minute)
            } catch (e: Exception) {
                Log.e("ALARM_TEST", "Error in ReminderReceiver processing", e)
            }
        }
    }
}