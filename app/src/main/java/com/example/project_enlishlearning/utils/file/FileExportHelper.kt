package com.example.project_enlishlearning.utils.file

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FileExportHelper {
    suspend fun writeCsvToUri(
        context: Context,
        uri: Uri,
        csvContent: String
    ) = withContext(Dispatchers.IO) {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(csvContent.toByteArray())
        } ?: throw IllegalStateException("Unable to write export file")
    }
}
