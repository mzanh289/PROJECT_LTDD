package com.example.project_enlishlearning.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.project_enlishlearning.data.local.entity.LearningProgressEntity
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
import com.example.project_enlishlearning.data.repository.DailyReviewCount
import kotlinx.coroutines.flow.Flow

@Dao
interface LearningProgressDao {

    @Query("""
        SELECT * FROM learning_progress 
        WHERE userId = :userId 
        ORDER BY updatedAt DESC
    """)
    fun getAllProgressByUser(userId: String): Flow<List<LearningProgressEntity>>

    @Query("""
        SELECT * FROM learning_progress 
        WHERE userId = :userId AND wordId = :wordId 
        LIMIT 1
    """)
    suspend fun getProgressByWord(
        userId: String,
        wordId: Long
    ): LearningProgressEntity?

    @Upsert
    suspend fun upsertProgress(progress: LearningProgressEntity)

    @Query("""
        SELECT COUNT(*) FROM learning_progress 
        WHERE userId = :userId
    """)
    fun countAllLearningWords(userId: String): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM learning_progress 
        WHERE userId = :userId AND status = 'NEW'
    """)
    fun countNewWords(userId: String): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM learning_progress 
        WHERE userId = :userId AND status = 'LEARNING'
    """)
    fun countLearningWords(userId: String): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM learning_progress 
        WHERE userId = :userId AND status = 'REVIEWING'
    """)
    fun countReviewingWords(userId: String): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM learning_progress 
        WHERE userId = :userId AND status = 'MASTERED'
    """)
    fun countMasteredWords(userId: String): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM learning_progress 
        WHERE userId = :userId 
        AND nextReviewAt <= :currentTime
        AND status != 'MASTERED'
    """)
    fun countReviewDueToday(
        userId: String,
        currentTime: Long = System.currentTimeMillis()
    ): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM learning_progress 
        WHERE userId = :userId 
        AND nextReviewAt <= :currentTime
        AND status != 'MASTERED'
    """)
    suspend fun getReviewDueCount(
        userId: String,
        currentTime: Long = System.currentTimeMillis()
    ): Int

    @Query("""
        SELECT strftime('%Y-%m-%d', lastReviewedAt / 1000, 'unixepoch', 'localtime') AS day,
               COUNT(*) AS reviewCount
        FROM learning_progress
        WHERE userId = :userId
          AND lastReviewedAt >= :minTime
        GROUP BY day
        ORDER BY day ASC
    """)
    fun getDailyReviewCounts(
        userId: String,
        minTime: Long
    ): Flow<List<DailyReviewCount>>

    @Query("""
        SELECT vw.* FROM vocabulary_words vw
        INNER JOIN learning_progress lp ON vw.wordId = lp.wordId
        WHERE lp.userId = :userId
        AND lp.nextReviewAt <= :currentTime
        AND lp.status != 'MASTERED'
        ORDER BY lp.updatedAt DESC
    """)
    fun getDueReviewWords(
        userId: String,
        currentTime: Long = System.currentTimeMillis()
    ): Flow<List<VocabularyWordEntity>>

    @Query("""
        SELECT SUM(correctCount) FROM learning_progress 
        WHERE userId = :userId
    """)
    fun getTotalCorrectAnswers(userId: String): Flow<Int?>

    @Query("""
        SELECT SUM(wrongCount) FROM learning_progress 
        WHERE userId = :userId
    """)
    fun getTotalWrongAnswers(userId: String): Flow<Int?>

    @Query("""
        DELETE FROM learning_progress 
        WHERE userId = :userId AND wordId = :wordId
    """)
    suspend fun deleteProgressByWord(
        userId: String,
        wordId: Long
    )
    @Query("""
    SELECT vw.*
    FROM vocabulary_words vw
    INNER JOIN learning_progress lp ON vw.wordId = lp.wordId
    WHERE lp.userId = :userId
    AND vw.setId = :setId
    AND lp.status = 'LEARNING'
    ORDER BY lp.updatedAt DESC
""")
    fun getReviewWordsBySet(
        userId: String,
        setId: Int
    ): Flow<List<VocabularyWordEntity>>
    // hàm dùng để continue

    @Query("""
    SELECT COUNT(*)
    FROM vocabulary_words
    WHERE setId = :setId
""")
    suspend fun countTotalWordsBySet(setId: Int): Int

    @Query("""
    SELECT COUNT(*)
    FROM vocabulary_words w
    INNER JOIN learning_progress p
        ON w.wordId = p.wordId
    WHERE w.setId = :setId
    AND p.userId = :userId
    AND p.lastReviewedAt > 0
""")
    suspend fun countLearnedWordsBySet(
        userId: String,
        setId: Int
    ): Int

    @Query("""
    SELECT w.*
    FROM vocabulary_words w
    LEFT JOIN learning_progress p
        ON w.wordId = p.wordId
        AND p.userId = :userId
    WHERE w.setId = :setId
    AND p.progressId IS NULL
""")
    fun getUnlearnedWordsBySet(
        userId: String,
        setId: Int
    ): Flow<List<VocabularyWordEntity>>
}