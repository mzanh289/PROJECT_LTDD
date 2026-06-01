package com.example.project_enlishlearning.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
fun ProfileScreen(
	navController: NavController,
	authViewModel: AuthViewModel,
	profileViewModel: ProfileViewModel,
	onEditProfileClick: () -> Unit,
	onLogoutClick: () -> Unit,
	selected: BottomNavItem = BottomNavItem.Profile,
	onBottomItemSelected: (BottomNavItem) -> Unit = {},
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

	val levelProgress = getLevelProgress(englishLevel)

	Scaffold(
		topBar = {
			AppToolbar(
				title = "Profile",
				subtitle = "Manage your learning profile and goals.",
				navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
				onNavigationClick = { navController.popBackStack() }
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
					ProfileHeroCard(
						displayName = displayName,
						email = email,
						englishLevel = englishLevel
					)
				}

				item {
					LevelProgressCard(
						englishLevel = englishLevel,
						progress = levelProgress
					)
				}

				item {
					LearningGoalCard(
						learningGoal = learningGoal,
						dailyNewWordTarget = dailyNewWordTarget,
						dailyReviewTarget = dailyReviewTarget
					)
				}

				item {
					ProfileInfoCard(
						email = email,
						englishLevel = englishLevel,
						learningGoal = learningGoal
					)
				}

				item {
					ProfileActionsCard(
						onEditClick = onEditProfileClick,
						onLogoutClick = {
							authViewModel.logout {
								onLogoutClick()
							}
						}
					)
				}
			}
		}
	}
}

@Composable
private fun ProfileHeroCard(
	displayName: String,
	email: String,
	englishLevel: String
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(30.dp),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.primary
		),
		elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(22.dp),
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				modifier = Modifier
					.size(72.dp)
					.clip(CircleShape)
					.background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f)),
				contentAlignment = Alignment.Center
			) {
				Icon(
					imageVector = Icons.Default.Person,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.onPrimary,
					modifier = Modifier.size(38.dp)
				)
			}

			Spacer(modifier = Modifier.width(16.dp))

			Column(
				modifier = Modifier.weight(1f)
			) {
				Text(
					text = if (displayName.isBlank()) "No name" else displayName,
					style = MaterialTheme.typography.titleLarge,
					color = MaterialTheme.colorScheme.onPrimary,
					fontWeight = FontWeight.Bold
				)

				Spacer(modifier = Modifier.height(4.dp))

				Text(
					text = if (email.isBlank()) "No email" else email,
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
				)

				Spacer(modifier = Modifier.height(10.dp))

				Box(
					modifier = Modifier
						.background(
							color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.16f),
							shape = RoundedCornerShape(50.dp)
						)
						.padding(horizontal = 12.dp, vertical = 6.dp)
				) {
					Text(
						text = "Level $englishLevel",
						style = MaterialTheme.typography.labelLarge,
						color = MaterialTheme.colorScheme.onPrimary
					)
				}
			}
		}
	}
}

@Composable
private fun LevelProgressCard(
	englishLevel: String,
	progress: Float
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(28.dp),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Column(
			modifier = Modifier.padding(20.dp),
			verticalArrangement = Arrangement.spacedBy(14.dp)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					imageVector = Icons.Default.School,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.primary
				)

				Spacer(modifier = Modifier.width(10.dp))

				Text(
					text = "English Level",
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.SemiBold
				)
			}

			Text(
				text = getLevelTitle(englishLevel),
				style = MaterialTheme.typography.headlineSmall,
				color = MaterialTheme.colorScheme.primary,
				fontWeight = FontWeight.Bold
			)

			LinearProgressIndicator(
				progress = { progress },
				modifier = Modifier
					.fillMaxWidth()
					.height(10.dp),
				trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
			)

			Text(
				text = getLevelDescription(englishLevel),
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}

@Composable
private fun LearningGoalCard(
	learningGoal: String,
	dailyNewWordTarget: String,
	dailyReviewTarget: String
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(28.dp),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Column(
			modifier = Modifier.padding(20.dp),
			verticalArrangement = Arrangement.spacedBy(14.dp)
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically
			) {
				Icon(
					imageVector = Icons.Default.TrackChanges,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.primary
				)

				Spacer(modifier = Modifier.width(10.dp))

				Text(
					text = "Learning Goal",
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.SemiBold
				)
			}

			Text(
				text = if (learningGoal.isBlank()) "No goal set yet" else learningGoal,
				style = MaterialTheme.typography.bodyLarge
			)

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(12.dp)
			) {
				GoalMiniCard(
					title = "New Words",
					value = dailyNewWordTarget,
					icon = Icons.Default.Flag,
					modifier = Modifier.weight(1f)
				)

				GoalMiniCard(
					title = "Reviews",
					value = dailyReviewTarget,
					icon = Icons.Default.Timer,
					modifier = Modifier.weight(1f)
				)
			}
		}
	}
}

