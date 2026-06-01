package com.example.project_enlishlearning.data.repository

import com.example.project_enlishlearning.data.local.dao.LearningProgressDao
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import kotlinx.coroutines.flow.Flow

class FlashcardRepository(
    private val learningProgressDao: LearningProgressDao
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
}