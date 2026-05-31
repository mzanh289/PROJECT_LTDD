package com.example.project_enlishlearning.utils.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(
        context: Context,
        intent: Intent
    ) {

        Log.d(
            "ALARM_TEST",
            "RECEIVER FIRED"
        )

        NotificationHelper.showDailyReminder(context)
    }
}