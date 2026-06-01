package com.example.project_enlishlearning.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_enlishlearning.data.local.database.AppDatabase
import com.example.project_enlishlearning.data.repository.FlashcardRepository
import com.example.project_enlishlearning.utils.FirebaseManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReviewVocabularyViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)

    private val repository = FlashcardRepository(
        learningProgressDao = database.learningProgressDao()
    )

    private val currentUserId: String
        get() = FirebaseManager.auth.currentUser?.uid ?: "local_user"

    private val _uiState = MutableStateFlow(ReviewVocabularyUiState())
    val uiState: StateFlow<ReviewVocabularyUiState> = _uiState.asStateFlow()

    fun loadDifficultWords(setId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            repository.getDifficultWordsBySet(
                userId = currentUserId,
                setId = setId
            ).collect { words ->
                _uiState.value = ReviewVocabularyUiState(
                    isLoading = false,
                    difficultWords = words
                )
            }
        }
    }
}