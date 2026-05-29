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

    // Thêm một từ vựng mới vào bộ từ vựng
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: VocabularyWordEntity): Long

    // Cập nhật thông tin từ vựng (Ví dụ: đổi trạng thái New -> Learning hoặc bấm thích/hủy thích)
    @Update
    suspend fun updateWord(word: VocabularyWordEntity): Int

    // Xóa một từ vựng ra khỏi bộ
    @Delete
    suspend fun deleteWord(word: VocabularyWordEntity): Int
}