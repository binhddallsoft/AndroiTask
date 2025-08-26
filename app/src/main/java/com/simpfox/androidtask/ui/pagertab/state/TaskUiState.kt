package com.simpfox.androidtask.ui.pagertab.state

import com.simpfox.androidtask.database.entity.TaskEntity

data class TaskUiState(
    val id: Long,
    val content: String,
    val isFavorite: Boolean,
    val isCompleted: Boolean,
    val collectionId: Long,
    val updatedAt: Long
)
