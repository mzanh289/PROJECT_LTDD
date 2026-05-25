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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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

@Composable
fun FlashcardLearningScreen() {

    var flipped by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (flipped) 180f else 0f,
        animationSpec = tween(500),
        label = ""
    )

    var studiedWords by remember { mutableIntStateOf(18) }
    val dailyGoal = 30

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
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(20.dp)
            ) {

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Flashcard Learning",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Review vocabulary using spaced repetition.",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(28.dp))

                // =========================
                // DAILY LEARNING PLAN
                // =========================

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "Daily Learning Plan",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            DailyPlanItem(
                                title = "New Words",
                                value = "30"
                            )

                            DailyPlanItem(
                                title = "Need Review",
                                value = "52"
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "$studiedWords / $dailyGoal completed",
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = {
                                studiedWords / dailyGoal.toFloat()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp),
                            color = Primary,
                            trackColor = Color(0xFFE5E7EB)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // =========================
                // FLASHCARD
                // =========================

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp)
                        .rotate(rotation)
                        .clickable {
                            flipped = !flipped
                        },
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        if (!flipped) {

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Surface(
                                    shape = CircleShape,
                                    color = Primary.copy(alpha = 0.1f)
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Replay,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "Meticulous",
                                    fontSize = 34.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF111827)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "/məˈtɪk.jə.ləs/",
                                    color = Color.Gray,
                                    fontSize = 18.sp
                                )

                                Spacer(modifier = Modifier.height(20.dp))

                                Text(
                                    text = "Tap to flip card",
                                    color = Primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }

                        } else {

                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {

                                Text(
                                    text = "Meaning",
                                    color = Primary,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "Showing great attention to detail; very careful and precise.",
                                    textAlign = TextAlign.Center,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Text(
                                    text = "Example",
                                    color = Orange,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "\"She kept meticulous records of every transaction.\"",
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // =========================
                // SRS BUTTONS
                // =========================

                Text(
                    text = "How well did you remember?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    SrsButton(
                        text = "Again",
                        color = Color(0xFFEF4444),
                        modifier = Modifier.weight(1f)
                    )

                    SrsButton(
                        text = "Hard",
                        color = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    SrsButton(
                        text = "Good",
                        color = Color(0xFF10B981),
                        modifier = Modifier.weight(1f)
                    )

                    SrsButton(
                        text = "Easy",
                        color = Primary,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // =========================
                // SM-2 RESULT
                // =========================

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = "SM-2 Result",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {

                            Sm2Info(
                                title = "Next Review",
                                value = "Tomorrow"
                            )

                            Sm2Info(
                                title = "Ease Factor",
                                value = "2.5"
                            )
                        }
                    }
                }
            }

            BottomNavigationBar()
        }
    }
}

@Composable
fun DailyPlanItem(
    title: String,
    value: String
) {

    Column {

        Text(
            text = value,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            color = Color.Gray
        )
    }
}

@Composable
fun Sm2Info(
    title: String,
    value: String
) {

    Column {

        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            color = Color.Gray
        )
    }
}

@Composable
fun SrsButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier
) {

    Button(
        onClick = { },
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color
        )
    ) {

        Text(
            text = text,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun BottomNavigationBar() {

    NavigationBar(
        containerColor = Color.White
    ) {

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null
                )
            },
            label = {
                Text("Dashboard")
            }
        )

        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null
                )
            },
            label = {
                Text("Learning")
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Primary,
                selectedTextColor = Primary,
                indicatorColor = Primary.copy(alpha = 0.1f)
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = null
                )
            },
            label = {
                Text("Statistics")
            }
        )

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null
                )
            },
            label = {
                Text("Profile")
            }
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun FlashcardLearningPreview() {

    MaterialTheme {
        FlashcardLearningScreen()
    }
}