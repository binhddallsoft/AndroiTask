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
    suspend fun getTasksByCollectionId(collectionId: Int): List<TaskEntity>

    @Query("UPDATE task SET is_favourite = :isFavorite WHERE id = :taskId")
    suspend fun updateTaskFavorite(taskId: Int, isFavorite: Boolean)

    @Query("UPDATE task SET is_completed = :isCompleted WHERE id = :taskId")
    suspend fun updateTaskCompletion(taskId: Int, isCompleted: Boolean)

    @Query("UPDATE task_collections SET title = :title WHERE id = :collectionId")
    suspend fun updateTaskCollectionTitle(collectionId: Long, title: String)

    @Update
    suspend fun updateTaskCollection(taskCollection: TaskCollection) // Neu co id thi se tu tim id de update entity

    @Delete
    suspend fun deleteTaskCollection(taskCollection: TaskCollection) // Neu co id thi se tu tim id de xoa entity
}