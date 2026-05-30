package com.example.project_enlishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "learning_progress",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["wordId"]),
        Index(value = ["nextReviewAt"])
    ]
)
data class LearningProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val progressId: Long = 0,

    val userId: String,
    val wordId: Long,

    val status: String = "NEW",

    val easeFactor: Double = 2.5,
    val intervalDays: Int = 0,
    val repetition: Int = 0,

    val correctCount: Int = 0,
    val wrongCount: Int = 0,

    val lastReviewedAt: Long = 0L,
    val nextReviewAt: Long = 0L,

    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)