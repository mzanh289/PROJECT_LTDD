package com.example.project_enlishlearning.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project_enlishlearning.data.local.dao.LearningProgressDao
import com.example.project_enlishlearning.data.local.dao.UserProfileDao
import com.example.project_enlishlearning.data.local.dao.VocabularyDao
import com.example.project_enlishlearning.data.local.entity.LearningProgressEntity
import com.example.project_enlishlearning.data.local.entity.UserProfileEntity
import com.example.project_enlishlearning.data.local.entity.VocabularySetEntity
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        VocabularySetEntity::class,
        VocabularyWordEntity::class,
        UserProfileEntity::class,
        LearningProgressEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun vocabularyDao(): VocabularyDao

    abstract fun userProfileDao(): UserProfileDao

    abstract fun learningProgressDao(): LearningProgressDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE vocabulary_words ADD COLUMN userId TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "english_learning_database"
                )
                    .addMigrations(MIGRATION_3_4)
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }
}