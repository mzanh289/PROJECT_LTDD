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
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.BottomNavItem
import com.example.project_enlishlearning.ui.components.BottomNavigationBar
import com.example.project_enlishlearning.viewmodel.AuthViewModel
import com.example.project_enlishlearning.viewmodel.ProfileViewModel

@Composable
fun EditProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    selected: BottomNavItem = BottomNavItem.Profile,
    onBottomItemSelected: (BottomNavItem) -> Unit = {}
) {
    val profile by profileViewModel.profile.collectAsState()

    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(authViewModel.getCurrentUserEmail()) }
    var englishLevel by remember { mutableStateOf("A1") }
    var learningGoal by remember { mutableStateOf("") }
    var dailyNewWordTarget by remember { mutableStateOf("10") }
    var dailyReviewTarget by remember { mutableStateOf("20") }

    var displayNameError by remember { mutableStateOf<String?>(null) }
    var englishLevelError by remember { mutableStateOf<String?>(null) }
    var learningGoalError by remember { mutableStateOf<String?>(null) }
    var dailyNewWordTargetError by remember { mutableStateOf<String?>(null) }
    var dailyReviewTargetError by remember { mutableStateOf<String?>(null) }

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
        },
        bottomBar = {
            BottomNavigationBar(
                selected = selected,
                onItemSelected = onBottomItemSelected
            )
        }

    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
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
                            onValueChange = {
                                displayName = it
                                displayNameError = null
                            },
                            label = "Full Name",
                            icon = Icons.Default.Person,
                            errorMessage = displayNameError
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
                            onValueChange = {
                                englishLevel = it.uppercase()
                                englishLevelError = null
                            },
                            label = "English Level",
                            icon = Icons.Default.School,
                            errorMessage = englishLevelError
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ModernProfileTextField(
                            value = learningGoal,
                            onValueChange = {
                                learningGoal = it
                                learningGoalError = null
                            },
                            label = "Learning Goal",
                            icon = Icons.Default.Flag,
                            errorMessage = learningGoalError
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ModernProfileTextField(
                            value = dailyNewWordTarget,
                            onValueChange = {
                                dailyNewWordTarget = it
                                dailyNewWordTargetError = null
                            },
                            label = "New Words Per Day",
                            icon = Icons.Default.Timer,
                            errorMessage = dailyNewWordTargetError
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ModernProfileTextField(
                            value = dailyReviewTarget,
                            onValueChange = {
                                dailyReviewTarget = it
                                dailyReviewTargetError = null
                            },
                            label = "Reviews Per Day",
                            icon = Icons.Default.Timer,
                            errorMessage = dailyReviewTargetError
                        )

                        Spacer(modifier = Modifier.height(22.dp))

                        Button(
                            onClick = {
                                val validLevels = listOf("A1", "A2", "B1", "B2", "C1", "C2")

                                val newWordTarget = dailyNewWordTarget.toIntOrNull()
                                val reviewTarget = dailyReviewTarget.toIntOrNull()

                                displayNameError = null
                                englishLevelError = null
                                learningGoalError = null
                                dailyNewWordTargetError = null
                                dailyReviewTargetError = null

                                var isValid = true

                                if (displayName.trim().isEmpty()) {
                                    displayNameError = "Full name cannot be empty"
                                    isValid = false
                                }

                                if (englishLevel.trim().uppercase() !in validLevels) {
                                    englishLevelError = "Level must be A1, A2, B1, B2, C1 or C2"
                                    isValid = false
                                }

                                if (learningGoal.trim().isEmpty()) {
                                    learningGoalError = "Learning goal cannot be empty"
                                    isValid = false
                                }

                                if (newWordTarget == null || newWordTarget <= 0) {
                                    dailyNewWordTargetError = "New words per day must be greater than 0"
                                    isValid = false
                                }

                                if (reviewTarget == null || reviewTarget <= 0) {
                                    dailyReviewTargetError = "Reviews per day must be greater than 0"
                                    isValid = false
                                }

                                if (isValid) {
                                    profileViewModel.saveProfile(
                                        email = email,
                                        displayName = displayName.trim(),
                                        englishLevel = englishLevel.trim().uppercase(),
                                        learningGoal = learningGoal.trim(),
                                        dailyNewWordTarget = newWordTarget ?: 10,
                                        dailyReviewTarget = reviewTarget ?: 20
                                    )

                                    navController.popBackStack()
                                }
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
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
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
    enabled: Boolean = true,
    errorMessage: String? = null
) {
    Column {
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
            shape = RoundedCornerShape(18.dp),
            isError = errorMessage != null
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}