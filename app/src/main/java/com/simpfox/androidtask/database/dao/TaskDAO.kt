package com.simpfox.androidtask.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.simpfox.androidtask.database.entity.TaskCollection
import com.simpfox.androidtask.database.entity.TaskEntity

@Dao
interface TaskDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskCollection(taskCollection: TaskCollection) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(task: TaskEntity): Long

    @Query("SELECT * FROM task_collections")
    suspend fun getTaskCollections(): List<TaskCollection>

    @Query("SELECT * FROM task WHERE collection_id = :collectionId")
    suspend fun getTasksByCollectionId(collectionId: Long): List<TaskEntity>

    @Query("UPDATE task SET is_favorite = :isFavorite WHERE id = :taskId")
    suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean) : Int

    @Query("UPDATE task SET is_completed = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Int

    @Query("UPDATE task_collections SET title = :title WHERE id = :collectionId")
    suspend fun updateTaskCollectionTitle(collectionId: Long, title: String): Int

    @Update
    suspend fun updateTaskCollection(taskCollection: TaskCollection): Int

    @Update
    suspend fun updateTask(task: TaskEntity): Int

    @Delete
    suspend fun deleteTaskCollection(taskCollection: TaskCollection): Int

    @Query("DELETE FROM task_collections  WHERE id = :collectionId")
    suspend fun deleteTaskCollectionById(collectionId: Long): Int

    @Delete
    suspend fun deleteTask(task: TaskEntity): Int

    @Query("UPDATE task_collections SET sort_type = :sortType WHERE id = :collectionId")
    suspend fun updateCollectionSortType(collectionId: Long, sortType: Int) : Int
}