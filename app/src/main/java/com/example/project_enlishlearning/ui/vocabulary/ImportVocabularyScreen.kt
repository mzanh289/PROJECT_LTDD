package com.example.project_enlishlearning.ui.vocabulary

import android.app.Application
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_enlishlearning.data.importexport.ImportPreview
import com.example.project_enlishlearning.ui.components.AppCard
import com.example.project_enlishlearning.ui.components.AppGradientBackground
import com.example.project_enlishlearning.ui.components.AppSectionHeader
import com.example.project_enlishlearning.ui.components.AppToolbar
import com.example.project_enlishlearning.ui.components.PrimaryButton
import com.example.project_enlishlearning.ui.components.SecondaryButton
import com.example.project_enlishlearning.utils.file.FilePickerHelper
import com.example.project_enlishlearning.viewmodel.ImportPreviewState
import com.example.project_enlishlearning.viewmodel.ImportState
import com.example.project_enlishlearning.viewmodel.VocabularyViewModel
import com.example.project_enlishlearning.ui.theme.AppDimens

@Composable
fun ImportVocabularyScreen(
    navController: NavController,
    setId: Int,
    viewModel: VocabularyViewModel = viewModel(
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(
            LocalContext.current.applicationContext as Application
        )
    )
) {
    val previewState by viewModel.importPreviewState.collectAsState()
    val importState by viewModel.importState.collectAsState()
    val selectedFileName by viewModel.selectedImportFileName.collectAsState()

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetImportState()
        }
    }

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            val contentResolver = context.contentResolver
            val fileName = FilePickerHelper.getFileName(contentResolver, uri)
            val fileBytes = contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
            viewModel.previewImport(setId, fileName, fileBytes)
        }
    }

    Scaffold(
        topBar = {
            AppToolbar(
                title = "Import Vocabulary",
                subtitle = "Preview CSV data before saving",
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
                contentPadding = PaddingValues(
                    start = AppDimens.ScreenPadding,
                    end = AppDimens.ScreenPadding,
                    top = 12.dp,
                    bottom = AppDimens.SectionSpacing
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    AppCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
                            AppSectionHeader(
                                title = "Select CSV file",
                                subtitle = "Choose a file from Downloads"
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = selectedFileName ?: "No file selected",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            SecondaryButton(
                                text = "Choose CSV",
                                leadingIcon = Icons.Default.UploadFile,
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    launcher.launch(FilePickerHelper.csvMimeTypes)
                                }
                            )
                        }
                    }
                }

                when (previewState) {
                    is ImportPreviewState.Loading -> {
                        item {
                            LoadingCard("Parsing CSV file...")
                        }
                    }
                    is ImportPreviewState.Error -> {
                        val message = (previewState as ImportPreviewState.Error).message
                        item {
                            MessageCard("Preview error", message)
                        }
                    }
                    is ImportPreviewState.Success -> {
                        val preview = (previewState as ImportPreviewState.Success).preview
                        item {
                            PreviewSummaryCard(preview)
                        }
                        items(preview.previewItems) { item ->
                            AppCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
                                    Text(
                                        text = item.word,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = item.meaning,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (item.pronunciation.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = item.pronunciation,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                    ImportPreviewState.Idle -> Unit
                }

                when (importState) {
                    is ImportState.Loading -> {
                        item {
                            LoadingCard("Importing vocabulary...")
                        }
                    }
                    is ImportState.Error -> {
                        val message = (importState as ImportState.Error).message
                        item {
                            MessageCard("Import error", message)
                        }
                    }
                    is ImportState.Success -> {
                        val result = (importState as ImportState.Success).result
                        item {
                            AppCard(modifier = Modifier.fillMaxWidth()) {
                                Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
                                    AppSectionHeader(
                                        title = "Import complete",
                                        subtitle = "Summary"
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Imported: ${result.importedCount}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Duplicates: ${result.duplicateCount}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Failed: ${result.failedCount}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                    ImportState.Idle -> Unit
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SecondaryButton(
                            text = "Cancel",
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.weight(1f)
                        )
                        PrimaryButton(
                            text = "Import",
                            onClick = { viewModel.confirmImport(setId) },
                            enabled = previewState is ImportPreviewState.Success
                                    && importState !is ImportState.Loading,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PreviewSummaryCard(preview: ImportPreview) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
            AppSectionHeader(
                title = "Preview summary",
                subtitle = preview.fileName
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Total rows: ${preview.totalRows}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Valid rows: ${preview.validRows}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Invalid rows: ${preview.invalidRows}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun LoadingCard(message: String) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(AppDimens.CardPadding)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.height(24.dp),
                strokeWidth = 3.dp
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun MessageCard(title: String, message: String) {
    AppCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(AppDimens.CardPadding)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
