package com.example.project_enlishlearning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.project_enlishlearning.data.local.database.AppDatabase
import com.example.project_enlishlearning.navigation.AppNavGraph
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import com.example.project_enlishlearning.utils.notification.NotificationChannelHelper
import com.example.project_enlishlearning.viewmodel.AuthViewModel
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import com.example.project_enlishlearning.utils.DatabaseProvider
import com.example.project_enlishlearning.navigation.Screen

class MainActivity : ComponentActivity() {

    private val destinationState = mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DatabaseProvider.init(this)

        requestNotificationPermission()
        NotificationChannelHelper.createChannel(this)

        handleIntent(intent)

        enableEdgeToEdge()
        val database = AppDatabase.getDatabase(this)

        setContent {
            ProjectEnlishLearningTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()

                val destination by destinationState
                val currentBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                LaunchedEffect(destination, currentRoute) {
                    if (destination == "due_review" && currentRoute != null && currentRoute != Screen.Splash.route) {
                        navController.navigate(
                            Screen.ReviewVocabulary.createRoute(
                                Screen.ReviewVocabulary.GLOBAL_DUE_REVIEW_SET_ID
                            )
                        )
                        destinationState.value = null
                    }
                }

                AppNavGraph(
                    navController = navController,
                    database = database,
                    authViewModel = authViewModel,
                    onSessionValid = {
                        val currentDest = destinationState.value
                        if (currentDest == "due_review") {
                            destinationState.value = null
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Splash.route) {
                                    inclusive = true
                                }
                            }
                            navController.navigate(
                                Screen.ReviewVocabulary.createRoute(
                                    Screen.ReviewVocabulary.GLOBAL_DUE_REVIEW_SET_ID
                                )
                            )
                        } else {
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(Screen.Splash.route) {
                                    inclusive = true
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val destination = intent.getStringExtra("destination")
        if (destination != null) {
            destinationState.value = destination
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
