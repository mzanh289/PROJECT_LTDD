package com.example.project_enlishlearning.utils.notification

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "notification_settings"
)

class NotificationPreferences(
    private val context: Context
) {

    companion object {

        private val DAILY_REMINDER =
            booleanPreferencesKey("daily_reminder")

        private val REMINDER_HOUR =
            intPreferencesKey("reminder_hour")

        private val REMINDER_MINUTE =
            intPreferencesKey("reminder_minute")

        private val REVIEW_REMINDER =
            booleanPreferencesKey("review_reminder")

        private val PUSH_NOTIFICATION =
            booleanPreferencesKey("push_notification")
    }

    val dailyReminderEnabled: Flow<Boolean> =
        context.dataStore.data.map {
            it[DAILY_REMINDER] ?: true
        }

    val reminderHour: Flow<Int> =
        context.dataStore.data.map {
            it[REMINDER_HOUR] ?: 20
        }

    val reminderMinute: Flow<Int> =
        context.dataStore.data.map {
            it[REMINDER_MINUTE] ?: 0
        }

    val reviewReminderEnabled: Flow<Boolean> =
        context.dataStore.data.map {
            it[REVIEW_REMINDER] ?: true
        }

    val pushNotificationEnabled: Flow<Boolean> =
        context.dataStore.data.map {
            it[PUSH_NOTIFICATION] ?: true
        }

    suspend fun saveDailyReminder(
        enabled: Boolean
    ) {
        context.dataStore.edit {
            it[DAILY_REMINDER] = enabled
        }
    }

    suspend fun saveReminderTime(
        hour: Int,
        minute: Int
    ) {
        context.dataStore.edit {
            it[REMINDER_HOUR] = hour
            it[REMINDER_MINUTE] = minute
        }
    }

    suspend fun saveReviewReminder(
        enabled: Boolean
    ) {
        context.dataStore.edit {
            it[REVIEW_REMINDER] = enabled
        }
    }

    suspend fun savePushNotification(
        enabled: Boolean
    ) {
        context.dataStore.edit {
            it[PUSH_NOTIFICATION] = enabled
        }
    }
}