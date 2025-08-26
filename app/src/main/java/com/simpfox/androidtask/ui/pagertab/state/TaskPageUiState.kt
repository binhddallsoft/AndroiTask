package com.simpfox.androidtask.ui.pagertab.state

data class TaskPageUiState(
    val activeTaskList: List<TaskUiState>,
    val completedTaskList: List<TaskUiState>,
)

