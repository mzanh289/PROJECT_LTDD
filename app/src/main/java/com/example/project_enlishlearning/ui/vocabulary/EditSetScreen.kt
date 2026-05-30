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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_enlishlearning.ui.components.*
import com.example.project_enlishlearning.ui.theme.AppDimens
import com.example.project_enlishlearning.viewmodel.VocabularyViewModel

@Composable
fun EditVocabularySetScreen(
    navController: NavController,
    setId: Int, // Chuyển từ String sang Int để khớp với DB
    viewModel: VocabularyViewModel = viewModel()
) {
    // Lấy toàn bộ danh sách bộ từ vựng từ Database ra để tìm bộ cần sửa
    val allSets by viewModel.vocabularySets.collectAsState()
    val currentSet = allSets.find { it.setId == setId }

    var setName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Khi tìm thấy dữ liệu từ DB, đổ text cũ vào ô nhập liệu tương ứng
    LaunchedEffect(currentSet) {
        currentSet?.let {
            setName = it.title
            description = it.description
        }
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Edit Set",
                subtitle = "Modify vocabulary set information.",
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
                                leadingIcon = Icons.Default.Book
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            AppTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = "Description",
                                leadingIcon = Icons.Default.Description,
                                minLines = 3,
                                singleLine = false
                            )

                            Spacer(modifier = Modifier.height(22.dp))

                            PrimaryButton(
                                text = "Save Changes",
                                onClick = {
                                    if (setName.isNotBlank() && currentSet != null) {
                                        // Gọi hàm update của ViewModel đẩy thông tin mới xuống DB
                                        viewModel.updateVocabularySet(
                                            setId = currentSet.setId,
                                            title = setName,
                                            description = description,
                                            totalWords = currentSet.totalWords,
                                            progress = currentSet.progress
                                        )
                                        navController.popBackStack() // Cập nhật xong quay lại màn hình cũ
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