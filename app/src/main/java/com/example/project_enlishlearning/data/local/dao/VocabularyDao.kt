package com.example.project_enlishlearning.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.project_enlishlearning.data.local.entity.VocabularySetEntity
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface VocabularyDao {
    @Query("SELECT * FROM vocabulary_sets ORDER BY setId ASC")
    fun getAllSets(): Flow<List<VocabularySetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(vocabularySet: VocabularySetEntity): Long

    @Update
    suspend fun updateSet(vocabularySet: VocabularySetEntity): Int

    // Xóa một bộ từ vựng
    @Delete
    suspend fun deleteSet(vocabularySet: VocabularySetEntity): Int

    @Query("SELECT * FROM vocabulary_words WHERE setId = :targetSetId")
    fun getWordsBySetId(targetSetId: Int): Flow<List<VocabularyWordEntity>>

    @Query("SELECT * FROM vocabulary_words WHERE setId = :targetSetId")
    suspend fun getWordsBySetIdOnce(targetSetId: Int): List<VocabularyWordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: VocabularyWordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<VocabularyWordEntity>): List<Long>

    @Update
    suspend fun updateWord(word: VocabularyWordEntity): Int

    @Delete
    suspend fun deleteWord(word: VocabularyWordEntity): Int

    @Query("SELECT * FROM vocabulary_words WHERE wordId = :id")
    suspend fun getWordById(id: Long): VocabularyWordEntity?

    @Query("SELECT * FROM vocabulary_sets WHERE userId = :userId ORDER BY setId ASC")
    fun getAllSetsByUserId(userId: String): Flow<List<VocabularySetEntity>>

    @Query("UPDATE vocabulary_sets SET totalWords = totalWords + 1 WHERE setId = :setId")
    suspend fun incrementTotalWords(setId: Int)

    @Query("UPDATE vocabulary_sets SET totalWords = totalWords + :delta WHERE setId = :setId")
    suspend fun incrementTotalWordsBy(setId: Int, delta: Int)

    @Query("UPDATE vocabulary_sets SET totalWords = totalWords - 1 WHERE setId = :setId AND totalWords > 0")
    suspend fun decrementTotalWords(setId: Int)
    @Query("SELECT COUNT(*) FROM vocabulary_sets WHERE userId = :userId")
    fun countVocabularySets(userId: String): Flow<Int>

    @Query("SELECT COUNT(*) FROM vocabulary_words WHERE userId = :userId")
    fun countVocabularyWords(userId: String): Flow<Int>

    @Query("SELECT * FROM vocabulary_words WHERE setId = :targetSetId AND userId = :userId")
    fun getWordsBySetIdAndUserId(targetSetId: Int, userId: String): Flow<List<VocabularyWordEntity>>

    @Query("SELECT * FROM vocabulary_words WHERE setId = :targetSetId AND userId = :userId")
    suspend fun getWordsBySetIdOnceAndUserId(targetSetId: Int, userId: String): List<VocabularyWordEntity>

    @Query("""
    UPDATE vocabulary_sets
    SET progress = :progress
    WHERE setId = :setId
""")
    suspend fun updateSetProgress(
        setId: Int,
        progress: Int
    )
}