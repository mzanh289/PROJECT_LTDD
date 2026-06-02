package com.example.project_enlishlearning.ui.flashcard

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.PrimaryButton
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import com.example.project_enlishlearning.viewmodel.ReviewVocabularyViewModel

@Composable
fun ReviewVocabularyScreen(
    navController: NavController,
    setId: Int,
    viewModel: ReviewVocabularyViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    LaunchedEffect(setId) {
        viewModel.loadDifficultWords(setId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val reviewWords = uiState.difficultWords

    val isDueReviewMode = setId == Screen.ReviewVocabulary.GLOBAL_DUE_REVIEW_SET_ID
    val title = if (isDueReviewMode) "Global Due Review" else "Review Vocabulary"
    val subtitle = if (isDueReviewMode) "Review all due words across all sets" else "Spaced repetition learning session"

    Scaffold(
        topBar = {
            AppToolbar(
                title = title,
                subtitle = subtitle,
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

                AppCard(modifier = Modifier.fillMaxWidth()) {

                    Column(modifier = Modifier.padding(AppDimens.CardPadding)) {

                        Text(
                            text = if (isDueReviewMode) "Due Review" else "Today's Review",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isDueReviewMode) {
                                "You have ${reviewWords.size} words due for review."
                            } else {
                                "You have ${reviewWords.size} words to review today."
                            },
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        PrimaryButton(
                            text = "Start Review",
                            onClick = {
                                navController.navigate(
                                    Screen.FlashcardLearning.createRoute(
                                        setId = setId,
                                        mode = "review"
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Due Words",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else if (reviewWords.isEmpty()) {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "No difficult words right now. Nice work!",
                            modifier = Modifier.padding(AppDimens.CardPadding),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 80.dp)
                    ) {
                        items(reviewWords, key = { it.wordId }) { item ->
                            ReviewWordCard(item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewWordCard(item: VocabularyWordEntity) {

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
                    color = when (item.status) {
                        "LEARNING" -> Color(0xFFF59E0B)
                        "REVIEWING" -> Color(0xFF10B981)
                        "MASTERED" -> Color(0xFF4F46E5)
                        else -> Color(0xFFEF4444)
                    }
                ) {
                    Text(
                        text = item.status,
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
                text = item.example,
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
            navController = rememberNavController(),
            setId = 1
        )
    }
}