package com.example.project_enlishlearning.data.importexport

data class ImportPreviewItem(
    val word: String,
    val meaning: String,
    val pronunciation: String
)

data class ParsedVocabularyRow(
    val word: String,
    val meaning: String,
    val pronunciation: String,
    val example: String
)

data class ImportPreview(
    val fileName: String,
    val totalRows: Int,
    val validRows: Int,
    val invalidRows: Int,
    val previewItems: List<ImportPreviewItem>,
    val validItems: List<ParsedVocabularyRow>,
    val errors: List<String> = emptyList()
)
