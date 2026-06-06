package com.example.project_enlishlearning.data.repository

import com.example.project_enlishlearning.data.local.dao.LearningProgressDao
import com.example.project_enlishlearning.data.local.dao.VocabularyDao
import com.example.project_enlishlearning.data.local.entity.LearningProgressEntity
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import com.example.project_enlishlearning.utils.FirebaseManager
import kotlinx.coroutines.flow.Flow

class FlashcardRepository(
    private val learningProgressDao: LearningProgressDao,
    private val vocabularyDao: VocabularyDao
) {
    private val currentUserId: String
        get() = FirebaseManager.auth.currentUser?.uid ?: ""

    fun getDifficultWordsBySet(
        userId: String,
        setId: Int
    ): Flow<List<VocabularyWordEntity>> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return learningProgressDao.getReviewWordsBySet(
            userId = uid,
            setId = setId
        )
    }

    fun getDueReviewWords(
        userId: String
    ): Flow<List<VocabularyWordEntity>> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return learningProgressDao.getDueReviewWords(uid)
    }

    fun getWordsBySetId(setId: Int): Flow<List<VocabularyWordEntity>> {
        return vocabularyDao.getWordsBySetIdAndUserId(setId, currentUserId)
    }

    suspend fun getProgressByWord(userId: String, wordId: Long): LearningProgressEntity? {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return learningProgressDao.getProgressByWord(uid, wordId)
    }

    suspend fun upsertProgress(progress: LearningProgressEntity) {
        val uid = FirebaseManager.auth.currentUser?.uid ?: progress.userId
        learningProgressDao.upsertProgress(progress.copy(userId = uid))
    }

    suspend fun updateWord(word: VocabularyWordEntity): Int {
        val uid = FirebaseManager.auth.currentUser?.uid ?: word.userId
        return vocabularyDao.updateWord(word.copy(userId = uid))
    }

    suspend fun getReviewDueCount(userId: String): Int {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return learningProgressDao.getReviewDueCount(uid)
    }

    fun getUnlearnedWordsBySet(
        userId: String,
        setId: Int
    ): Flow<List<VocabularyWordEntity>> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return learningProgressDao.getUnlearnedWordsBySet(uid, setId)
    }

    suspend fun updateSetProgress(
        userId: String,
        setId: Int
    ) {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        val totalWords = learningProgressDao.countTotalWordsBySet(uid, setId)

        val learnedWords = learningProgressDao.countLearnedWordsBySet(
            userId = uid,
            setId = setId
        )

        val progress = if (totalWords == 0) {
            0
        } else {
            ((learnedWords.toFloat() / totalWords.toFloat()) * 100).toInt()
        }

        vocabularyDao.updateSetProgress(
            setId = setId,
            progress = progress
        )
    }
}