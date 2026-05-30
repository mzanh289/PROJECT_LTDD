package com.example.project_enlishlearning.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project_enlishlearning.data.local.entity.UserProfileEntity
import com.example.project_enlishlearning.data.repository.UserProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: UserProfileRepository,
    private val userId: String
) : ViewModel() {

    val profile = repository.getProfile(userId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun saveProfile(
        email: String,
        displayName: String,
        avatarUrl: String = "",
        englishLevel: String,
        learningGoal: String,
        dailyNewWordTarget: Int,
        dailyReviewTarget: Int
    ) {
        viewModelScope.launch {
            repository.saveProfile(
                UserProfileEntity(
                    userId = userId,
                    email = email,
                    displayName = displayName,
                    avatarUrl = avatarUrl,
                    englishLevel = englishLevel,
                    learningGoal = learningGoal,
                    dailyNewWordTarget = dailyNewWordTarget,
                    dailyReviewTarget = dailyReviewTarget,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }
}

class ProfileViewModelFactory(
    private val repository: UserProfileRepository,
    private val userId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository, userId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}