package com.example.project_enlishlearning.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.viewmodel.AuthViewModel
import com.example.project_enlishlearning.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel
) {
    val profile by profileViewModel.profile.collectAsState()

    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(authViewModel.getCurrentUserEmail()) }
    var englishLevel by remember { mutableStateOf("A1") }
    var learningGoal by remember { mutableStateOf("") }
    var dailyNewWordTarget by remember { mutableStateOf("10") }
    var dailyReviewTarget by remember { mutableStateOf("20") }

    LaunchedEffect(profile) {
        profile?.let {
            displayName = it.displayName
            email = it.email
            englishLevel = it.englishLevel
            learningGoal = it.learningGoal
            dailyNewWordTarget = it.dailyNewWordTarget.toString()
            dailyReviewTarget = it.dailyReviewTarget.toString()
        }
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Edit Profile",
                subtitle = "Update your learning profile.",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    EditProfileHeader()
                }

                item {
                    EditProfileCard {
                        ModernProfileTextField(
                            value = displayName,
                            onValueChange = { displayName = it },
                            label = "Full Name",
                            icon = Icons.Default.Person
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ModernProfileTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = "Email",
                            icon = Icons.Default.Email,
                            enabled = false
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ModernProfileTextField(
                            value = englishLevel,
                            onValueChange = { englishLevel = it.uppercase() },
                            label = "English Level",
                            icon = Icons.Default.School
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ModernProfileTextField(
                            value = learningGoal,
                            onValueChange = { learningGoal = it },
                            label = "Learning Goal",
                            icon = Icons.Default.Flag
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ModernProfileTextField(
                            value = dailyNewWordTarget,
                            onValueChange = { dailyNewWordTarget = it },
                            label = "New Words Per Day",
                            icon = Icons.Default.Timer
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ModernProfileTextField(
                            value = dailyReviewTarget,
                            onValueChange = { dailyReviewTarget = it },
                            label = "Reviews Per Day",
                            icon = Icons.Default.Timer
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        Button(
                            onClick = {
                                profileViewModel.saveProfile(
                                    email = email,
                                    displayName = displayName,
                                    englishLevel = englishLevel,
                                    learningGoal = learningGoal,
                                    dailyNewWordTarget = dailyNewWordTarget.toIntOrNull() ?: 10,
                                    dailyReviewTarget = dailyReviewTarget.toIntOrNull() ?: 20
                                )

                                navController.popBackStack()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text("Save Changes")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditProfileHeader() {
    Column {
        Text(
            text = "Edit your profile",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Set your English level and daily learning goals.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EditProfileCard(
    content: @Composable ColumnScope.() -> Unit
) {
    androidx.compose.material3.Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}

@Composable
private fun ModernProfileTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        shape = RoundedCornerShape(18.dp)
    )
}