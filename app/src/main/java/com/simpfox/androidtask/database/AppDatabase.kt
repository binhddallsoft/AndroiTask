package com.simpfox.androidtask.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.simpfox.androidtask.database.dao.TaskDAO
import com.simpfox.androidtask.database.entity.TaskCollection
import com.simpfox.androidtask.database.entity.TaskEntity

private const val DATABASE_NAME = "app_database"
private const val DATABASE_VERSION = 2

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
        )
            .addMigrations(MIGRATE_1_2)
            .build()
    }
}

private val MIGRATE_1_2 = object : Migration(1,2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE task_collections ADD COLUMN sort_type INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE task_collections ADD COLUMN create_at INTEGER NOT NULL DEFAULT 0")
    }
}