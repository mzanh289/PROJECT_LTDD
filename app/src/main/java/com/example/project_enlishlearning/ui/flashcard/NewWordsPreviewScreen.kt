package com.example.project_enlishlearning.ui.flashcard

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.project_enlishlearning.viewmodel.VocabularyViewModel

@Composable
fun NewWordsPreviewScreen(
    navController: NavController,
    setId: Int, // Bắt buộc nhận ID của bộ từ vựng
    viewModel: VocabularyViewModel = viewModel( // Khởi tạo ViewModel với Application context
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    // 1. Tự động tải dữ liệu từ Database khi mở màn hình này
    LaunchedEffect(setId) {
        viewModel.loadWordsForSet(setId)
    }

    // 2. Lắng nghe danh sách từ vựng được cập nhật
    val words by viewModel.wordsInSet.collectAsState()

    Scaffold(
        contentWindowInsets = WindowInsets.systemBars,
        topBar = {
            AppToolbar(
                title = "Today's New Words",
                subtitle = "Preview vocabulary before starting flashcard learning.",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            Surface(tonalElevation = 2.dp) {
                PrimaryButton(
                    text = "Start Flashcard Session",
                    onClick = {
                        // Nhớ truyền tiếp setId sang màn hình FlashcardLearning nếu màn hình đó cũng cần tải Database nhé
                        navController.navigate("${Screen.FlashcardLearning.route}/$setId")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(AppDimens.ScreenPadding)
                        .navigationBarsPadding()
                )
            }
        }
    ) { innerPadding ->
        AppGradientBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = AppDimens.ScreenPadding,
                    end = AppDimens.ScreenPadding,
                    top = 12.dp,
                    bottom = innerPadding.calculateBottomPadding() + 16.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    // Truyền tổng số từ vựng lấy được vào thẻ báo cáo
                    DailyGoalCard(totalWords = words.size)
                }

                // Hiển thị từng từ vựng bằng dữ liệu từ Database
                items(words, key = { it.wordId }) { item ->
                    FlashcardPreviewCard(item)
                }
            }
        }
    }
}

@Composable
private fun DailyGoalCard(totalWords: Int) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(AppDimens.CardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Daily Goal",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$totalWords New Words", // Hiển thị số lượng linh hoạt
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
private fun FlashcardPreviewCard(item: VocabularyWordEntity) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.word,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.pronunciation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Meaning",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.meaning,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Example",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = item.example,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewWordsPreviewScreenPreview() {
    ProjectEnlishLearningTheme {
        // Truyền tạm setId = 1 cho Preview không bị báo lỗi
        NewWordsPreviewScreen(navController = rememberNavController(), setId = 1)
    }
}