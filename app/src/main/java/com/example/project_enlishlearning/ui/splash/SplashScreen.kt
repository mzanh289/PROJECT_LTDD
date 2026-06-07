package com.example.project_enlishlearning.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.ui.components.PrimaryButton
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project_enlishlearning.viewmodel.AuthViewModel
import com.example.project_enlishlearning.R

@Composable
fun SplashScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var isCheckingSession by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (authViewModel.getCurrentUserId() != null) {
            isCheckingSession = true
            authViewModel.checkSession(
                onSessionValid = {
                    isCheckingSession = false
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        }
                    }
                },
                onSessionInvalid = {
                    isCheckingSession = false
                }
            )
        } else {
            isCheckingSession = false
        }
    }

    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF3120E0),
            Color(0xFF4E5BFF),
            Color(0xFF57E3A5)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Logo
            Surface(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(28.dp),
                color = Color.White.copy(alpha = 0.08f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.minlish3),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Title
            Text(
                text = "MinLish",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Learn smarter every day",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "•••",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "GROUP-03",
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = androidx.compose.ui.unit.TextUnit.Unspecified
            )

            Spacer(modifier = Modifier.height(300.dp))

            if (isCheckingSession) {
                androidx.compose.material3.CircularProgressIndicator(
                    color = Color.White
                )
            } else {
                PrimaryButton(
                    text = "Let's Get Started",
                    onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) {
                                inclusive = true
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SplashScreenPreview() {
    ProjectEnlishLearningTheme {
        SplashScreen(
            navController = rememberNavController()
        )
    }
}