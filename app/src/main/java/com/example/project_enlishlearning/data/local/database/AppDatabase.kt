package com.example.project_enlishlearning.data.local.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.project_enlishlearning.data.local.dao.VocabularyDao
import com.example.project_enlishlearning.data.local.entity.VocabularySetEntity
import com.example.project_enlishlearning.data.local.entity.VocabularyWordEntity
@Database(
    entities = [VocabularySetEntity::class, VocabularyWordEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vocabularyDao(): VocabularyDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Nếu INSTANCE đã có thì trả về luôn, nếu null thì mới tạo mới
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "english_learning_database" // Đây là tên file database vật lý lưu trong điện thoại
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}