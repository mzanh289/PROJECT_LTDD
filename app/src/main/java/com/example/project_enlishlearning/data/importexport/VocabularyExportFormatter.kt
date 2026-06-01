package com.example.project_enlishlearning.data.importexport

import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity

class VocabularyExportFormatter {
    fun formatCsv(words: List<VocabularyWordEntity>): String {
        val builder = StringBuilder()
        builder.append("word,meaning,pronunciation,example")
        builder.append('\n')
        words.forEach { word ->
            builder.append(toCsvLine(word))
            builder.append('\n')
        }
        return builder.toString()
    }

    private fun toCsvLine(word: VocabularyWordEntity): String {
        return listOf(
            escapeCsv(word.word),
            escapeCsv(word.meaning),
            escapeCsv(word.pronunciation),
            escapeCsv(word.example)
        ).joinToString(",")
    }

    private fun escapeCsv(value: String): String {
        val needsQuotes = value.contains(",") || value.contains("\n") || value.contains("\"")
        if (!needsQuotes) return value
        val escaped = value.replace("\"", "\"\"")
        return "\"$escaped\""
    }
}
