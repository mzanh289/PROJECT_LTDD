package com.example.project_enlishlearning.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
@Entity(
    tableName = "vocabulary_words",
    foreignKeys = [
        ForeignKey(
            entity = VocabularySetEntity::class,
            parentColumns = ["setId"],
            childColumns = ["setId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VocabularyWordEntity(
    @PrimaryKey(autoGenerate = true)
    val wordId: Int = 0,
    val setId: Int, // Khóa ngoại liên kết với bảng trên
    val word: String,
    val pronunciation: String,
    val meaning: String,
    val example: String,
    val status: String = "New",
    val isFavorite: Boolean = false
)