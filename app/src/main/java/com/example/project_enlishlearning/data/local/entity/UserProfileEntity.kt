package com.example.project_enlishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey
    val userId: String,

    val email: String = "",
    val displayName: String = "",
    val avatarUrl: String = "",

    val englishLevel: String = "A1",
    val learningGoal: String = "",
    val dailyNewWordTarget: Int = 10,
    val dailyReviewTarget: Int = 20,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)