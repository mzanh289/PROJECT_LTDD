package com.example.project_enlishlearning.ui.vocabulary

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Description
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
fun CreateVocabularySetScreen(
    navController: NavController,
    viewModel: VocabularyViewModel = viewModel() // Tiêm ViewModel vào đây
) {
    var setName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Create Set",
                subtitle = "Add a new vocabulary collection.",
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
                                value = setName,
                                onValueChange = { setName = it },
                                label = "Set Name",
                                placeholder = "e.g., IELTS Academic Vocabulary",
                                leadingIcon = Icons.Default.Book
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = "Description",
                                placeholder = "What is this set for?",
                                leadingIcon = Icons.Default.Description,
                                minLines = 3,
                                singleLine = false
                            )

                            Spacer(modifier = Modifier.height(22.dp))

                            PrimaryButton(
                                text = "Create Set",
                                onClick = {
                                    if (setName.isNotBlank()) {
                                        // 1. Gọi ViewModel lưu vào DB
                                        viewModel.addVocabularySet(setName, description)
                                        // 2. Quay trở lại màn hình danh sách thay vì navigate tràn lan
                                        navController.popBackStack()
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CreateVocabularySetPreview() {
    ProjectEnlishLearningTheme {
        CreateVocabularySetScreen(navController = rememberNavController())
    }
}