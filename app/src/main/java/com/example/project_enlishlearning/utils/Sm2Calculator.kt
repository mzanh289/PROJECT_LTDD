package com.example.project_enlishlearning.utils

import com.example.project_enlishlearning.data.local.entity.LearningProgressEntity
import kotlin.math.max
import kotlin.math.roundToInt

enum class ReviewRating(val quality: Int) {
    AGAIN(0),
    HARD(3),
    GOOD(4),
    EASY(5)
}

object Sm2Calculator {

    fun calculate(
        oldProgress: LearningProgressEntity?,
        userId: String,
        wordId: Long,
        rating: ReviewRating
    ): LearningProgressEntity {
        val now = System.currentTimeMillis()

        val oldEaseFactor = oldProgress?.easeFactor ?: 2.5
        val oldRepetition = oldProgress?.repetition ?: 0
        val oldInterval = oldProgress?.intervalDays ?: 0
        val oldCorrect = oldProgress?.correctCount ?: 0
        val oldWrong = oldProgress?.wrongCount ?: 0

        val quality = rating.quality
        val isRemembered = rating == ReviewRating.GOOD || rating == ReviewRating.EASY
        val needsReview = rating == ReviewRating.AGAIN || rating == ReviewRating.HARD

        val newEaseFactor = calculateEaseFactor(oldEaseFactor, quality)

        val newRepetition: Int
        val newInterval: Int

        if (rating == ReviewRating.AGAIN) {
            newRepetition = 0
            newInterval = 0
        } else {
            newRepetition = oldRepetition + 1

            newInterval = when (newRepetition) {
                1 -> 1
                2 -> 6
                else -> max(1, (oldInterval * newEaseFactor).roundToInt())
            }
        }

        val nextReviewAt = when (rating) {
            ReviewRating.AGAIN -> now + 10 * 60 * 1000L
            ReviewRating.HARD -> now + 1 * 24 * 60 * 60 * 1000L
            ReviewRating.GOOD -> now + newInterval * 24L * 60 * 60 * 1000
            ReviewRating.EASY -> now + max(newInterval, 3) * 24L * 60 * 60 * 1000
        }

        val status = when {
            needsReview -> "LEARNING"
            newRepetition >= 5 -> "MASTERED"
            newRepetition >= 2 -> "REVIEWING"
            else -> "LEARNING"
        }

        return LearningProgressEntity(
            progressId = oldProgress?.progressId ?: 0,
            userId = userId,
            wordId = wordId,
            status = status,
            easeFactor = newEaseFactor,
            intervalDays = newInterval,
            repetition = newRepetition,
            correctCount = oldCorrect + if (isRemembered) 1 else 0,
            wrongCount = oldWrong + if (needsReview) 1 else 0,
            lastReviewedAt = now,
            nextReviewAt = nextReviewAt,
            createdAt = oldProgress?.createdAt ?: now,
            updatedAt = now
        )
    }

    private fun calculateEaseFactor(
        oldEaseFactor: Double,
        quality: Int
    ): Double {
        val newEaseFactor = oldEaseFactor + (
                0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02)
                )

        return max(1.3, newEaseFactor)
    }
}