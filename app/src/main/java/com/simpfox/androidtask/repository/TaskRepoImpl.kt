package com.simpfox.androidtask.repository

import com.simpfox.androidtask.database.dao.TaskDAO
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

    override suspend fun getTasksByCollectionId(collectionId: Int): List<TaskEntity> = withContext(Dispatchers.IO) {
        taskDao.getTasksByCollectionId(collectionId)
    }

    override suspend fun addTaskCollection(title: String): TaskCollection? {
        val taskCollection = TaskCollection(title = title, updatedAt = Calendar.getInstance().timeInMillis)
        val id = taskDao.insertTaskCollection(taskCollection)
        return if(id > 0) {
            taskCollection.copy(
                id = id
            )
        } else {
            null
        }
    }
}