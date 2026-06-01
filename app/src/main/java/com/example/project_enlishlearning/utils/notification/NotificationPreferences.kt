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
}