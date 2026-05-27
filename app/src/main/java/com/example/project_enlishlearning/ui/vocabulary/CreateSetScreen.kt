package com.example.project_enlishlearning.ui.vocabulary

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppTextField
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.PrimaryButton
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateVocabularySetScreen(
    navController: NavController,
    onCreate: () -> Unit = {}
) {
    var setName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val selectedTags = remember { mutableStateListOf<String>() }
    val tags = listOf("IELTS", "Business", "Travel", "TOEIC", "Academic", "Daily Life")

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Create Vocabulary Set",
                subtitle = "Organize vocabulary into custom learning collections.",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        AppGradientBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = AppDimens.ScreenPadding,
                    end = AppDimens.ScreenPadding,
                    top = 12.dp,
                    bottom = AppDimens.SectionSpacing
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.padding(AppDimens.CardPadding)
                        ) {
                            AppTextField(
                                value = setName,
                                onValueChange = { setName = it },
                                label = "Vocabulary Set Name",
                                leadingIcon = Icons.Default.Book
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            AppTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = "Description",
                                leadingIcon = Icons.Default.Description,
                                minLines = 4,
                                singleLine = false
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            Text(
                                text = "Tags",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                tags.forEach { tag ->
                                    FilterChip(
                                        selected = selectedTags.contains(tag),
                                        onClick = {
                                            if (selectedTags.contains(tag)) {
                                                selectedTags.remove(tag)
                                            } else {
                                                selectedTags.add(tag)
                                            }
                                        },
                                        label = { Text(tag) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(22.dp))

                            PrimaryButton(
                                text = "Create Set",
                                onClick = onCreate,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreateVocabularySetPreview() {
    ProjectEnlishLearningTheme {
        CreateVocabularySetScreen(navController = rememberNavController())
    }
}