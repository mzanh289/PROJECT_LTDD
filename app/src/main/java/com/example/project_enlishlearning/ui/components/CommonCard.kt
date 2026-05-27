package com.example.project_enlishlearning.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.GradientEnd
import com.example.project_enlishlearning.ui.theme.GradientStart

@Composable
fun AppGradientBackground(
	modifier: Modifier = Modifier,
	content: @Composable BoxScope.() -> Unit
) {
	Box(
		modifier = modifier
			.background(
				Brush.verticalGradient(
					listOf(GradientStart, GradientEnd)
				)
			),
		content = content
	)
}

@Composable
fun AppCard(
	modifier: Modifier = Modifier,
	content: @Composable ColumnScope.() -> Unit
) {
	Card(
		modifier = modifier,
		shape = RoundedCornerShape(AppDimens.CardRadius),
		colors = CardDefaults.cardColors(
			containerColor = MaterialTheme.colorScheme.surface
		),
		elevation = CardDefaults.cardElevation(
			defaultElevation = AppDimens.CardElevation
		),
		content = content
	)
}

@Composable
fun PrimaryButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	leadingIcon: ImageVector? = null,
	enabled: Boolean = true
) {
	Button(
		onClick = onClick,
		modifier = modifier.height(AppDimens.ButtonHeight),
		shape = RoundedCornerShape(AppDimens.ButtonRadius),
		enabled = enabled,
		colors = ButtonDefaults.buttonColors(
			containerColor = MaterialTheme.colorScheme.primary,
			contentColor = MaterialTheme.colorScheme.onPrimary
		)
	) {
		if (leadingIcon != null) {
			Icon(
				imageVector = leadingIcon,
				contentDescription = null,
				modifier = Modifier.size(18.dp)
			)
			Spacer(modifier = Modifier.size(8.dp))
		}
		Text(text = text, style = MaterialTheme.typography.labelLarge)
	}
}

@Composable
fun SecondaryButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	leadingIcon: ImageVector? = null
) {
	OutlinedButton(
		onClick = onClick,
		modifier = modifier.height(AppDimens.ButtonHeight),
		shape = RoundedCornerShape(AppDimens.ButtonRadius)
	) {
		if (leadingIcon != null) {
			Icon(
				imageVector = leadingIcon,
				contentDescription = null,
				modifier = Modifier.size(18.dp)
			)
			Spacer(modifier = Modifier.size(8.dp))
		}
		Text(text = text, style = MaterialTheme.typography.labelLarge)
	}
}

@Composable
fun AppTextField(
	value: String,
	onValueChange: (String) -> Unit,
	label: String,
	modifier: Modifier = Modifier,
	placeholder: String? = null,
	leadingIcon: ImageVector? = null,
	trailingIcon: @Composable (() -> Unit)? = null,
	singleLine: Boolean = true,
	minLines: Int = 1,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	visualTransformation: VisualTransformation = VisualTransformation.None
) {
	OutlinedTextField(
		value = value,
		onValueChange = onValueChange,
		label = { Text(label) },
		placeholder = placeholder?.let { { Text(it) } },
		leadingIcon = leadingIcon?.let {
			{ Icon(imageVector = it, contentDescription = null) }
		},
		trailingIcon = trailingIcon,
		singleLine = singleLine,
		minLines = minLines,
		keyboardOptions = keyboardOptions,
		visualTransformation = visualTransformation,
		shape = RoundedCornerShape(AppDimens.FieldRadius),
		colors = OutlinedTextFieldDefaults.colors(
			focusedBorderColor = MaterialTheme.colorScheme.primary,
			unfocusedBorderColor = MaterialTheme.colorScheme.outline,
			focusedLabelColor = MaterialTheme.colorScheme.primary,
			focusedLeadingIconColor = MaterialTheme.colorScheme.primary
		),
		modifier = modifier.fillMaxWidth()
	)
}

@Composable
fun AppPasswordField(
	value: String,
	onValueChange: (String) -> Unit,
	label: String,
	visible: Boolean,
	onToggle: () -> Unit,
	modifier: Modifier = Modifier
) {
	AppTextField(
		value = value,
		onValueChange = onValueChange,
		label = label,
		modifier = modifier,
		leadingIcon = Icons.Default.Lock,
		trailingIcon = {
			IconButton(onClick = onToggle) {
				Icon(
					imageVector = if (visible) {
						Icons.Outlined.Visibility
					} else {
						Icons.Outlined.VisibilityOff
					},
					contentDescription = null
				)
			}
		},
		visualTransformation = if (visible) {
			VisualTransformation.None
		} else {
			PasswordVisualTransformation()
		}
	)
}

@Composable
fun AppSectionHeader(
	title: String,
	subtitle: String? = null,
	modifier: Modifier = Modifier
) {
	Column(modifier = modifier) {
		Text(
			text = title,
			style = MaterialTheme.typography.titleLarge,
			color = MaterialTheme.colorScheme.onBackground
		)
		if (!subtitle.isNullOrBlank()) {
			Spacer(modifier = Modifier.height(6.dp))
			Text(
				text = subtitle,
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant
			)
		}
	}
}

@Composable
fun AppTagChip(
	text: String,
	modifier: Modifier = Modifier,
	containerColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
	contentColor: Color = MaterialTheme.colorScheme.primary
) {
	Box(
		modifier = modifier
			.background(containerColor, CircleShape)
			.padding(horizontal = 14.dp, vertical = 6.dp),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.labelLarge,
			color = contentColor
		)
	}
}

@Composable
fun AuthLogo(
	modifier: Modifier = Modifier,
	text: String = "M"
) {
	Box(
		modifier = modifier
			.size(72.dp)
			.background(MaterialTheme.colorScheme.primary, CircleShape),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = text,
			color = MaterialTheme.colorScheme.onPrimary,
			style = MaterialTheme.typography.displaySmall
		)
	}
}

