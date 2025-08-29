package com.simpfox.androidtask.ui.pagertab.state

import com.simpfox.androidtask.database.entity.SortType
import com.simpfox.androidtask.database.entity.TaskCollection
import com.simpfox.androidtask.database.entity.toSortType


data class TabUiState(
    val id: Long,
    val title: String,
    val sortType: SortType
)

fun TaskCollection.toTabUiState() : TabUiState {
    return TabUiState(
        id = this.id,
        title = this.title,
        sortType = this.sortType.toSortType()
    )
}