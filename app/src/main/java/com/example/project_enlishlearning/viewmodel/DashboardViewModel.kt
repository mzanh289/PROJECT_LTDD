package com.example.project_enlishlearning.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project_enlishlearning.data.repository.DashboardRepository
import com.example.project_enlishlearning.data.repository.DailyReviewCount
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(
    private val repository: DashboardRepository,
    private val userId: String
) : ViewModel() {

    val totalVocabularySets = repository.getTotalVocabularySets(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalVocabularyWords = repository.getTotalVocabularyWords(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val totalLearningWords = repository.getTotalLearningWords(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val newWords = repository.getNewWords(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val learningWords = repository.getLearningWords(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val reviewingWords = repository.getReviewingWords(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val masteredWords = repository.getMasteredWords(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val reviewDueToday = repository.getReviewDueToday(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val weeklyReviewCounts: kotlinx.coroutines.flow.StateFlow<List<DailyReviewCount>> =
        repository.getWeeklyReviewCounts(userId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val retentionRate = repository.getRetentionRate(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    val learningStreak = repository.getLearningStreak(userId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)
}

class DashboardViewModelFactory(
    private val repository: DashboardRepository,
    private val userId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(repository, userId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}