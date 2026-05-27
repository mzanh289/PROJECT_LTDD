package com.example.project_enlishlearning.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppSectionHeader
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.BottomNavItem
import com.example.project_enlishlearning.ui.components.BottomNavigationBar
import com.example.project_enlishlearning.ui.theme.Accent
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import com.example.project_enlishlearning.ui.theme.Secondary
import androidx.compose.foundation.layout.width

@Composable
fun ProfileScreen(
	selected: BottomNavItem = BottomNavItem.Profile,
	onBottomItemSelected: (BottomNavItem) -> Unit = {}
) {
    val primaryColor = MaterialTheme.colorScheme.primary

    val stats = remember(primaryColor) {
        listOf(
            ProfileStat("Streak", "18 Days", Icons.Default.LocalFireDepartment, Accent),
            ProfileStat("Words", "1,245", Icons.Default.School, Secondary),
            ProfileStat("Level", "B1", Icons.Default.Timeline, primaryColor)
        )
    }

	Scaffold(
		topBar = {
			AppToolbar(
				title = "Profile",
				subtitle = "Track your learning goals and progress."
			)
		},
		bottomBar = {
			BottomNavigationBar(
				selected = selected,
				onItemSelected = onBottomItemSelected
			)
		}
	) { innerPadding ->
		AppGradientBackground(
			modifier = Modifier
				.fillMaxSize()
				.padding(innerPadding)
		) {
			LazyColumn(
				modifier = Modifier.fillMaxSize(),
				contentPadding = androidx.compose.foundation.layout.PaddingValues(
					start = AppDimens.ScreenPadding,
					end = AppDimens.ScreenPadding,
					top = 12.dp,
					bottom = AppDimens.BottomBarPadding
				),
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				item {
					ProfileHeader()
				}

				item {
					AppSectionHeader(title = "Learning Target")
					Spacer(modifier = Modifier.height(12.dp))
					LearningTargetCard()
				}

				item {
					AppSectionHeader(title = "Stats")
					Spacer(modifier = Modifier.height(12.dp))
					Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
						stats.forEach { stat ->
							ProfileStatCard(stat, Modifier.weight(1f))
						}
					}
				}

				item {
					AppSectionHeader(title = "Achievements")
					Spacer(modifier = Modifier.height(12.dp))
					AchievementCard()
				}
			}
		}
	}
}

@Composable
private fun ProfileHeader() {
	AppCard(modifier = Modifier.fillMaxWidth()) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(AppDimens.CardPadding),
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				modifier = Modifier
					.size(72.dp)
					.background(
						MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
						CircleShape
					),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = "L",
					style = MaterialTheme.typography.displaySmall,
					color = MaterialTheme.colorScheme.primary
				)
			}
			Spacer(modifier = Modifier.width(16.dp))
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = "Linh Nguyen",
					style = MaterialTheme.typography.titleLarge
				)
				Spacer(modifier = Modifier.height(4.dp))
				Text(
					text = "Level B1 - Intermediate",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
	}
}

@Composable
private fun LearningTargetCard() {
	AppCard(modifier = Modifier.fillMaxWidth()) {
		Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
			Text(
				text = "Daily goal: 20 minutes",
				style = MaterialTheme.typography.titleMedium
			)
			Spacer(modifier = Modifier.height(6.dp))
			Text(
				text = "You're on track to reach 5 hours this week.",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
			Spacer(modifier = Modifier.height(14.dp))
			LinearProgressIndicator(
				progress = { 0.72f },
				modifier = Modifier
					.fillMaxWidth()
					.height(8.dp),
				color = MaterialTheme.colorScheme.primary,
				trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
			)
			Spacer(modifier = Modifier.height(6.dp))
			Text(
				text = "72% weekly target completed",
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}

@Composable
private fun ProfileStatCard(stat: ProfileStat, modifier: Modifier = Modifier) {
	AppCard(modifier = modifier) {
		Column(
			modifier = Modifier.padding(AppDimens.CardPadding),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Icon(
				imageVector = stat.icon,
				contentDescription = null,
				tint = stat.tint
			)
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = stat.value,
				style = MaterialTheme.typography.titleLarge
			)
			Text(
				text = stat.label,
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}

@Composable
private fun AchievementCard() {
	AppCard(modifier = Modifier.fillMaxWidth()) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(AppDimens.CardPadding),
			verticalAlignment = Alignment.CenterVertically
		) {
			Box(
				modifier = Modifier
					.size(52.dp)
					.background(
						MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
						CircleShape
					),
				contentAlignment = Alignment.Center
			) {
				Icon(
					imageVector = Icons.Default.EmojiEvents,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.primary
				)
			}
			Spacer(modifier = Modifier.width(12.dp))
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = "Vocabulary Master",
					style = MaterialTheme.typography.titleMedium
				)
				Spacer(modifier = Modifier.height(4.dp))
				Text(
					text = "Completed 10 vocabulary sets",
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant
				)
			}
		}
	}
}

private data class ProfileStat(
	val label: String,
	val value: String,
	val icon: androidx.compose.ui.graphics.vector.ImageVector,
	val tint: Color
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
	ProjectEnlishLearningTheme {
		ProfileScreen()
	}
}

