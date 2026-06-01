package com.example.project_enlishlearning.ui.vocabulary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.ui.components.*
import com.example.project_enlishlearning.ui.theme.*
import com.example.project_enlishlearning.viewmodel.VocabularyViewModel
import com.example.project_enlishlearning.data.local.entity.VocabularySetEntity
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import com.example.project_enlishlearning.viewmodel.ExportState
import android.app.Application
import com.example.project_enlishlearning.utils.file.FileExportHelper
import kotlinx.coroutines.launch

// Định nghĩa enum trạng thái để đồng bộ hiển thị màu sắc trên UI
enum class VocabularyStatus(val label: String) {
    New("New"),
    Learning("Learning"),
    Mastered("Mastered")
}

@Composable
fun VocabularySetDetailScreen(
    navController: NavController,
    setId: Int, // Nhận ID của bộ từ vựng được truyền sang từ màn hình trước
    selected: BottomNavItem = BottomNavItem.Vocabulary,
    onBottomItemSelected: (BottomNavItem) -> Unit = {},
    viewModel: VocabularyViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val exportState by viewModel.exportState.collectAsState()

    var pendingExport by remember { mutableStateOf<ExportPayload?>(null) }

    val exportLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("text/csv")
    ) { uri ->
        val payload = pendingExport
        if (uri != null && payload != null) {
            coroutineScope.launch {
                try {
                    FileExportHelper.writeCsvToUri(
                        context = context,
                        uri = uri,
                        csvContent = payload.csvContent
                    )
                    snackbarHostState.showSnackbar(
                        message = "Exported: ${payload.fileName}"
                    )
                } catch (exception: Exception) {
                    snackbarHostState.showSnackbar(
                        message = exception.message ?: "Export failed"
                    )
                } finally {
                    pendingExport = null
                    viewModel.resetExportState()
                }
            }
        } else {
            pendingExport = null
            viewModel.resetExportState()
        }
    }

    LaunchedEffect(exportState) {
        when (exportState) {
            is ExportState.Error -> {
                val error = exportState as ExportState.Error
                snackbarHostState.showSnackbar(
                    message = error.message
                )
                viewModel.resetExportState()
            }
            is ExportState.Ready -> {
                val ready = exportState as ExportState.Ready
                pendingExport = ExportPayload(
                    csvContent = ready.csvContent,
                    fileName = ready.fileName
                )
                exportLauncher.launch(ready.fileName)
            }
            else -> Unit
        }
    }

    // 1. Tự động gọi Database để lấy danh sách từ vựng khi mở màn hình này lên
    LaunchedEffect(setId) {
        viewModel.loadWordsForSet(setId)
    }

    // 2. Lấy dữ liệu thật từ ViewModel
    val words by viewModel.wordsInSet.collectAsState()
    val allSets by viewModel.vocabularySets.collectAsState()

    // Tìm thông tin của bộ từ vựng hiện tại để vẽ phần Header
    val currentSet = allSets.find { it.setId == setId }

    Scaffold(
        topBar = {
            AppToolbar(
                title = currentSet?.title ?: "Loading...",
                subtitle = "Vocabulary set details",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                selected = selected,
                onItemSelected = onBottomItemSelected
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        AppGradientBackground(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = AppDimens.ScreenPadding,
                    end = AppDimens.ScreenPadding,
                    top = 12.dp,
                    bottom = AppDimens.SectionSpacing
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hiển thị thẻ thông tin Bộ từ vựng ở trên cùng nếu tìm thấy dữ liệu
                currentSet?.let {
                    item {
                        SetHeaderCard(set = it)
                    }
                }

                item {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
                            AppSectionHeader(
                                title = "Manage vocabulary",
                                subtitle = "Add, import, export, or search"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                    SecondaryButton(
                                        text = "Add",
                                        onClick = {
                                            navController.navigate("${Screen.AddVocabulary.route}/$setId")
                                        },
                                        leadingIcon = Icons.Default.Add,
                                        modifier = Modifier.weight(1f).widthIn(min = 0.dp)
                                    )

                                    SecondaryButton(
                                        text = "Import",
                                        onClick = {
                                            navController.navigate("${Screen.ImportVocabulary.route}/$setId")
                                        },
                                        leadingIcon = Icons.Default.UploadFile,
                                        modifier = Modifier.weight(1f).widthIn(min = 0.dp)
                                    )

                                    PrimaryButton(
                                        text = "Export",
                                        onClick = {
                                            viewModel.exportVocabularySet(setId, currentSet?.title)
                                        },
                                        leadingIcon = Icons.Default.FileDownload,
                                        enabled = exportState !is ExportState.Loading,
                                        modifier = Modifier.weight(1f).widthIn(min = 0.dp)
                                    )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            VocabularySearchBar(
                                value = searchQuery,
                                onValueChange = { searchQuery = it }
                            )
                        }
                    }
                }

                // Lọc từ vựng theo ô tìm kiếm
                val filteredWords = words.filter {
                    it.word.contains(searchQuery, ignoreCase = true) ||
                            it.meaning.contains(searchQuery, ignoreCase = true)
                }

                // ... (Các code bên trên giữ nguyên)
                if (filteredWords.isEmpty()) {
                    item {
                        EmptyStateView(
                            onAddVocabulary = {
                                navController.navigate("${Screen.AddVocabulary.route}/$setId")
                            }
                        )
                    }
                } else {
                    itemsIndexed(filteredWords, key = { _, item -> item.wordId }) { _, item ->
                        VocabularyItemCard(
                            word = item,
                            onFavoriteToggle = {
                                viewModel.toggleFavorite(item)
                            },
                            onDeleteClick = {
                                // Gọi hàm xóa từ vựng vừa viết ở Bước 1
                                    viewModel.deleteWord(item)
                            },
                            onEditClick = {
                                navController.navigate("edit_word/${item.wordId}")
                            }
                        )
                    }
                }
                // ...
                }
            }
        }
    }

