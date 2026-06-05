package com.example.project_enlishlearning.utils.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.project_enlishlearning.R
import com.example.project_enlishlearning.MainActivity
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

object NotificationHelper {

    fun showNotification(
        context: Context,
        title: String,
        message: String,
        destination: String? = null,
        setId: Int? = null
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            if (destination != null) {
                putExtra("destination", destination)
            }
            if (setId != null) {
                putExtra("setId", setId)
            }
        }

        val requestCode = (destination?.hashCode() ?: 0) + (setId ?: 0)
        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(
            context,
            NotificationChannelHelper.CHANNEL_ID
        )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val manager = NotificationManagerCompat.from(context)

        val canNotify =
            Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

        if (canNotify) {
            // Use different ID to prevent overwriting notifications if they fire close to each other
            val notificationId = when (destination) {
                "due_review" -> 1002
                "home" -> 1001
                else -> 1003
            }
            manager.notify(notificationId, notification)
        }
    }

    fun showDailyReminder(context: Context) {
        showNotification(
            context = context,
            title = "📚 Time to study!",
            message = "🔔 Don't forget to review your vocabulary today.",
            destination = "home"
        )
    }

    fun showReviewReminder(context: Context, dueCount: Int) {
        showNotification(
            context = context,
            title = "Review Reminder",
            message = "You have $dueCount words ready for review.",
            destination = "due_review"
        )
    }
}