@Composable
private fun GoalMiniCard(
	title: String,
	value: String,
	icon: ImageVector,
	modifier: Modifier = Modifier
) {
	Box(
		modifier = modifier
			.background(
				color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
				shape = RoundedCornerShape(20.dp)
			)
			.padding(14.dp)
	) {
		Column {
			Icon(
				imageVector = icon,
				contentDescription = null,
				tint = MaterialTheme.colorScheme.primary
			)

			Spacer(modifier = Modifier.height(10.dp))

			Text(
				text = value,
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.Bold
			)

			Text(
				text = "$title/day",
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}

@Composable
private fun ProfileInfoCard(
	email: String,
	englishLevel: String,
	learningGoal: String
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(28.dp),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Column(
			modifier = Modifier.padding(20.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp)
		) {
			Text(
				text = "Account Information",
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.SemiBold
			)

			InfoRow(
				icon = Icons.Default.Email,
				title = "Email",
				value = if (email.isBlank()) "No email" else email
			)

			InfoRow(
				icon = Icons.Default.School,
				title = "Level",
				value = englishLevel
			)

			InfoRow(
				icon = Icons.Default.TrackChanges,
				title = "Goal",
				value = if (learningGoal.isBlank()) "No goal" else learningGoal
			)
		}
	}
}

@Composable
private fun InfoRow(
	icon: ImageVector,
	title: String,
	value: String
) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically
	) {
		Box(
			modifier = Modifier
				.background(
					color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
					shape = RoundedCornerShape(16.dp)
				)
				.padding(10.dp)
		) {
			Icon(
				imageVector = icon,
				contentDescription = null,
				tint = MaterialTheme.colorScheme.primary
			)
		}

		Spacer(modifier = Modifier.width(14.dp))

		Column(
			modifier = Modifier.weight(1f)
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.bodySmall,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)

			Text(
				text = value,
				style = MaterialTheme.typography.bodyLarge,
				fontWeight = FontWeight.Medium
			)
		}
	}
}

@Composable
private fun ProfileActionsCard(
	onEditClick: () -> Unit,
	onLogoutClick: () -> Unit
) {
	Card(
		modifier = Modifier.fillMaxWidth(),
		shape = RoundedCornerShape(28.dp),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
	) {
		Column(
			modifier = Modifier.padding(20.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp)
		) {
			Text(
				text = "Actions",
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.SemiBold
			)

			Button(
				onClick = onEditClick,
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(18.dp)
			) {
				Icon(
					imageVector = Icons.Default.Edit,
					contentDescription = null
				)

				Spacer(modifier = Modifier.width(8.dp))

				Text("Edit Profile")
			}

			OutlinedButton(
				onClick = onLogoutClick,
				modifier = Modifier.fillMaxWidth(),
				shape = RoundedCornerShape(18.dp),
				colors = ButtonDefaults.outlinedButtonColors(
					contentColor = MaterialTheme.colorScheme.error
				)
			) {
				Icon(
					imageVector = Icons.Default.Logout,
					contentDescription = null
				)

				Spacer(modifier = Modifier.width(8.dp))

				Text("Logout")
			}
		}
	}
}

private fun getLevelProgress(level: String): Float {
	return when (level.uppercase()) {
		"A1" -> 0.15f
		"A2" -> 0.30f
		"B1" -> 0.50f
		"B2" -> 0.68f
		"C1" -> 0.85f
		"C2" -> 1.0f
		else -> 0.15f
	}
}

private fun getLevelTitle(level: String): String {
	return when (level.uppercase()) {
		"A1" -> "Beginner"
		"A2" -> "Elementary"
		"B1" -> "Intermediate"
		"B2" -> "Upper Intermediate"
		"C1" -> "Advanced"
		"C2" -> "Proficient"
		else -> "Beginner"
	}
}

private fun getLevelDescription(level: String): String {
	return when (level.uppercase()) {
		"A1" -> "You are starting with basic English words and phrases."
		"A2" -> "You can understand simple sentences and common expressions."
		"B1" -> "You can communicate in daily situations with confidence."
		"B2" -> "You can understand complex texts and express ideas clearly."
		"C1" -> "You can use English fluently in study and work contexts."
		"C2" -> "You can understand and use English almost like a native speaker."
		else -> "You are starting with basic English words and phrases."
	}
}