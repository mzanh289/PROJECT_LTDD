package com.example.project_enlishlearning.data.repository

import com.example.project_enlishlearning.data.importexport.ImportPreview
import com.example.project_enlishlearning.data.importexport.ImportResult
import com.example.project_enlishlearning.data.importexport.VocabularyExportFormatter
import com.example.project_enlishlearning.data.importexport.VocabularyImportParser
import com.example.project_enlishlearning.data.local.dao.VocabularyDao
import com.example.project_enlishlearning.data.local.entity.VocabularySetEntity
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

class VocabularyRepository(
    private val vocabularyDao: VocabularyDao,
    private val importParser: VocabularyImportParser,
    private val exportFormatter: VocabularyExportFormatter
) {
    fun getAllSetsByUserId(userId: String): Flow<List<VocabularySetEntity>> {
        return vocabularyDao.getAllSetsByUserId(userId)
    }

    fun getWordsBySetId(setId: Int): Flow<List<VocabularyWordEntity>> {
        return vocabularyDao.getWordsBySetId(setId)
    }

    suspend fun insertSet(set: VocabularySetEntity): Long {
        return vocabularyDao.insertSet(set)
    }

    suspend fun updateSet(set: VocabularySetEntity): Int {
        return vocabularyDao.updateSet(set)
    }

    suspend fun deleteSet(set: VocabularySetEntity): Int {
        return vocabularyDao.deleteSet(set)
    }

    suspend fun insertWord(word: VocabularyWordEntity): Long {
        return vocabularyDao.insertWord(word)
    }

    suspend fun updateWord(word: VocabularyWordEntity): Int {
        return vocabularyDao.updateWord(word)
    }

    suspend fun deleteWord(word: VocabularyWordEntity): Int {
        return vocabularyDao.deleteWord(word)
    }

    suspend fun getWordById(wordId: Int): VocabularyWordEntity? {
        return vocabularyDao.getWordById(wordId)
    }

    suspend fun incrementTotalWords(setId: Int) {
        vocabularyDao.incrementTotalWords(setId)
    }

    suspend fun decrementTotalWords(setId: Int) {
        vocabularyDao.decrementTotalWords(setId)
    }

    suspend fun exportVocabularySet(setId: Int): String {
        val words = vocabularyDao.getWordsBySetIdOnce(setId)
        return exportFormatter.formatCsv(words)
    }

    suspend fun previewImport(
        inputStream: InputStream,
        fileName: String
    ): ImportPreview {
        return importParser.parseCsv(inputStream, fileName)
    }

    suspend fun importFromPreview(
        setId: Int,
        preview: ImportPreview
    ): ImportResult {
        val existingWords = vocabularyDao.getWordsBySetIdOnce(setId)
        val existingKeys = existingWords
            .map { normalizeKey(it.word, it.meaning) }
            .toMutableSet()

        var duplicateCount = 0
        val newWords = mutableListOf<VocabularyWordEntity>()

        preview.validItems.forEach { row ->
            val key = normalizeKey(row.word, row.meaning)
            if (existingKeys.contains(key)) {
                duplicateCount += 1
            } else {
                existingKeys.add(key)
                newWords.add(
                    VocabularyWordEntity(
                        setId = setId,
                        word = row.word,
                        meaning = row.meaning,
                        pronunciation = row.pronunciation,
                        example = row.example
                    )
                )
            }
        }

        if (newWords.isNotEmpty()) {
            vocabularyDao.insertWords(newWords)
            vocabularyDao.incrementTotalWordsBy(setId, newWords.size)
        }

        return ImportResult(
            importedCount = newWords.size,
            duplicateCount = duplicateCount,
            failedCount = preview.invalidRows,
            errors = preview.errors
        )
    }

    private fun normalizeKey(word: String, meaning: String): String {
        return "${word.trim().lowercase()}|${meaning.trim().lowercase()}"
    }
}
