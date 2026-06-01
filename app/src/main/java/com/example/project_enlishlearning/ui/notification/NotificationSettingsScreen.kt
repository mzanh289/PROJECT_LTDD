package com.example.project_enlishlearning.ui.notification

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.project_enlishlearning.ui.components.PrimaryButton
import android.app.TimePickerDialog
import androidx.compose.material3.TextButton
import com.example.project_enlishlearning.viewmodel.NotificationViewModel

@Composable
fun NotificationSettingsScreen(
    navController: NavController,
    selected: BottomNavItem = BottomNavItem.Notification,
    onBottomItemSelected: (BottomNavItem) -> Unit = {},
    viewModel: NotificationViewModel = viewModel()
) {

    val context = LocalContext.current
    val dailyReminder by viewModel.dailyReminderEnabled.collectAsState()
    val selectedHour by viewModel.selectedHour.collectAsState()
    val selectedMinute by viewModel.selectedMinute.collectAsState()

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
                    .verticalScroll(rememberScrollState())
                    .padding(
                        start = AppDimens.ScreenPadding,
                        end = AppDimens.ScreenPadding,
                        top = 12.dp,
                        bottom = AppDimens.SectionSpacing + 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                NotificationSection(title = "Daily Reminder"){
                    NotificationToggleItem(
                        icon = Icons.Default.NotificationsActive,
                        title = "Daily learning reminder",
                        subtitle = "Remind you to learn new vocabulary every day",
                        checked = dailyReminder,
                        recommended = true,
                        onCheckedChange = {
                            viewModel.toggleDailyReminder(it)
                        }
                    )

                    NotificationTimeCard(
                        hour = selectedHour,
                        minute = selectedMinute,
                        onTimeSelected = { hour, minute ->

                            viewModel.updateReminderTime(
                                hour,
                                minute
                            )
                        }
                    )
                }



                NotificationSection(title = "Review Reminder") {
                    NotificationToggleItem(
                        icon = Icons.Default.Schedule,
                        title = "Review due reminder",
                        subtitle = "Comming soon",
                        checked = false,
                        onCheckedChange = {}
                    )
                }

                NotificationSection(title = "Channel Settings") {
                    NotificationToggleItem(
                        icon = Icons.Default.Notifications,
                        title = "Push Notification",
                        subtitle = "Comming soon",
                        checked = false,
                        onCheckedChange = {}
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    )

                    NotificationToggleItem(
                        icon = Icons.Default.Email,
                        title = "Email Notification",
                        subtitle = "Comming soon",
                        checked = false,
                        onCheckedChange = {}
                    )
                }

                InfoFooter()

                PrimaryButton(
                    text = "Test Notification",
                    onClick = {
                        viewModel.testNotification()
                    }
                )
            }
        }
    }
}

@Composable
fun NotificationTimeCard(
    hour: Int,
    minute: Int,
    onTimeSelected: (Int, Int) -> Unit
) {

    val context = LocalContext.current

    AppCard(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppDimens.CardPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = "Reminder Time",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = String.format(
                        "%02d:%02d",
                        hour,
                        minute
                    ),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            TextButton(
                onClick = {

                    val picker = TimePickerDialog(
                        context,
                        { _, h, m ->

                            onTimeSelected(
                                h,
                                m
                            )
                        },
                        hour,
                        minute,
                        true
                    )

                    picker.show()
                }
            ) {

                Text("Change")
            }
        }
    }
}

@Composable
fun NotificationSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(AppDimens.CardPadding),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
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


        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f, fill = false) // QUAN TRỌNG: Giúp chữ tự ngắt dòng nếu quá dài chứ không đẩy badge ra ngoài screen
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

        Spacer(modifier = Modifier.width(8.dp))

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