@Composable
fun SetHeaderCard(set: VocabularySetEntity) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        listOf(
                            GradientStart.copy(alpha = 0.9f),
                            GradientEnd.copy(alpha = 0.6f)
                        )
                    )
                )
                .padding(AppDimens.CardPadding)
        ) {
            Text(
                text = set.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = set.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${set.totalWords} words",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "${set.progress}%",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { set.progress / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )
        }
    }
}

private data class ExportPayload(
    val csvContent: String,
    val fileName: String
)

@Composable
fun VocabularySearchBar(value: String, onValueChange: (String) -> Unit) {
    AppTextField(
        value = value,
        onValueChange = onValueChange,
        label = "Search",
        placeholder = "Search words...",
        leadingIcon = Icons.Default.Search
    )
}

@Composable
fun VocabularyItemCard(
    word: VocabularyWordEntity,
    onFavoriteToggle: () -> Unit,
    onDeleteClick: () -> Unit, // Thêm tham số sự kiện Xóa
    onEditClick: () -> Unit    // Thêm tham số sự kiện Sửa
) {
    // 1. Thêm biến quản lý trạng thái mở/đóng Menu và Hộp thoại xác nhận xóa
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = word.word,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = word.pronunciation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = onFavoriteToggle) {
                    Icon(
                        imageVector = if (word.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (word.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // 2. Wrap IconButton trong Box và thêm DropdownMenu
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
                        )
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit Word") },
                            onClick = {
                                expanded = false
                                onEditClick() // Gọi sự kiện sửa
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete Word") },
                            onClick = {
                                expanded = false
                                showDeleteDialog = true // Mở hộp thoại xác nhận xóa
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = word.meaning,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = word.example,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            val currentStatus = when (word.status) {
                "Learning" -> VocabularyStatus.Learning
                "Mastered" -> VocabularyStatus.Mastered
                else -> VocabularyStatus.New
            }
            StatusChip(status = currentStatus)
        }
    }

    // 3. Hiển thị thông báo xác nhận xóa
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete vocabulary?") },
            text = { Text("Are you sure you want to delete '${word.word}'? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick() // Gọi logic xóa thực sự xuống Database
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun EmptyStateView(onAddVocabulary: () -> Unit) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(AppDimens.CardPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "No vocabulary in this set",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Add your first word to start learning.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            PrimaryButton(
                text = "Add first word",
                onClick = onAddVocabulary,
                leadingIcon = Icons.Default.Add,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun StatusChip(status: VocabularyStatus) {
    val color = when (status) {
        VocabularyStatus.New -> MaterialTheme.colorScheme.primary
        VocabularyStatus.Learning -> Warning
        VocabularyStatus.Mastered -> Success
    }

    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = status.label,
            style = MaterialTheme.typography.labelLarge,
            color = color
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun VocabularySetDetailPreview() {
    ProjectEnlishLearningTheme {
        VocabularySetDetailScreen(navController = rememberNavController(), setId = 1)
    }
}
