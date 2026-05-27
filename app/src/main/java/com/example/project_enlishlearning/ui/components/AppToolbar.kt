package com.example.project_enlishlearning.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolbar(
	title: String,
	subtitle: String? = null,
	navigationIcon: ImageVector? = null,
	onNavigationClick: (() -> Unit)? = null,
	actions: @Composable RowScope.() -> Unit = {}
) {
	CenterAlignedTopAppBar(
		title = {
			Column {
				Text(
					text = title,
					style = MaterialTheme.typography.titleLarge,
					color = MaterialTheme.colorScheme.onBackground,
					maxLines = 1,
					overflow = TextOverflow.Ellipsis
				)
				if (!subtitle.isNullOrBlank()) {
					Spacer(modifier = Modifier.height(2.dp))
					Text(
						text = subtitle,
						style = MaterialTheme.typography.bodyMedium,
						color = MaterialTheme.colorScheme.onSurfaceVariant,
						maxLines = 1,
						overflow = TextOverflow.Ellipsis
					)
				}
			}
		},
		navigationIcon = {
			if (navigationIcon != null && onNavigationClick != null) {
				IconButton(onClick = onNavigationClick) {
					Icon(
						imageVector = navigationIcon,
						contentDescription = null,
						tint = MaterialTheme.colorScheme.onBackground
					)
				}
			}
		},
		actions = actions,
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = MaterialTheme.colorScheme.background
		)
	)
}

