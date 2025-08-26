package com.simpfox.androidtask.ui.pagertab

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.simpfox.androidtask.ui.pagertab.state.TaskUiState

@Composable
fun ActiveTaskListSection(activeTaskList: List<TaskUiState>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        activeTaskList.forEach {
            TaskItemLayout(it, onCompleteTask = { taskState ->
                Log.d("TaskItemLayout", "Task completed: ${taskState.id}")
            }, onTaskClicked =  { taskState ->
                Log.d("TaskItemLayout", "Task clicked: ${taskState.id}")
            }, onTaskFavorite = { taskState ->
                Log.d("TaskItemLayout", "Task favorite: ${taskState.id}")
            } )
        }
    }
}