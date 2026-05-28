package com.example.project_enlishlearning.ui.vocabulary

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppTextField
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.BottomNavItem
import com.example.project_enlishlearning.ui.components.BottomNavigationBar
import com.example.project_enlishlearning.ui.components.PrimaryButton
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme

@Composable
fun AddVocabularyScreen(
    navController: NavController,
    selected: BottomNavItem = BottomNavItem.Vocabulary,
    onBottomItemSelected: (BottomNavItem) -> Unit = {}
) {
    var word by remember { mutableStateOf("") }
    var pronunciation by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var example by remember { mutableStateOf("") }
    var collocation by remember { mutableStateOf("") }
    var relatedWords by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Add Vocabulary",
                subtitle = "Add detailed vocabulary information for learning.",
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
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    start = AppDimens.ScreenPadding,
                    end = AppDimens.ScreenPadding,
                    top = 12.dp,
                    bottom = AppDimens.SectionSpacing
                ),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        androidx.compose.foundation.layout.Column(
                            modifier = Modifier.padding(AppDimens.CardPadding)
                        ) {
                            AppTextField(
                                value = word,
                                onValueChange = { word = it },
                                label = "Word",
                                leadingIcon = Icons.Default.Language
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = pronunciation,
                                onValueChange = { pronunciation = it },
                                label = "Pronunciation",
                                leadingIcon = Icons.Default.RecordVoiceOver
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = meaning,
                                onValueChange = { meaning = it },
                                label = "Meaning",
                                leadingIcon = Icons.Default.Description
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = "Description (English)",
                                leadingIcon = Icons.Default.Description,
                                minLines = 4,
                                singleLine = false
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = example,
                                onValueChange = { example = it },
                                label = "Example",
                                leadingIcon = Icons.Default.Description,
                                minLines = 3,
                                singleLine = false
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = collocation,
                                onValueChange = { collocation = it },
                                label = "Collocation",
                                leadingIcon = Icons.Default.Tag
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = relatedWords,
                                onValueChange = { relatedWords = it },
                                label = "Related Words",
                                leadingIcon = Icons.Default.Language
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = note,
                                onValueChange = { note = it },
                                label = "Note",
                                leadingIcon = Icons.AutoMirrored.Filled.Note,
                                minLines = 3,
                                singleLine = false
                            )
                            Spacer(modifier = Modifier.height(18.dp))
                            PrimaryButton(
                                text = "Save Vocabulary",
                                onClick = {
                                    navController.navigate(Screen.VocabularySetDetail.route)
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddVocabularyPreview() {
    ProjectEnlishLearningTheme {
        AddVocabularyScreen(navController = rememberNavController())
    }
}