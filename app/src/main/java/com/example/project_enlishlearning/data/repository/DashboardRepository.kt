package com.example.project_enlishlearning.data.repository

import com.example.project_enlishlearning.data.local.dao.LearningProgressDao
import com.example.project_enlishlearning.data.local.dao.VocabularyDao
import com.example.project_enlishlearning.utils.FirebaseManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.time.LocalDate

data class DailyReviewCount(
    val day: String,
    val reviewCount: Int
)

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

    fun getWeeklyReviewCounts(userId: String): Flow<List<DailyReviewCount>> {
        val minTime = System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000L
        return learningProgressDao.getDailyReviewCounts(userId, minTime)
    }

    fun getRetentionRate(userId: String): Flow<Float> {
        return combine(
            learningProgressDao.getTotalCorrectAnswers(userId),
            learningProgressDao.getTotalWrongAnswers(userId)
        ) { correctCount, wrongCount ->
            val correct = correctCount ?: 0
            val wrong = wrongCount ?: 0
            val total = correct + wrong
            if (total == 0) 0f else correct.toFloat() / total.toFloat()
        }
    }

    fun getLearningStreak(userId: String): Flow<Int> {
        return getWeeklyReviewCounts(userId).map { counts ->
            val daysWithActivity = counts.mapNotNull {
                runCatching { LocalDate.parse(it.day) }.getOrNull()
            }.toSet()

            var streak = 0
            var currentDay = LocalDate.now()

            while (daysWithActivity.contains(currentDay)) {
                streak++
                currentDay = currentDay.minusDays(1)
            }

            streak
        }
    }
}