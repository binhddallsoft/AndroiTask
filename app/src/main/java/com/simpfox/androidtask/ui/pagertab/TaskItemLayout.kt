package com.simpfox.androidtask.ui.pagertab

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simpfox.androidtask.R
import com.simpfox.androidtask.ui.pagertab.state.TaskUiState

@Composable
fun TaskItemLayout(
    state: TaskUiState,
    onCompleteTask: (TaskUiState) -> Unit = {},
    onTaskClicked: (TaskUiState) -> Unit = {},
    onTaskFavorite: (TaskUiState) -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable {
            Log.d("TaskItemLayout", "Task clicked: ${state.id}")
            onTaskClicked(state)
        },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    ) {
        Checkbox(
            checked = state.isCompleted,
            onCheckedChange = { isChecked ->
                onCompleteTask(state)
            }
        )
        Text(text = state.content, modifier = Modifier.weight(1.0f).padding(4.dp))
        Image(
            painter = if(state.isFavorite) painterResource(id = R.drawable.ic_favorite) else painterResource(id = R.drawable.ic_unfavorite),
            contentDescription = null,
            modifier = Modifier.clickable {
                onTaskFavorite(state)
            }
        )
    }
}

@Preview
@Composable
fun TaskItemLayoutPreview() {
    TaskItemLayout(
        state = TaskUiState(
            id = 1,
            content = "Task 1",
            isCompleted = false,
            isFavorite = true,
            collectionId = 1,
            updatedAt = 1000
        )
    )
}