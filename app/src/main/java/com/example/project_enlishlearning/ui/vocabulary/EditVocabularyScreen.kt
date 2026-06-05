package com.example.project_enlishlearning.ui.vocabulary

import android.app.Application
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_enlishlearning.ui.components.*
import com.example.project_enlishlearning.utils.constants.WORD_ACTION_RESULT
import com.example.project_enlishlearning.utils.constants.WordAction
import com.example.project_enlishlearning.viewmodel.VocabularyViewModel


@Composable
fun EditVocabularyScreen(
    navController: NavController,
    wordId: Any,
    viewModel: VocabularyViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    // Lấy danh sách từ trong tập dữ liệu hiện tại để tìm từ cần sửa
    // 1. Lắng nghe dữ liệu của từ vựng từ ViewModel
    val currentWord by viewModel.currentEditWord.collectAsState()

    // 2. Tự động gọi ViewModel lấy dữ liệu khi mở màn hình
    LaunchedEffect(wordId) {
        viewModel.loadWordById(wordId as Int)
    }

    var wordText by remember { mutableStateOf("") }
    var pronunciation by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }
    var example by remember { mutableStateOf("") }

    // Đổ dữ liệu cũ vào ô nhập khi tìm thấy từ
    LaunchedEffect(currentWord) {
        currentWord?.let {
            wordText = it.word
            pronunciation = it.pronunciation
            meaning = it.meaning
            example = it.example
        }
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Edit Vocabulary",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        AppGradientBackground(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            AppTextField(value = wordText, onValueChange = { wordText = it }, label = "Word")
                            Spacer(modifier = Modifier.height(12.dp))
                            AppTextField(value = pronunciation, onValueChange = { pronunciation = it }, label = "Pronunciation")
                            Spacer(modifier = Modifier.height(12.dp))
                            AppTextField(value = meaning, onValueChange = { meaning = it }, label = "Meaning")
                            Spacer(modifier = Modifier.height(12.dp))
                            AppTextField(value = example, onValueChange = { example = it }, label = "Example", minLines = 2)

                            Spacer(modifier = Modifier.height(24.dp))

                            PrimaryButton(
                                text = "Update Word",
                                onClick = {
                                    // Dùng ?.let để an toàn tuyệt đối với null
                                    currentWord?.let { word ->
                                        if (wordText.isNotBlank()) {
                                            viewModel.updateWord(
                                                // Lấy dữ liệu từ biến 'word' đã được bảo vệ
                                                wordId = word.wordId,
                                                setId = word.setId,
                                                word = wordText, // Dữ liệu mới từ ô nhập
                                                pronunciation = pronunciation,
                                                meaning = meaning,
                                                example = example,
                                                status = word.status,
                                                isFavorite = word.isFavorite
                                            )
                                            navController.previousBackStackEntry
                                                ?.savedStateHandle
                                                ?.set(
                                                    WORD_ACTION_RESULT,
                                                    WordAction.UPDATED
                                                )

                                            navController.popBackStack()
                                        }
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