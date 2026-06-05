package com.example.project_enlishlearning.utils.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM_SERVICE", "Message received from: ${remoteMessage.from}")

        val prefs = NotificationPreferences(applicationContext)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val enabled = prefs.pushNotificationEnabled.first()
                if (enabled) {
                    val title = remoteMessage.notification?.title 
                        ?: remoteMessage.data["title"] 
                        ?: "Daily Learning"
                    
                    val body = remoteMessage.notification?.body 
                        ?: remoteMessage.data["body"] 
                        ?: "Check out your daily vocabulary learning!"

                    NotificationHelper.showNotification(
                        context = applicationContext,
                        title = title,
                        message = body,
                        destination = "home"
                    )
                } else {
                    Log.d("FCM_SERVICE", "Push notification ignored (disabled in settings)")
                }
            } catch (e: Exception) {
                Log.e("FCM_SERVICE", "Error processing FCM notification", e)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM_SERVICE", "New registration token: $token")
        // Token could be sent to app server if required by backend, currently not specified.
    }
}
