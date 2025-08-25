package com.simpfox.androidtask.repository

import com.simpfox.androidtask.database.entity.TaskCollection
import com.simpfox.androidtask.database.entity.TaskEntity

interface TaskRepo {
    suspend fun getTaskCollection(): List<TaskCollection>
    suspend fun getTasksByCollectionId(collectionId: Int): List<TaskEntity>
    suspend fun addTaskCollection(title: String) : TaskCollection?
}