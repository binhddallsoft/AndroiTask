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
import com.simpfox.androidtask.TaskDelegate
import com.simpfox.androidtask.ui.pagertab.state.TaskUiState
import androidx.compose.runtime.key
@Composable
fun CompletedTaskListSection(completeTaskList: List<TaskUiState>, taskDelegate: TaskDelegate) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = Color.Black.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
            .animateContentSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Tac dung cua doan nay -> key dung de luu giu id cho 1 column tranh update state nham
        completeTaskList.forEachIndexed { _, taskUiState ->
            key(taskUiState.id) {
                TaskItemLayout(taskUiState, taskDelegate)
            }
        }
    }
}