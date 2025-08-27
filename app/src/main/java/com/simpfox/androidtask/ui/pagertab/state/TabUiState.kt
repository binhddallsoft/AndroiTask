package com.simpfox.androidtask.ui.pagertab.state

import com.simpfox.androidtask.database.entity.TaskCollection


data class TabUiState(
    val id: Long,
    val title: String,
)

fun TaskCollection.toTabUiState() : TabUiState {
    return TabUiState(
        id = this.id,
        title = this.title
    )
}