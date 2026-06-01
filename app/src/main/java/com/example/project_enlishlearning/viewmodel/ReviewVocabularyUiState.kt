package com.example.project_enlishlearning.viewmodel

import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity

data class ReviewVocabularyUiState(
    val isLoading: Boolean = false,
    val difficultWords: List<VocabularyWordEntity> = emptyList()
)