package com.simpfox.androidtask.ui.pagertab.state

import androidx.compose.ui.text.intl.Locale
import com.simpfox.androidtask.database.entity.TaskEntity
import java.text.SimpleDateFormat
import java.util.Date

data class TaskUiState(
    val id: Long,
    val content: String,
    val isFavorite: Boolean = false,
    val isCompleted: Boolean = false,
    val collectionId: Long,
    val updatedAt: Long,
    val stringUpdatedAt: String,
)

fun TaskEntity.toTaskUiState() : TaskUiState {
    return TaskUiState(
        id = this.id,
        content = this.content,
        isFavorite = this.isFavorite,
        isCompleted = this.isCompleted,
        collectionId = this.collectionId,
        updatedAt = this.updatedAt,
        stringUpdatedAt = this.updatedAt.millisToStringDate()
    )
}

fun Long.millisToStringDate() : String {
    return SimpleDateFormat("EEE,dd MMM yyyy", java.util.Locale.getDefault()).format(Date(this)).toString()
}

//fun TaskUiState.toTaskEntity() : TaskEntity {
//    return TaskEntity(
//        id = this.id,
//        content = this.content,
//        isFavorite = this.isFavorite,
//        isCompleted = this.isCompleted,
//        collectionId = this.collectionId,
//        updatedAt = Calendar.getInstance().timeInMillis,
//    )
//}