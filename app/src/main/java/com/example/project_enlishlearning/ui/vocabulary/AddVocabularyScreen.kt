package com.example.project_enlishlearning.ui.vocabulary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.ui.components.*
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.ui.theme.ProjectEnlishLearningTheme
import com.example.project_enlishlearning.viewmodel.VocabularyViewModel

@Composable
fun AddVocabularyScreen(
    navController: NavController,
    setId: Int, // Bắt buộc nhận setId để biết từ này thuộc về bộ nào
    viewModel: VocabularyViewModel = viewModel()
) {
    var word by remember { mutableStateOf("") }
    var pronunciation by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var example by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Add Vocabulary",
                subtitle = "Add a new word to your set.",
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
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(AppDimens.ScreenPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
                            AppTextField(
                                value = word,
                                onValueChange = { word = it },
                                label = "Vocabulary Word",
                                placeholder = "e.g., Abundant"
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = pronunciation,
                                onValueChange = { pronunciation = it },
                                label = "Pronunciation",
                                placeholder = "e.g., /əˈbʌn.dənt/",
                                leadingIcon = Icons.Default.RecordVoiceOver
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = meaning,
                                onValueChange = { meaning = it },
                                label = "Meaning",
                                placeholder = "e.g., Nhiều, phong phú",
                                leadingIcon = Icons.Default.Description
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = example,
                                onValueChange = { example = it },
                                label = "Example Sentence",
                                placeholder = "e.g., Birds are abundant in the river.",
                                minLines = 2,
                                singleLine = false
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            PrimaryButton(
                                text = "Save Vocabulary",
                                onClick = {
                                    if (word.isNotBlank() && meaning.isNotBlank()) {
                                        // Gọi ViewModel thêm từ vựng vào DB đúng setId của bộ từ vựng đó
                                        viewModel.addWord(
                                            setId = setId,
                                            word = word,
                                            pronunciation = pronunciation,
                                            meaning = meaning,
                                            example = example
                                        )
                                        navController.popBackStack() // Lưu xong quay về màn chi tiết bộ
                                    }
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
        AddVocabularyScreen(navController = rememberNavController(), setId = 1)
    }
}