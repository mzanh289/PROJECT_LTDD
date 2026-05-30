package com.example.project_enlishlearning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.navigation.AppNavGraph
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectEnlishLearningTheme {
                // Khởi tạo navController tại đây
                val navController = rememberNavController()

                // Chỉ gọi đúng 1 NavGraph duy nhất, truyền navController vào đó
                AppNavGraph(navController = navController)
            }
        }
    }
}