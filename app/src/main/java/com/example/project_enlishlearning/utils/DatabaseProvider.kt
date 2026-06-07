package com.example.project_enlishlearning.utils

import android.content.Context
import com.example.project_enlishlearning.data.local.database.AppDatabase

object DatabaseProvider {
    @Volatile
    private var database: AppDatabase? = null
    @Volatile
    private var context: Context? = null

    fun init(context: Context) {
        if (database == null) {
            synchronized(this) {
                if (database == null) {
                    this.context = context.applicationContext
                    database = AppDatabase.getDatabase(context.applicationContext)
                }
            }
        }
    }

    fun getDatabase(): AppDatabase {
        return database ?: throw IllegalStateException("DatabaseProvider not initialized. Call init(context) first.")
    }

    fun getContext(): Context {
        return context ?: throw IllegalStateException("DatabaseProvider not initialized. Call init(context) first.")
    }
}
