package com.example.project_enlishlearning.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_enlishlearning.utils.notification.AlarmScheduler
import com.example.project_enlishlearning.utils.notification.NotificationHelper
import com.example.project_enlishlearning.utils.notification.NotificationPreferences
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NotificationViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val _dailyReminderEnabled = MutableStateFlow(true)
    val dailyReminderEnabled: StateFlow<Boolean> =
        _dailyReminderEnabled.asStateFlow()

    private val _selectedHour = MutableStateFlow(20)
    val selectedHour: StateFlow<Int> = _selectedHour

    private val _selectedMinute = MutableStateFlow(0)
    val selectedMinute: StateFlow<Int> = _selectedMinute

    private val _reviewReminderEnabled = MutableStateFlow(true)
    val reviewReminderEnabled: StateFlow<Boolean> =
        _reviewReminderEnabled.asStateFlow()

    private val _pushNotificationEnabled = MutableStateFlow(true)
    val pushNotificationEnabled: StateFlow<Boolean> =
        _pushNotificationEnabled.asStateFlow()

    private val prefs =
        NotificationPreferences(application)

    init {
        viewModelScope.launch {
            prefs.dailyReminderEnabled.collect {
                _dailyReminderEnabled.value = it
            }
        }

        viewModelScope.launch {
            prefs.reminderHour.collect {
                _selectedHour.value = it
            }
        }

        viewModelScope.launch {
            prefs.reminderMinute.collect {
                _selectedMinute.value = it
            }
        }

        viewModelScope.launch {
            prefs.reviewReminderEnabled.collect {
                _reviewReminderEnabled.value = it
            }
        }

        viewModelScope.launch {
            prefs.pushNotificationEnabled.collect { enabled ->
                _pushNotificationEnabled.value = enabled
                if (enabled) {
                    subscribeToDailyLearningTopic()
                } else {
                    unsubscribeFromDailyLearningTopic()
                }
            }
        }

        viewModelScope.launch {
            val enabled =
                prefs.dailyReminderEnabled.first()

            val hour =
                prefs.reminderHour.first()

            val minute =
                prefs.reminderMinute.first()

            if (enabled) {
                AlarmScheduler.scheduleDailyReminder(
                    getApplication(),
                    hour,
                    minute
                )
            }
        }
    }

    fun toggleDailyReminder(
        enabled: Boolean
    ) {
        Log.d(
            "ALARM_TEST",
            "toggleDailyReminder $enabled"
        )

        _dailyReminderEnabled.value = enabled

        viewModelScope.launch {
            prefs.saveDailyReminder(enabled)
        }

        if (enabled) {
            AlarmScheduler.scheduleDailyReminder(
                getApplication(),
                _selectedHour.value,
                _selectedMinute.value
            )
        } else {
            AlarmScheduler.cancelReminder(
                getApplication()
            )
        }
    }

    fun updateReminderTime(
        hour: Int,
        minute: Int
    ) {
        Log.d(
            "ALARM_TEST",
            "updateReminderTime $hour:$minute"
        )

        _selectedHour.value = hour
        _selectedMinute.value = minute

        viewModelScope.launch {
            prefs.saveReminderTime(
                hour,
                minute
            )
        }

        if (_dailyReminderEnabled.value) {
            AlarmScheduler.scheduleDailyReminder(
                getApplication(),
                hour,
                minute
            )
        }
    }

    fun toggleReviewReminder(enabled: Boolean) {
        _reviewReminderEnabled.value = enabled
        viewModelScope.launch {
            prefs.saveReviewReminder(enabled)
        }
    }

    fun togglePushNotification(enabled: Boolean) {
        _pushNotificationEnabled.value = enabled
        viewModelScope.launch {
            prefs.savePushNotification(enabled)
        }
        if (enabled) {
            subscribeToDailyLearningTopic()
        } else {
            unsubscribeFromDailyLearningTopic()
        }
    }

    private fun subscribeToDailyLearningTopic() {
        try {
            FirebaseMessaging.getInstance()
                .subscribeToTopic("daily_learning")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FCM_SUBSCRIBE", "Successfully subscribed to daily_learning topic")
                    } else {
                        Log.w("FCM_SUBSCRIBE", "Subscription to daily_learning failed", task.exception)
                    }
                }
        } catch (e: Exception) {
            Log.e("FCM_SUBSCRIBE", "Error subscribing to FCM topic", e)
        }
    }

    private fun unsubscribeFromDailyLearningTopic() {
        try {
            FirebaseMessaging.getInstance()
                .unsubscribeFromTopic("english_learning")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FCM_SUBSCRIBE", "Successfully unsubscribed from daily_learning topic")
                    } else {
                        Log.w("FCM_SUBSCRIBE", "Unsubscription from daily_learning failed", task.exception)
                    }
                }
        } catch (e: Exception) {
            Log.e("FCM_SUBSCRIBE", "Error unsubscribing from FCM topic", e)
        }
    }

    fun testNotification() {
        NotificationHelper.showDailyReminder(
            getApplication()
        )
    }
}