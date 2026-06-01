package com.example.project_enlishlearning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.data.local.database.AppDatabase
import com.example.project_enlishlearning.navigation.AppNavGraph
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import com.example.project_enlishlearning.utils.notification.NotificationChannelHelper
import com.example.project_enlishlearning.viewmodel.AuthViewModel
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestNotificationPermission()

        NotificationChannelHelper.createChannel(this)

        enableEdgeToEdge()
        val database = AppDatabase.getDatabase(this)

        setContent {
            ProjectEnlishLearningTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()

                AppNavGraph(
                    navController = navController,
                    database = database,
                    authViewModel = authViewModel
                )
            }
        }
    }

    private fun requestNotificationPermission() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {

            val granted = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!granted) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }
}

