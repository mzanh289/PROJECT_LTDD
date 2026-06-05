package com.example.project_enlishlearning.data.repository

import com.example.project_enlishlearning.data.local.dao.LearningProgressDao
import com.example.project_enlishlearning.data.local.dao.VocabularyDao
import kotlinx.coroutines.flow.Flow

class DashboardRepository(
    private val vocabularyDao: VocabularyDao,
    private val learningProgressDao: LearningProgressDao
) {

    fun getTotalVocabularySets(userId: String): Flow<Int> {
        return vocabularyDao.countVocabularySets()
    }

    fun getTotalVocabularyWords(userId: String): Flow<Int> {
        return vocabularyDao.countVocabularyWords()
    }

    fun getTotalLearningWords(userId: String): Flow<Int> {
        return learningProgressDao.countAllLearningWords(userId)
    }

    fun getNewWords(userId: String): Flow<Int> {
        return learningProgressDao.countNewWords(userId)
    }

    fun getLearningWords(userId: String): Flow<Int> {
        return learningProgressDao.countLearningWords(userId)
    }

    fun getReviewingWords(userId: String): Flow<Int> {
        return learningProgressDao.countReviewingWords(userId)
    }

    fun getMasteredWords(userId: String): Flow<Int> {
        return learningProgressDao.countMasteredWords(userId)
    }

    fun getReviewDueToday(userId: String): Flow<Int> {
        return learningProgressDao.countReviewDueToday(userId)
    }
}