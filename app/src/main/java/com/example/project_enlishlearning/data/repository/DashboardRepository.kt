package com.example.project_enlishlearning.data.repository

import com.example.project_enlishlearning.data.local.dao.LearningProgressDao
import com.example.project_enlishlearning.data.local.dao.VocabularyDao
import com.example.project_enlishlearning.utils.FirebaseManager
import kotlinx.coroutines.flow.Flow

class DashboardRepository(
    private val vocabularyDao: VocabularyDao,
    private val learningProgressDao: LearningProgressDao
) {

    fun getTotalVocabularySets(userId: String): Flow<Int> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return vocabularyDao.countVocabularySets(uid)
    }

    fun getTotalVocabularyWords(userId: String): Flow<Int> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return vocabularyDao.countVocabularyWords(uid)
    }

    fun getTotalLearningWords(userId: String): Flow<Int> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return learningProgressDao.countAllLearningWords(uid)
    }

    fun getNewWords(userId: String): Flow<Int> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return learningProgressDao.countNewWords(uid)
    }

    fun getLearningWords(userId: String): Flow<Int> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return learningProgressDao.countLearningWords(uid)
    }

    fun getReviewingWords(userId: String): Flow<Int> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return learningProgressDao.countReviewingWords(uid)
    }

    fun getMasteredWords(userId: String): Flow<Int> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return learningProgressDao.countMasteredWords(uid)
    }

    fun getReviewDueToday(userId: String): Flow<Int> {
        val uid = FirebaseManager.auth.currentUser?.uid ?: userId
        return learningProgressDao.countReviewDueToday(uid)
    }
}