package com.example.project_enlishlearning.data.importexport

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class VocabularyImportParser {
    fun parseCsv(
        inputStream: InputStream,
        fileName: String
    ): ImportPreview {
        val reader = BufferedReader(InputStreamReader(inputStream))
        val lines = reader.readLines().filter { it.isNotBlank() }
        if (lines.isEmpty()) {
            return ImportPreview(
                fileName = fileName,
                totalRows = 0,
                validRows = 0,
                invalidRows = 0,
                previewItems = emptyList(),
                validItems = emptyList(),
                errors = listOf("Empty CSV file")
            )
        }

        val dataLines = if (isHeaderLine(lines.first())) {
            lines.drop(1)
        } else {
            lines
        }

        val validItems = mutableListOf<ParsedVocabularyRow>()
        val errors = mutableListOf<String>()
        var invalidCount = 0

        dataLines.forEachIndexed { index, rawLine ->
            val lineNumber = index + 1
            val columns = parseCsvLine(rawLine)
            val word = columns.getOrNull(0)?.trim().orEmpty()
            val pronunciation = columns.getOrNull(1)?.trim().orEmpty()
            val meaning  = columns.getOrNull(2)?.trim().orEmpty()
            val example = columns.getOrNull(3)?.trim().orEmpty()

            if (word.isBlank() || meaning.isBlank()) {
                invalidCount += 1
                errors.add("Row $lineNumber is missing required fields")
            } else {
                validItems.add(
                    ParsedVocabularyRow(
                        word = word,
                        meaning = meaning,
                        pronunciation = pronunciation,
                        example = example
                    )
                )
            }
        }

        val previewItems = validItems.take(10).map {
            ImportPreviewItem(
                word = it.word,
                meaning = it.meaning,
                pronunciation = it.pronunciation
            )
        }

        return ImportPreview(
            fileName = fileName,
            totalRows = dataLines.size,
            validRows = validItems.size,
            invalidRows = invalidCount,
            previewItems = previewItems,
            validItems = validItems,
            errors = errors
        )
    }

    private fun isHeaderLine(line: String): Boolean {
        val columns = parseCsvLine(line).map { it.lowercase().trim() }
        return columns.contains("word") && columns.contains("meaning")
    }

    private fun parseCsvLine(line: String): List<String> {
        val result = mutableListOf<String>()
        val current = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < line.length) {
            val char = line[i]
            when {
                char == '"' -> {
                    val nextChar = line.getOrNull(i + 1)
                    if (inQuotes && nextChar == '"') {
                        current.append('"')
                        i += 1
                    } else {
                        inQuotes = !inQuotes
                    }
                }
                char == ',' && !inQuotes -> {
                    result.add(current.toString())
                    current.clear()
                }
                else -> current.append(char)
            }
            i += 1
        }
        result.add(current.toString())
        return result
    }
}
