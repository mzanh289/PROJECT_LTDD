package com.example.project_enlishlearning.ui.dashboard

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

@Composable
fun DashboardScreen(
    selected: BottomNavItem = BottomNavItem.Dashboard,
    onBottomItemSelected: (BottomNavItem) -> Unit = {}
) {
    var dailyReminder by remember { mutableStateOf(true) }
    var reviewReminder by remember { mutableStateOf(true) }
    var emailNotification by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Learning Progress",
                subtitle = "Track your English learning performance."
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
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        DashboardMetricCard(
                            title = "Words Learned",
                            value = "1,245",
                            icon = Icons.Default.School,
                            iconTint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        DashboardMetricCard(
                            title = "Streak",
                            value = "15 Days",
                            icon = Icons.Default.LocalFireDepartment,
                            iconTint = Accent,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                item {
                    DashboardMetricCard(
                        title = "Accuracy",
                        value = "92%",
                        icon = Icons.AutoMirrored.Filled.ShowChart,
                        iconTint = Secondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    AppSectionHeader(title = "Learning Analytics")
                }

                items(listOf(
                    Pair("Daily Activity", "Your learning consistency this week"),
                    Pair("Retention Rate", "Vocabulary memory performance")
                )) { item ->
                    AnalyticsCard(title = item.first, subtitle = item.second)
                }

                item {
                    AppSectionHeader(title = "Current English Level")
                }

                item {
                    LevelCard()
                }

                item {
                    AppSectionHeader(title = "Notification Settings")
                }

                item {
                    NotificationToggleItem(
                        icon = Icons.Default.Notifications,
                        title = "Daily Learning Reminder",
                        subtitle = "Get reminded to study every day",
                        checked = dailyReminder,
                        onCheckedChange = { dailyReminder = it }
                    )
                }

                item {
                    NotificationToggleItem(
                        icon = Icons.Default.Timer,
                        title = "Review Reminder",
                        subtitle = "Reminder for vocabulary review schedule",
                        checked = reviewReminder,
                        onCheckedChange = { reviewReminder = it }
                    )
                }

                item {
                    NotificationToggleItem(
                        icon = Icons.Default.Email,
                        title = "Email Notifications",
                        subtitle = "Receive progress and lesson updates",
                        checked = emailNotification,
                        onCheckedChange = { emailNotification = it }
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressDashboardScreen() {
    DashboardScreen()
}

@Composable
private fun DashboardMetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    AppCard(modifier = modifier) {
        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun AnalyticsCard(
    title: String,
    subtitle: String
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                listOf(36, 72, 54, 90, 62, 102, 84).forEach { height ->
                    Box(
                        modifier = Modifier
                            .width(22.dp)
                            .height(height.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun LevelCard() {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(AppDimens.CardPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Intermediate",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "You can communicate confidently in daily situations.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NotificationToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProgressDashboardPreview() {
    ProjectEnlishLearningTheme {
        DashboardScreen()
    }
}

