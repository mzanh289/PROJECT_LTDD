package com.example.project_enlishlearning.data.repository

import com.example.project_enlishlearning.data.importexport.ImportPreview
import com.example.project_enlishlearning.data.importexport.ImportResult
import com.example.project_enlishlearning.data.importexport.VocabularyExportFormatter
import com.example.project_enlishlearning.data.importexport.VocabularyImportParser
import com.example.project_enlishlearning.data.local.dao.VocabularyDao
import com.example.project_enlishlearning.data.local.entity.VocabularySetEntity
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import com.example.project_enlishlearning.utils.FirebaseManager
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

class VocabularyRepository(
    private val vocabularyDao: VocabularyDao,
    private val importParser: VocabularyImportParser,
    private val exportFormatter: VocabularyExportFormatter
) {
    private val currentUserId: String
        get() = FirebaseManager.auth.currentUser?.uid ?: ""

    fun getAllSetsByUserId(userId: String): Flow<List<VocabularySetEntity>> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return vocabularyDao.getAllSetsByUserId(uid)
    }

    fun getWordsBySetId(setId: Int): Flow<List<VocabularyWordEntity>> {
        return vocabularyDao.getWordsBySetIdAndUserId(setId, currentUserId)
    }

    suspend fun seedDefaultVocabulary(userId: String, inputStream: InputStream) {
        val defaultSet = VocabularySetEntity(
            userId = userId,
            title = "Từ vựng cơ bản",
            description = "Bộ từ vựng mặc định dành cho người mới bắt đầu.",
            totalWords = 0,
            progress = 0
        )
        val setId = vocabularyDao.insertSet(defaultSet).toInt()

        val preview = importParser.parseCsv(inputStream, "default_vocabulary.csv")
        val newWords = mutableListOf<VocabularyWordEntity>()

        preview.validItems.forEach { row ->
            newWords.add(
                VocabularyWordEntity(
                    setId = setId,
                    userId = userId,
                    word = row.word,
                    meaning = row.meaning,
                    pronunciation = row.pronunciation,
                    example = row.example
                )
            )
        }

        if (newWords.isNotEmpty()) {
            vocabularyDao.insertWords(newWords)
            vocabularyDao.incrementTotalWordsBy(setId, newWords.size)
        }
    }

    suspend fun insertSet(set: VocabularySetEntity): Long {
        val uid = FirebaseManager.auth.currentUser?.uid ?: set.userId
        return vocabularyDao.insertSet(set.copy(userId = uid))
    }

    suspend fun updateSet(set: VocabularySetEntity): Int {
        val uid = FirebaseManager.auth.currentUser?.uid ?: set.userId
        return vocabularyDao.updateSet(set.copy(userId = uid))
    }

    suspend fun deleteSet(set: VocabularySetEntity): Int {
        return vocabularyDao.deleteSet(set)
    }

    suspend fun insertWord(word: VocabularyWordEntity): Long {
        val uid = FirebaseManager.auth.currentUser?.uid ?: word.userId
        return vocabularyDao.insertWord(word.copy(userId = uid))
    }

    suspend fun updateWord(word: VocabularyWordEntity): Int {
        val uid = FirebaseManager.auth.currentUser?.uid ?: word.userId
        return vocabularyDao.updateWord(word.copy(userId = uid))
    }

    suspend fun deleteWord(word: VocabularyWordEntity): Int {
        return vocabularyDao.deleteWord(word)
    }

    suspend fun getWordById(wordId: Long): VocabularyWordEntity? {
        val word = vocabularyDao.getWordById(wordId)
        if (word != null && word.userId != currentUserId) {
            return null
        }
        return word
    }

    suspend fun incrementTotalWords(setId: Int) {
        vocabularyDao.incrementTotalWords(setId)
    }

    suspend fun decrementTotalWords(setId: Int) {
        vocabularyDao.decrementTotalWords(setId)
    }

    suspend fun exportVocabularySet(setId: Int): String {
        val words = vocabularyDao.getWordsBySetIdOnceAndUserId(setId, currentUserId)
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
        val uid = currentUserId
        val existingWords = vocabularyDao.getWordsBySetIdOnceAndUserId(setId, uid)
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
                        userId = uid,
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
