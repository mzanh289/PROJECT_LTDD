package com.example.project_enlishlearning.ui.flashcard

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.PrimaryButton
import com.example.project_enlishlearning.ui.components.SecondaryButton
import com.example.project_enlishlearning.ui.theme.Accent
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.Primary
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import com.example.project_enlishlearning.ui.theme.Secondary

@Composable
fun FlashcardResultScreen(
	navController: NavController,
	setId: Int,
	correct: Int,
	wrong: Int,
	total: Int
) {
	val progress = if (total == 0) 0f else correct.toFloat() / total.toFloat()
	val percent = (progress * 100).toInt()
	Scaffold(
		topBar = {
			AppToolbar(
				title = "Session Completed",
				subtitle = "Great job reviewing today's flashcards.",
				navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
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
					bottom = AppDimens.SectionSpacing
				),
				verticalArrangement = Arrangement.spacedBy(16.dp)
			) {
				item {
					ScoreBadge(progress = progress)
				}

				item {
					Text(
						text = "Session Completed",
						style = MaterialTheme.typography.headlineMedium,
						color = MaterialTheme.colorScheme.onBackground
					)
					Spacer(modifier = Modifier.height(8.dp))
					Text(
						text = "Great job! You reviewed today's flashcards successfully.",
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant,
						textAlign = TextAlign.Center
					)
				}

				item {
					Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
						ResultStatCard(
							title = "Remembered",
							value = "$correct",
							color = Secondary,
							modifier = Modifier.weight(1f)
						)

						ResultStatCard(
							title = "Need Review",
							value = "$wrong",
							color = Accent,
							modifier = Modifier.weight(1f)
						)
					}
				}

				item {
					Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
						ResultStatCard(
							title = "Total Words",
							value = "$total",
							color = Primary,
							modifier = Modifier.weight(1f)
						)

						ResultStatCard(
							title = "Score",
							value = "$percent%",
							color = Color(0xFFEC4899),
							modifier = Modifier.weight(1f)
						)
					}
				}

				item {
					AppCard(modifier = Modifier.fillMaxWidth()) {
						Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
							Text(
								text = "Session Summary",
								style = MaterialTheme.typography.titleMedium,
								fontWeight = FontWeight.Bold
							)

							Spacer(modifier = Modifier.height(12.dp))

							Text(
								text = "You remembered $correct out of $total words.",
								style = MaterialTheme.typography.bodyMedium,
								color = MaterialTheme.colorScheme.onSurfaceVariant
							)

							Spacer(modifier = Modifier.height(8.dp))

							Text(
								text = "$wrong words need more practice.",
								style = MaterialTheme.typography.bodyMedium,
								color = MaterialTheme.colorScheme.primary
							)
						}
					}
				}

				item {
					PrimaryButton(
						text = "Review Difficult Words",
						onClick = {
							navController.navigate(Screen.ReviewVocabulary.createRoute(setId))
						},
						modifier = Modifier.fillMaxWidth()
					)
				}

				item {
					SecondaryButton(
						text = "Continue Learning",
						onClick = {
							navController.navigate(Screen.VocabularySetList.route)
						},
						modifier = Modifier.fillMaxWidth()
					)
				}
			}
		}
	}
}

@Composable
private fun ScoreBadge(progress: Float = 0.92f) {

	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {

		Box(contentAlignment = Alignment.Center) {

			CircularProgressIndicator(
				progress = { progress },
				modifier = Modifier.size(120.dp),
				strokeWidth = 10.dp,
				color = Color(0xFF4CAF50),
				trackColor = Color(0xFF81C784).copy(alpha = 0.3f)
			)

			Text(
				text = "${(progress * 100).toInt()}%",
				color = Color.Black,
				style = MaterialTheme.typography.displaySmall
			)
		}
	}
}

@Composable
private fun ResultStatCard(
	title: String,
	value: String,
	color: Color,
	modifier: Modifier = Modifier
) {
	AppCard(modifier = modifier) {
		Column(
			modifier = Modifier.padding(AppDimens.CardPadding),
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = value,
				style = MaterialTheme.typography.titleLarge,
				color = color
			)
			Spacer(modifier = Modifier.height(6.dp))
			Text(
				text = title,
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}

@Composable
private fun ReviewScheduleItem(
	word: String,
	nextReview: String
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically
	) {
		Text(text = word, style = MaterialTheme.typography.bodyMedium)
		Text(
			text = nextReview,
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.primary
		)
	}
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun FlashcardResultScreenPreview() {
//	ProjectEnlishLearningTheme {
//		FlashcardResultScreen(navController = rememberNavController())
//	}
//}

