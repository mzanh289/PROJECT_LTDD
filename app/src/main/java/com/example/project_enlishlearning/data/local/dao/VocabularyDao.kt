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
    // =================================================================
    // 1. CÁC HÀM QUẢN LÝ BỘ TỪ VỰNG (VocabularySetEntity)
    // =================================================================

    // Lấy toàn bộ danh sách bộ từ vựng sắp xếp theo ID tăng dần
    @Query("SELECT * FROM vocabulary_sets ORDER BY setId ASC")
    fun getAllSets(): Flow<List<VocabularySetEntity>>

    // Thêm một bộ từ vựng mới
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSet(vocabularySet: VocabularySetEntity): Long

    // Cập nhật thông tin bộ từ vựng (Ví dụ: khi số từ tăng lên hoặc tiến độ học thay đổi)
    @Update
    suspend fun updateSet(vocabularySet: VocabularySetEntity): Int

    // Xóa một bộ từ vựng
    @Delete
    suspend fun deleteSet(vocabularySet: VocabularySetEntity): Int


    // =================================================================
    // 2. CÁC HÀM QUẢN LÝ TỪ VỰNG CHI TIẾT (VocabularyWordEntity)
    // =================================================================

    // Lấy ra tất cả từ vựng thuộc về một bộ cụ thể dựa vào setId
    @Query("SELECT * FROM vocabulary_words WHERE setId = :targetSetId")
    fun getWordsBySetId(targetSetId: Int): Flow<List<VocabularyWordEntity>>

    @Query("SELECT * FROM vocabulary_words WHERE setId = :targetSetId")
    suspend fun getWordsBySetIdOnce(targetSetId: Int): List<VocabularyWordEntity>

    // Thêm một từ vựng mới vào bộ từ vựng
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: VocabularyWordEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<VocabularyWordEntity>): List<Long>

    // Cập nhật thông tin từ vựng (Ví dụ: đổi trạng thái New -> Learning hoặc bấm thích/hủy thích)
    @Update
    suspend fun updateWord(word: VocabularyWordEntity): Int

    // Xóa một từ vựng ra khỏi bộ
    @Delete
    suspend fun deleteWord(word: VocabularyWordEntity): Int

    @Query("SELECT * FROM vocabulary_words WHERE wordId = :id")
    suspend fun getWordById(id: Int): VocabularyWordEntity?

    @Query("SELECT * FROM vocabulary_sets WHERE userId = :userId ORDER BY setId ASC")
    fun getAllSetsByUserId(userId: String): Flow<List<VocabularySetEntity>>

    // Tăng số lượng từ vựng lên 1
    @Query("UPDATE vocabulary_sets SET totalWords = totalWords + 1 WHERE setId = :setId")
    suspend fun incrementTotalWords(setId: Int)

    @Query("UPDATE vocabulary_sets SET totalWords = totalWords + :delta WHERE setId = :setId")
    suspend fun incrementTotalWordsBy(setId: Int, delta: Int)

    // Giảm số lượng từ vựng đi 1 (đảm bảo không bị âm)
    @Query("UPDATE vocabulary_sets SET totalWords = totalWords - 1 WHERE setId = :setId AND totalWords > 0")
    suspend fun decrementTotalWords(setId: Int)
    @Query("SELECT COUNT(*) FROM vocabulary_sets")
    fun countVocabularySets(): Flow<Int>

    @Query("SELECT COUNT(*) FROM vocabulary_words")
    fun countVocabularyWords(): Flow<Int>

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