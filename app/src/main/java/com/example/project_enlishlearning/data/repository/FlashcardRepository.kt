package com.example.project_enlishlearning.data.repository

import com.example.project_enlishlearning.data.local.dao.LearningProgressDao
import com.example.project_enlishlearning.data.local.dao.VocabularyDao
import com.example.project_enlishlearning.data.local.entity.LearningProgressEntity
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import kotlinx.coroutines.flow.Flow

class FlashcardRepository(
    private val learningProgressDao: LearningProgressDao,
    private val vocabularyDao: VocabularyDao
) {
    fun getDifficultWordsBySet(
        userId: String,
        setId: Int
    ): Flow<List<VocabularyWordEntity>> {
        return learningProgressDao.getReviewWordsBySet(
            userId = userId,
            setId = setId
        )
    }

    fun getDueReviewWords(
        userId: String
    ): Flow<List<VocabularyWordEntity>> {
        return learningProgressDao.getDueReviewWords(userId)
    }

    fun getWordsBySetId(setId: Int): Flow<List<VocabularyWordEntity>> {
        return vocabularyDao.getWordsBySetId(setId)
    }

    suspend fun getProgressByWord(userId: String, wordId: Long): LearningProgressEntity? {
        return learningProgressDao.getProgressByWord(userId, wordId)
    }

    suspend fun upsertProgress(progress: LearningProgressEntity) {
        learningProgressDao.upsertProgress(progress)
    }

    suspend fun updateWord(word: VocabularyWordEntity): Int {
        return vocabularyDao.updateWord(word)
    }

    suspend fun getReviewDueCount(userId: String): Int {
        return learningProgressDao.getReviewDueCount(userId)
    }
}