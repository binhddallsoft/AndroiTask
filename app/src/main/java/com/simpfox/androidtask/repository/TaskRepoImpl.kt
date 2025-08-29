package com.simpfox.androidtask.repository

import com.simpfox.androidtask.database.dao.TaskDAO
import com.simpfox.androidtask.database.entity.SortType
import com.simpfox.androidtask.database.entity.TaskCollection
import com.simpfox.androidtask.database.entity.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

class TaskRepoImpl(
    private val taskDao: TaskDAO
) : TaskRepo {
    override suspend fun getTaskCollection(): List<TaskCollection>  = withContext(Dispatchers.IO) {
        taskDao.getTaskCollections()
    }


    override suspend fun getTasksByCollectionId(collectionId: Long): List<TaskEntity> = withContext(Dispatchers.IO) {
        taskDao.getTasksByCollectionId(collectionId)
    }

    override suspend fun addTaskCollection(title: String): TaskCollection? {
        val now = Calendar.getInstance().timeInMillis
        val taskCollection = TaskCollection(title = title, updatedAt = now, createAt = now, sortType = SortType.CREATED_DATE.value)
        val id = taskDao.insertTaskCollection(taskCollection)
        return if(id > 0) {
            taskCollection.copy(
                id = id
            )
        } else {
            null
        }
    }

    override suspend fun addTask(
        content: String,
        collectionId: Long,
    ): TaskEntity? = withContext(Dispatchers.IO) {
        val now = Calendar.getInstance().timeInMillis
        val task = TaskEntity(
            content = content,
            collectionId = collectionId,
            updatedAt = now,
            isFavorite = false,
            isCompleted = false,
            createdAt = now
        )
        val id = taskDao.insertTasks(task)
        if(id > 0) {
            task.copy(
                id = id
            )
        } else {
            null
        }
    }

    override suspend fun updateTask(task: TaskEntity): Boolean = withContext(Dispatchers.IO) {
        taskDao.updateTask(task) > 0
    }

    override suspend fun updateTaskCompleted(taskId: Long, isCompleted: Boolean): Boolean = withContext(Dispatchers.IO) {
        taskDao.updateTaskCompleted(taskId, isCompleted) > 0
    }

    override suspend fun updateTaskFavorite(taskId: Long, isFavorite: Boolean): Boolean = withContext(Dispatchers.IO) {
        taskDao.updateTaskFavorite(taskId, isFavorite) > 0
    }

    override suspend fun updateTaskCollection(taskCollection: TaskCollection) = withContext(Dispatchers.IO) {
        taskDao.updateTaskCollection(taskCollection) > 0
    }

    override suspend fun deleteTaskCollectionById(collectionId: Long) = withContext(Dispatchers.IO) {
        taskDao.deleteTaskCollectionById(collectionId) > 0
    }

    override suspend fun updateCollectionSortType(collectionId: Long, sortType: Int)  = withContext(Dispatchers.IO) {
        taskDao.updateCollectionSortType(collectionId, sortType) > 0
    }


}