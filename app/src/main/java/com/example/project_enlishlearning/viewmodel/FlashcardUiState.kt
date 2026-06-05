package com.example.project_enlishlearning.viewmodel

import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import com.example.project_enlishlearning.utils.ReviewRating

data class FlashcardAnswer(
    val word: VocabularyWordEntity,
    val rating: ReviewRating
)

data class FlashcardUiState(
    val words: List<VocabularyWordEntity> = emptyList(),
    val currentIndex: Int = 0,
    val isFlipped: Boolean = false,
    val correctCount: Int = 0,
    val wrongCount: Int = 0,
    val isLoading: Boolean = false,
    val isFinished: Boolean = false,

    // Lưu tạm
    val pendingAnswers: List<FlashcardAnswer> = emptyList()
) {
    val currentWord: VocabularyWordEntity?
        get() = words.getOrNull(currentIndex)

    val totalWords: Int
        get() = words.size

    val progress: Float
        get() {
            if (words.isEmpty()) return 0f
            return (currentIndex + 1).toFloat() / words.size.toFloat()
        }
}