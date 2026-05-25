package com.example.project_enlishlearning.ui.learning

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Primary = Color(0xFF4F46E5)
private val Orange = Color(0xFFFF8A00)
private val Background = Color(0xFFF5F7FF)

data class FlashcardWord(
    val word: String,
    val pronunciation: String,
    val meaning: String,
    val example: String
)

@Composable
fun NewWordsPreviewScreen() {

    val words = remember {
        listOf(
            FlashcardWord(
                word = "Acquire",
                pronunciation = "/əˈkwaɪər/",
                meaning = "To gain or obtain something.",
                example = "She acquired strong communication skills."
            ),
            FlashcardWord(
                word = "Determine",
                pronunciation = "/dɪˈtɜːrmɪn/",
                meaning = "To decide or establish something.",
                example = "The teacher determined the final score."
            ),
            FlashcardWord(
                word = "Significant",
                pronunciation = "/sɪɡˈnɪfɪkənt/",
                meaning = "Important or meaningful.",
                example = "Learning daily creates significant progress."
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFEDE9FE),
                        Background
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Today's New Words",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Preview vocabulary before starting flashcard learning.",
                color = Color.Gray,
                fontSize = 15.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                shape = RoundedCornerShape(22.dp),
                color = Primary,
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier.padding(22.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Daily Goal",
                            color = Color.White.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "12 New Words",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.15f))
                            .padding(14.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(34.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(words) { item ->

                    FlashcardPreviewCard(item)
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }

        ElevatedButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(18.dp)
        ) {

            Text(
                text = "Start Flashcard Session",
                modifier = Modifier.padding(vertical = 6.dp)
            )
        }
    }
}

@Composable
fun FlashcardPreviewCard(item: FlashcardWord) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = item.word,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.pronunciation,
                        color = Primary,
                        fontSize = 15.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(Primary.copy(alpha = 0.1f))
                        .padding(10.dp)
                ) {

                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = Primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Meaning",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.meaning,
                color = Color(0xFF4B5563),
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Example",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF3F4F6)
            ) {

                Text(
                    text = item.example,
                    modifier = Modifier.padding(14.dp),
                    color = Color(0xFF374151),
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NewWordsPreviewScreenPreview() {
    MaterialTheme {
        NewWordsPreviewScreen()
    }
}

@Composable
fun FlashcardResultScreen() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFEDE9FE),
                        Background
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Primary,
                                Orange
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "92%",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Session Completed 🎉",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Great job! You reviewed today's flashcards successfully.",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                ResultStatCard(
                    title = "Correct",
                    value = "22",
                    color = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )

                ResultStatCard(
                    title = "Need Review",
                    value = "3",
                    color = Orange,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                ResultStatCard(
                    title = "Study Time",
                    value = "18m",
                    color = Primary,
                    modifier = Modifier.weight(1f)
                )

                ResultStatCard(
                    title = "XP Earned",
                    value = "+120",
                    color = Color(0xFFEC4899),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {

                Column(
                    modifier = Modifier.padding(22.dp)
                ) {

                    Text(
                        text = "Next Review Schedule",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    ReviewScheduleItem(
                        word = "Acquire",
                        nextReview = "Tomorrow - 09:00 AM"
                    )

                    ReviewScheduleItem(
                        word = "Determine",
                        nextReview = "In 3 days"
                    )

                    ReviewScheduleItem(
                        word = "Significant",
                        nextReview = "Next week"
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            ElevatedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {

                Text(
                    text = "Continue Learning",
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            ElevatedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp)
            ) {

                Text(
                    text = "Review Difficult Words",
                    modifier = Modifier.padding(vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ResultStatCard(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = value,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = title,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ReviewScheduleItem(
    word: String,
    nextReview: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = word,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = nextReview,
            color = Primary,
            fontSize = 13.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FlashcardResultScreenPreview() {
    MaterialTheme {
        FlashcardResultScreen()
    }
}