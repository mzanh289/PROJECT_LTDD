package com.example.project_enlishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vocabulary_sets")
data class VocabularySetEntity(
    @PrimaryKey(autoGenerate = true)
    val setId: Int = 0,
    val userId: String,
    val title: String,
    val description: String,
    val totalWords: Int = 0,
    val progress: Int = 0
)