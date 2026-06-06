package com.example.project_enlishlearning.utils

import android.content.Context
import com.example.project_enlishlearning.data.local.database.AppDatabase

object DatabaseProvider {
    @Volatile
    private var database: AppDatabase? = null

    fun init(context: Context) {
        if (database == null) {
            synchronized(this) {
                if (database == null) {
                    database = AppDatabase.getDatabase(context.applicationContext)
                }
            }
        }
    }

    fun getDatabase(): AppDatabase {
        return database ?: throw IllegalStateException("DatabaseProvider not initialized. Call init(context) first.")
    }
}
