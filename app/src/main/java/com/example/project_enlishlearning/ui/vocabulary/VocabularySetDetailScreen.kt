package com.example.project_enlishlearning.ui.vocabulary

import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppTagChip
import com.example.project_enlishlearning.ui.components.AppTextField
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.BottomNavItem
import com.example.project_enlishlearning.ui.components.BottomNavigationBar
import com.example.project_enlishlearning.ui.components.PrimaryButton
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.GradientEnd
import com.example.project_enlishlearning.ui.theme.GradientStart
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import com.example.project_enlishlearning.ui.theme.Success
import com.example.project_enlishlearning.ui.theme.Warning

enum class VocabularyStatus(val label: String) {
    New("New"),
    Learning("Learning"),
    Mastered("Mastered")
}

data class VocabularySetDetailUi(
    val title: String,
    val description: String,
    val tags: List<String>,
    val totalWords: Int,
    val progress: Int
)

data class VocabularyWordUi(
    val word: String,
    val pronunciation: String,
    val meaning: String,
    val example: String,
    val status: VocabularyStatus,
    val isFavorite: Boolean
)

@Composable
fun VocabularySetDetailScreen(
    navController: NavController,
    selected: BottomNavItem = BottomNavItem.Vocabulary,
    onBottomItemSelected: (BottomNavItem) -> Unit = {},
    set: VocabularySetDetailUi = sampleVocabularySet,
    initialWords: List<VocabularyWordUi> = sampleVocabularyWords,
    onAddVocabulary: () -> Unit = { navController.navigate(Screen.AddVocabulary.route) }
) {
    var searchQuery by remember { mutableStateOf("") }
    val words = remember { mutableStateListOf(*initialWords.toTypedArray()) }

    Scaffold(
        topBar = {
            AppToolbar(
                title = set.title,
                subtitle = "Vocabulary set details",
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
                    SetHeaderCard(set = set)
                }

                item {
                    PrimaryButton(
                        text = "Add Vocabulary",
                        onClick = onAddVocabulary,
                        leadingIcon = Icons.Default.Add,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    VocabularySearchBar(
                        value = searchQuery,
                        onValueChange = { searchQuery = it }
                    )
                }

                val filteredWords = words.filter {
                    it.word.contains(searchQuery, ignoreCase = true) ||
                        it.meaning.contains(searchQuery, ignoreCase = true)
                }

                if (filteredWords.isEmpty()) {
                    item {
                        EmptyStateView(onAddVocabulary = onAddVocabulary)
                    }
                } else {
                    itemsIndexed(filteredWords, key = { _, item -> item.word }) { index, item ->
                        VocabularyItemCard(
                            word = item,
                            onFavoriteToggle = {
                                val currentIndex = words.indexOfFirst { it.word == item.word }
                                if (currentIndex >= 0) {
                                    val updated = words[currentIndex].copy(
                                        isFavorite = !words[currentIndex].isFavorite
                                    )
                                    words[currentIndex] = updated
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SetHeaderCard(
    set: VocabularySetDetailUi
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            GradientStart.copy(alpha = 0.9f),
                            GradientEnd.copy(alpha = 0.6f)
                        )
                    )
                )
                .padding(AppDimens.CardPadding)
        ) {
            Text(
                text = set.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = set.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(14.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                set.tags.forEach { tag ->
                    AppTagChip(text = tag)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${set.totalWords} words",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${set.progress}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { set.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
fun VocabularySearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        label = "Search",
        placeholder = "Search words...",
        leadingIcon = Icons.Default.Search
    )
}

@Composable
fun VocabularyItemCard(
    word: VocabularyWordUi,
    onFavoriteToggle: () -> Unit
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = word.word,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = word.pronunciation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onFavoriteToggle) {
                    Icon(
                        imageVector = if (word.isFavorite) {
                            Icons.Filled.Favorite
                        } else {
                            Icons.Outlined.FavoriteBorder
                        },
                        contentDescription = null,
                        tint = if (word.isFavorite) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }

                IconButton(onClick = { }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = word.meaning,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = word.example,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            StatusChip(status = word.status)
        }
    }
}

@Composable
fun EmptyStateView(
    onAddVocabulary: () -> Unit
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(AppDimens.CardPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "No vocabulary in this set",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Add your first word to start learning.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Add first word",
                onClick = onAddVocabulary,
                leadingIcon = Icons.Default.Add,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StatusChip(status: VocabularyStatus) {
    val color = when (status) {
        VocabularyStatus.New -> MaterialTheme.colorScheme.primary
        VocabularyStatus.Learning -> Warning
        VocabularyStatus.Mastered -> Success
    }

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = status.label,
            style = MaterialTheme.typography.labelLarge,
            color = color
        )
    }
}

private val sampleVocabularySet = VocabularySetDetailUi(
    title = "IELTS Academic Vocabulary",
    description = "Advanced academic terms for IELTS Reading and Writing tasks.",
    tags = listOf("IELTS", "Academic", "Exam"),
    totalWords = 96,
    progress = 68
)

private val sampleVocabularyWords = listOf(
    VocabularyWordUi(
        word = "Acquire",
        pronunciation = "/əˈkwaɪər/",
        meaning = "To obtain or gain something.",
        example = "She acquired new skills through daily practice.",
        status = VocabularyStatus.New,
        isFavorite = true
    ),
    VocabularyWordUi(
        word = "Determine",
        pronunciation = "/dɪˈtɝːmɪn/",
        meaning = "To decide or find out something.",
        example = "The results will determine the next steps.",
        status = VocabularyStatus.Learning,
        isFavorite = false
    ),
    VocabularyWordUi(
        word = "Significant",
        pronunciation = "/sɪɡˈnɪfɪkənt/",
        meaning = "Important or noticeable.",
        example = "There was a significant improvement in scores.",
        status = VocabularyStatus.Mastered,
        isFavorite = true
    ),
    VocabularyWordUi(
        word = "Approach",
        pronunciation = "/əˈproʊtʃ/",
        meaning = "A way of dealing with something.",
        example = "They adopted a new approach to problem solving.",
        status = VocabularyStatus.Learning,
        isFavorite = false
    ),
    VocabularyWordUi(
        word = "Enhance",
        pronunciation = "/ɪnˈhæns/",
        meaning = "To improve the quality or value of something.",
        example = "Music can enhance the atmosphere of a lesson.",
        status = VocabularyStatus.New,
        isFavorite = false
    ),
    VocabularyWordUi(
        word = "Sufficient",
        pronunciation = "/səˈfɪʃənt/",
        meaning = "Enough for a particular purpose.",
        example = "Provide sufficient evidence to support the claim.",
        status = VocabularyStatus.Learning,
        isFavorite = false
    ),
    VocabularyWordUi(
        word = "Sustain",
        pronunciation = "/səˈsteɪn/",
        meaning = "To maintain or keep something going.",
        example = "It is hard to sustain focus without breaks.",
        status = VocabularyStatus.Mastered,
        isFavorite = true
    ),
    VocabularyWordUi(
        word = "Illustrate",
        pronunciation = "/ˈɪləˌstreɪt/",
        meaning = "To explain or make something clear.",
        example = "The chart illustrates the growth trend.",
        status = VocabularyStatus.New,
        isFavorite = false
    ),
    VocabularyWordUi(
        word = "Allocate",
        pronunciation = "/ˈæl.ə.keɪt/",
        meaning = "To distribute resources for a purpose.",
        example = "Allocate time for reading every day.",
        status = VocabularyStatus.Learning,
        isFavorite = false
    ),
    VocabularyWordUi(
        word = "Evaluate",
        pronunciation = "/ɪˈvæl.ju.eɪt/",
        meaning = "To judge or assess the value of something.",
        example = "Evaluate the arguments before responding.",
        status = VocabularyStatus.Mastered,
        isFavorite = true
    )
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun VocabularySetDetailPreview() {
    ProjectEnlishLearningTheme {
        VocabularySetDetailScreen(navController = rememberNavController())
    }
}
