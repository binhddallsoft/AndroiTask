package com.simpfox.androidtask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.simpfox.androidtask.database.dao.TaskDAO
import com.simpfox.androidtask.database.entity.TaskCollection
import com.simpfox.androidtask.database.entity.TaskEntity

private const val DATABASE_NAME = "app_database"
private const val DATABASE_VERSION = 1

@Database(
    entities = [TaskEntity::class, TaskCollection::class],
    version = DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDAO

    companion object {
        private var instance: AppDatabase? = null

        operator fun invoke(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        fun buildDatabase(context: Context): AppDatabase = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }
}