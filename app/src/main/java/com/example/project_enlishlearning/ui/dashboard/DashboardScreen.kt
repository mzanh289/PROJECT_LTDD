package com.example.project_enlishlearning.ui.dashboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project_enlishlearning.viewmodel.AuthViewModel
import com.example.project_enlishlearning.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onProfileClick: () -> Unit,
    onVocabularyClick: () -> Unit,
    onFlashcardClick: () -> Unit
) {
    val totalVocabularySets by viewModel.totalVocabularySets.collectAsState()
    val totalVocabularyWords by viewModel.totalVocabularyWords.collectAsState()
    val totalLearningWords by viewModel.totalLearningWords.collectAsState()
    val newWords by viewModel.newWords.collectAsState()
    val learningWords by viewModel.learningWords.collectAsState()
    val reviewingWords by viewModel.reviewingWords.collectAsState()
    val masteredWords by viewModel.masteredWords.collectAsState()
    val reviewDueToday by viewModel.reviewDueToday.collectAsState()

    val totalProgressWords = newWords + learningWords + reviewingWords + masteredWords
    val masteredProgress = if (totalProgressWords > 0) {
        masteredWords.toFloat() / totalProgressWords.toFloat()
    } else {
        0f
    }

    Scaffold { padding ->

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
                    DashboardHeader()
                }

                item {
                    TodayReviewCard(
                        reviewDueToday = reviewDueToday,
                        onStartClick = onFlashcardClick
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ModernStatCard(
                            title = "Sets",
                            value = totalVocabularySets.toString(),
                            icon = Icons.Default.Book,
                            modifier = Modifier.weight(1f)
                        )

                        ModernStatCard(
                            title = "Words",
                            value = totalVocabularyWords.toString(),
                            icon = Icons.Default.MenuBook,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ModernStatCard(
                            title = "Learning",
                            value = totalLearningWords.toString(),
                            icon = Icons.Default.School,
                            modifier = Modifier.weight(1f)
                        )

                        ModernStatCard(
                            title = "Mastered",
                            value = masteredWords.toString(),
                            icon = Icons.Default.CheckCircle,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    ProgressOverviewCard(
                        newWords = newWords,
                        learningWords = learningWords,
                        reviewingWords = reviewingWords,
                        masteredWords = masteredWords,
                        masteredProgress = masteredProgress
                    )
                }

                item {
                    QuickActionsCard(
                        onFlashcardClick = onFlashcardClick,
                        onVocabularyClick = onVocabularyClick,
                        onProfileClick = onProfileClick
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardHeader() {
    Column {
        Text(
            text = "Dashboard",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Ready to improve your English today?",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TodayReviewCard(
    reviewDueToday: Int,
    onStartClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(22.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timer,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Today's Review",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "$reviewDueToday words due",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Start Learning")
            }
        }
    }
}

@Composable
private fun ModernStatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
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

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProgressOverviewCard(
    newWords: Int,
    learningWords: Int,
    reviewingWords: Int,
    masteredWords: Int,
    masteredProgress: Float
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
            Text(
                text = "Vocabulary Progress",
                style = MaterialTheme.typography.titleLarge
            )

            LinearProgressIndicator(
                progress = { masteredProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp),
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProgressText("New", newWords)
                ProgressText("Learning", learningWords)
                ProgressText("Reviewing", reviewingWords)
                ProgressText("Mastered", masteredWords)
            }
        }
    }
}

@Composable
private fun ProgressText(
    title: String,
    value: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuickActionsCard(
    onFlashcardClick: () -> Unit,
    onVocabularyClick: () -> Unit,
    onProfileClick: () -> Unit
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
                text = "Quick Actions",
                style = MaterialTheme.typography.titleLarge
            )

            Button(
                onClick = onFlashcardClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Start Flashcard")
            }

            Button(
                onClick = onVocabularyClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Manage Vocabulary")
            }

            Button(
                onClick = onProfileClick,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Profile")
            }
        }
    }
}
