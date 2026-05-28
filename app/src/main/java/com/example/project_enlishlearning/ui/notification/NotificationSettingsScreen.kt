package com.example.project_enlishlearning.ui.notification

import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.BottomNavItem
import com.example.project_enlishlearning.ui.components.BottomNavigationBar
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.GradientEnd
import com.example.project_enlishlearning.ui.theme.GradientStart
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme

@Composable
fun NotificationSettingsScreen(
    navController: NavController,
    selected: BottomNavItem = BottomNavItem.Notification,
    onBottomItemSelected: (BottomNavItem) -> Unit = {}
) {
    var dailyReminder by remember { mutableStateOf(true) }
    var reviewReminder by remember { mutableStateOf(true) }
    var pushNotification by remember { mutableStateOf(true) }
    var emailNotification by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Notifications",
                subtitle = "Manage learning reminders",
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = AppDimens.ScreenPadding,
                        end = AppDimens.ScreenPadding,
                        top = 12.dp,
                        bottom = AppDimens.SectionSpacing
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NotificationHeaderCard()

                NotificationSection(title = "Daily Reminder") {
                    NotificationToggleItem(
                        icon = Icons.Default.NotificationsActive,
                        title = "Nhac hoc moi ngay",
                        subtitle = "Nhac ban hoc tu vung moi ngay theo lich ca nhan",
                        checked = dailyReminder,
                        recommended = true,
                        onCheckedChange = { dailyReminder = it }
                    )
                }

                NotificationSection(title = "Review Reminder") {
                    NotificationToggleItem(
                        icon = Icons.Default.Schedule,
                        title = "Nhac tu den han on",
                        subtitle = "Dua tren thuat toan Spaced Repetition (SM-2)",
                        checked = reviewReminder,
                        onCheckedChange = { reviewReminder = it }
                    )
                }

                NotificationSection(title = "Channel Settings") {
                    NotificationToggleItem(
                        icon = Icons.Default.Notifications,
                        title = "Push Notification",
                        subtitle = "Nhan thong bao qua dien thoai",
                        checked = pushNotification,
                        onCheckedChange = { pushNotification = it }
                    )

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )

                    NotificationToggleItem(
                        icon = Icons.Default.Email,
                        title = "Email Notification",
                        subtitle = "Nhan thong bao qua email",
                        checked = emailNotification,
                        onCheckedChange = { emailNotification = it }
                    )
                }

                InfoFooter()
            }
        }
    }
}

@Composable
fun NotificationHeaderCard() {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            GradientStart.copy(alpha = 0.9f),
                            GradientEnd.copy(alpha = 0.6f)
                        )
                    )
                )
                .padding(AppDimens.CardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = "Stay Consistent",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Manage how you receive learning reminders",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun NotificationSection(
    title: String,
    content: @Composable () -> Unit
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
fun NotificationToggleItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    recommended: Boolean = false,
    onCheckedChange: (Boolean) -> Unit
) {
    val tint by animateColorAsState(
        targetValue = if (checked) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "notificationIconTint"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(tint.copy(alpha = 0.12f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                if (recommended) {
                    Spacer(modifier = Modifier.width(8.dp))
                    RecommendedBadge()
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
            )
        )
    }
}

@Composable
private fun RecommendedBadge() {
    Row(
        modifier = Modifier
            .background(
                MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                RoundedCornerShape(50)
            )
            .padding(horizontal = 8.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Recommended",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun InfoFooter() {
    Text(
        text = "You can change this anytime in settings",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 6.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationSettingsPreview() {
    ProjectEnlishLearningTheme {
        NotificationSettingsScreen(navController = rememberNavController())
    }
}
