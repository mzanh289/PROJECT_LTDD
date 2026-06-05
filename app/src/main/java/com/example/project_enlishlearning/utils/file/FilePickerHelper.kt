package com.example.project_enlishlearning.utils.file

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns

object FilePickerHelper {
    val csvMimeTypes = arrayOf(
        "text/csv",
        "text/comma-separated-values",
        "application/csv",
        "text/plain"
    )

    fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val nameIndex = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME) ?: -1
        val name = if (cursor != null && cursor.moveToFirst() && nameIndex >= 0) {
            cursor.getString(nameIndex)
        } else {
            uri.lastPathSegment ?: "vocabulary.csv"
        }
        cursor?.close()
        return name
    }
}
