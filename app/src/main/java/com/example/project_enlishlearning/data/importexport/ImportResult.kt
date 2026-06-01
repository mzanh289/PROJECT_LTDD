package com.example.project_enlishlearning.data.importexport

data class ImportResult(
    val importedCount: Int,
    val duplicateCount: Int,
    val failedCount: Int,
    val errors: List<String> = emptyList()
)
