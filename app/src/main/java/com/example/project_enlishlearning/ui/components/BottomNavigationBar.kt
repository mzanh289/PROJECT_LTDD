package com.example.project_enlishlearning.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomNavItem(val label: String, val icon: ImageVector) {
	Dashboard("Dashboard", Icons.Default.Home),
	Vocabulary("Vocabulary", Icons.AutoMirrored.Filled.MenuBook),
	Profile("Profile", Icons.Default.Person),

	Notification("Notification", Icons.Default.Notifications)
}

@Composable
fun BottomNavigationBar(
	selected: BottomNavItem,
	onItemSelected: (BottomNavItem) -> Unit
) {
	NavigationBar(
		containerColor = MaterialTheme.colorScheme.surface
	) {
		BottomNavItem.values().forEach { item ->
			NavigationBarItem(
				selected = item == selected,
				onClick = { onItemSelected(item) },
				icon = {
					androidx.compose.material3.Icon(
						imageVector = item.icon,
						contentDescription = item.label
					)
				},
				label = { Text(item.label) },
				colors = NavigationBarItemDefaults.colors(
					selectedIconColor = MaterialTheme.colorScheme.primary,
					selectedTextColor = MaterialTheme.colorScheme.primary,
					indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
					unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
					unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
				)
			)
		}
	}
}

