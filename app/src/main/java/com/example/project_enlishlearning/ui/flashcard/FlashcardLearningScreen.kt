package com.example.project_enlishlearning.ui.flashcard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppSectionHeader
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.BottomNavItem
import com.example.project_enlishlearning.ui.components.BottomNavigationBar
import com.example.project_enlishlearning.ui.theme.Accent
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.Error
import com.example.project_enlishlearning.ui.theme.Primary
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import com.example.project_enlishlearning.ui.theme.Secondary
import com.example.project_enlishlearning.ui.theme.Warning
import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.platform.LocalContext
import com.example.project_enlishlearning.viewmodel.VocabularyViewModel
import androidx.compose.runtime.LaunchedEffect
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import com.example.project_enlishlearning.viewmodel.FlashcardViewModel
import com.example.project_enlishlearning.utils.ReviewRating
@Composable
fun FlashcardLearningScreen(
    navController: NavController,
    setId: Int,
    mode: String = "normal",
    viewModel: FlashcardViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    LaunchedEffect(setId, mode) {
        if (mode == "review") {
            viewModel.loadReviewFlashcards(setId)
        } else {
            viewModel.loadFlashcards(setId)
        }
    }


    val uiState by viewModel.uiState.collectAsState()

    val words = uiState.words
    val currentIndex = uiState.currentIndex
    val currentWord = uiState.currentWord
    val flipped = uiState.isFlipped
    val progress = uiState.progress
    LaunchedEffect(uiState.isFinished) {
        if (uiState.isFinished) {
            navController.navigate(
                Screen.FlashcardResult.createRoute(
                    setId = setId,
                    correct = uiState.correctCount,
                    wrong = uiState.wrongCount,
                    total = uiState.totalWords
                )
            )
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...")
        }
        return
    }

    if (words.isEmpty() || currentWord == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Chưa có từ vựng trong bộ này")
        }
        return
    }
    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(520),
        label = ""
    )


    val selectedSrs = remember { mutableStateOf<String?>(null) }
    val density = LocalDensity.current

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Flashcard Learning",
                subtitle = "Review vocabulary using spaced repetition.",
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
                    bottom = AppDimens.BottomBarPadding
                ),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                item {
                    AppCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(320.dp)
                            .graphicsLayer {
                                rotationY = rotation
                                cameraDistance = 12f * density.density
                            }
                            .clickable { viewModel.flipCard() }
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (rotation <= 90f) {
                                FlashcardFront(word = currentWord)
                            } else {
                                FlashcardBack(
                                    word = currentWord,
                                    modifier = Modifier.graphicsLayer { rotationY = 180f }
                                )
                            }
                        }
                    }
                }

                item {
                    AppSectionHeader(title = "How well did you remember?")
                }
                item {

                    Text(
                        text = "${currentIndex + 1}/${words.size}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SrsButton(
                            text = "Again",
                            color = Error,
                            selected = selectedSrs.value == "Again",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedSrs.value = "Again" }
                        )
                        SrsButton(
                            text = "Hard",
                            color = Warning,
                            selected = selectedSrs.value == "Hard",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedSrs.value = "Hard" }
                        )
                    }
                }

                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        SrsButton(
                            text = "Good",
                            color = Secondary,
                            selected = selectedSrs.value == "Good",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedSrs.value = "Good" }
                        )
                        SrsButton(
                            text = "Easy",
                            color = Primary,
                            selected = selectedSrs.value == "Easy",
                            modifier = Modifier.weight(1f),
                            onClick = { selectedSrs.value = "Easy" }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val selected = selectedSrs.value ?: return@Button

                            val rating = when (selected) {
                                "Again" -> ReviewRating.AGAIN
                                "Hard" -> ReviewRating.HARD
                                "Good" -> ReviewRating.GOOD
                                "Easy" -> ReviewRating.EASY
                                else -> return@Button
                            }

                            selectedSrs.value = null
                            viewModel.answerCurrentWord(rating)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        Text(
                            text = "Next Word",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            navController.navigate(
                                Screen.FlashcardResult.createRoute(
                                    setId = setId,
                                    correct = uiState.correctCount,
                                    wrong = uiState.wrongCount,
                                    total = uiState.totalWords
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "Finish Session",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FlashcardFront( word: VocabularyWordEntity) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        ) {
            Icon(
                imageVector = Icons.Default.Replay,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = word.word,
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = word.pronunciation,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tap to flip card",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun FlashcardBack(word: VocabularyWordEntity,
                          modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Meaning",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = word.meaning,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Example",
            style = MaterialTheme.typography.bodyMedium,
            color = Accent,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = word.example,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SrsButton(
    text: String,
    color: Color,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) color else color.copy(alpha = 0.2f),
            contentColor = if (selected) Color.White else color
        )
    ) {
        Text(text = text, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FlashcardLearningPreview() {
    ProjectEnlishLearningTheme {
        FlashcardLearningScreen(navController = rememberNavController(), 3)
    }
}