package com.example.project_enlishlearning.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.project_enlishlearning.data.local.database.AppDatabase
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import com.example.project_enlishlearning.data.repository.FlashcardRepository
import com.example.project_enlishlearning.navigation.Screen
import com.example.project_enlishlearning.utils.FirebaseManager
import com.example.project_enlishlearning.utils.ReviewRating
import com.example.project_enlishlearning.utils.Sm2Calculator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FlashcardViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = FlashcardRepository(
        learningProgressDao = database.learningProgressDao(),
        vocabularyDao = database.vocabularyDao()
    )

    private val currentUserId: String
        get() = FirebaseManager.auth.currentUser?.uid ?: "local_user"

    private val _uiState = MutableStateFlow(FlashcardUiState())
    val uiState: StateFlow<FlashcardUiState> = _uiState.asStateFlow()

    // LOAD FLASHCARD THƯỜNG: HỌC TOÀN BỘ TỪ TRONG BỘ
    fun loadFlashcards(setId: Int) {
        viewModelScope.launch {
            resetLearningState()

            repository.getWordsBySetId(setId).collect { words ->
                _uiState.value = _uiState.value.copy(
                    words = words,
                    isLoading = false
                )
            }
        }
    }

    // LOAD FLASHCARD REVIEW: CHỈ HỌC LẠI CÁC TỪ KHÓ / TỪ ĐẾN HẠN
    fun loadReviewFlashcards(setId: Int) {
        viewModelScope.launch {
            resetLearningState()

            val flow = if (setId == Screen.ReviewVocabulary.GLOBAL_DUE_REVIEW_SET_ID) {
                repository.getDueReviewWords(userId = currentUserId)
            } else {
                repository.getDifficultWordsBySet(
                    userId = currentUserId,
                    setId = setId
                )
            }

            flow.collect { words ->
                _uiState.value = _uiState.value.copy(
                    words = words,
                    isLoading = false
                )
            }
        }
    }

    private fun resetLearningState() {
        _uiState.value = FlashcardUiState(
            isLoading = true,
            currentIndex = 0,
            isFlipped = false,
            correctCount = 0,
            wrongCount = 0,
            isFinished = false
        )
    }

    // LẬT THẺ
    fun flipCard() {
        _uiState.value = _uiState.value.copy(
            isFlipped = !_uiState.value.isFlipped
        )
    }

    // NGƯỜI DÙNG CHỌN AGAIN / HARD / GOOD / EASY
    fun answerCurrentWord(rating: ReviewRating) {
        val currentWord = _uiState.value.currentWord ?: return

        viewModelScope.launch {
            // 1. Tính SM2 và lưu LearningProgress
            val newStatus = updateLearningProgress(
                word = currentWord,
                rating = rating
            )

            // 2. Cập nhật status của từ vựng theo kết quả SM2
            updateWordStatus(
                word = currentWord,
                newStatus = newStatus
            )

            // 3. Cập nhật số từ remembered / need review trên UI
            updateCount(rating)

            // 4. Qua từ tiếp theo hoặc kết thúc
            goToNextWord()
        }
    }

    // CẬP NHẬT LEARNING PROGRESS BẰNG SM2
    private suspend fun updateLearningProgress(
        word: VocabularyWordEntity,
        rating: ReviewRating
    ): String {
        val oldProgress = repository.getProgressByWord(
            userId = currentUserId,
            wordId = word.wordId
        )

        val newProgress = Sm2Calculator.calculate(
            oldProgress = oldProgress,
            userId = currentUserId,
            wordId = word.wordId,
            rating = rating
        )

        repository.upsertProgress(newProgress)

        return newProgress.status
    }

    // CẬP NHẬT STATUS CỦA TỪ
    private suspend fun updateWordStatus(
        word: VocabularyWordEntity,
        newStatus: String
    ) {
        repository.updateWord(
            word.copy(status = newStatus)
        )
    }

    // ĐẾM KẾT QUẢ PHIÊN HỌC
    private fun updateCount(rating: ReviewRating) {
        val currentState = _uiState.value

        val isRemembered =
            rating == ReviewRating.GOOD || rating == ReviewRating.EASY

        _uiState.value = currentState.copy(
            correctCount = currentState.correctCount + if (isRemembered) 1 else 0,
            wrongCount = currentState.wrongCount + if (isRemembered) 0 else 1
        )
    }

    // CHUYỂN SANG TỪ TIẾP THEO
    private fun goToNextWord() {
        val currentState = _uiState.value
        val nextIndex = currentState.currentIndex + 1

        if (nextIndex >= currentState.words.size) {
            _uiState.value = currentState.copy(
                isFinished = true,
                isFlipped = false
            )
        } else {
            _uiState.value = currentState.copy(
                currentIndex = nextIndex,
                isFlipped = false
            )
        }
    }
}