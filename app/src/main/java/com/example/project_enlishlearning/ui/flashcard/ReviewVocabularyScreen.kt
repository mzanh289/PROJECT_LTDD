package com.example.project_enlishlearning.ui.flashcard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.ui.components.*
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme

data class ReviewWord(
    val word: String,
    val meaning: String,
    val dueLevel: String, // Again / Hard / Good / Easy
    val nextReview: String
)

@Composable
fun ReviewVocabularyScreen(
    navController: NavController
) {
    val reviewWords = remember {
        listOf(
            ReviewWord("Acquire", "To gain or obtain something", "Hard", "Tomorrow"),
            ReviewWord("Determine", "To decide or establish", "Good", "In 3 days"),
            ReviewWord("Significant", "Important or meaningful", "Again", "Today")
        )
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Review Vocabulary",
                subtitle = "Spaced repetition learning session",
                navigationIcon = Icons.Default.Replay,
                onNavigationClick = { navController.popBackStack() }
            )
        }
    ) { padding ->

        AppGradientBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppDimens.ScreenPadding)
            ) {

                // ================= HEADER CARD =================
                AppCard(modifier = Modifier.fillMaxWidth()) {

                    Column(modifier = Modifier.padding(AppDimens.CardPadding)) {

                        Text(
                            text = "Today's Review",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "You have ${reviewWords.size} words to review today.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PrimaryButton(
                            text = "Start Review",
                            onClick = {
                                navController.navigate(Screen.FlashcardLearning.route)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // ================= LIST =================
                Text(
                    text = "Due Words",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(reviewWords) { item ->
                        ReviewWordCard(item)
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewWordCard(item: ReviewWord) {

    AppCard(modifier = Modifier.fillMaxWidth()) {

        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = item.word,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when (item.dueLevel) {
                        "Again" -> Color(0xFFEF4444)
                        "Hard" -> Color(0xFFF59E0B)
                        "Good" -> Color(0xFF10B981)
                        else -> Color(0xFF4F46E5)
                    }
                ) {
                    Text(
                        text = item.dueLevel,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        color = Color.White,
                        fontSize = MaterialTheme.typography.labelSmall.fontSize
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.meaning,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Next review: ${item.nextReview}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReviewVocabularyScreenPreview() {
    ProjectEnlishLearningTheme {
        ReviewVocabularyScreen(
            navController = rememberNavController()
        )
    }
}

