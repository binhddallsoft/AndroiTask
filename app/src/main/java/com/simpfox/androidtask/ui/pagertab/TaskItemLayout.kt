package com.simpfox.androidtask.ui.pagertab

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.simpfox.androidtask.R
import com.simpfox.androidtask.TaskDelegate
import com.simpfox.androidtask.ui.items.itemBgColor
import com.simpfox.androidtask.ui.pagertab.state.TaskUiState

@Composable
fun LazyItemScope.TaskItemLayout(
    state: TaskUiState,
    taskDelegate: TaskDelegate
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable {}
            .background(color = itemBgColor)
            .animateItem(
                tween(easing = LinearEasing),
                tween(easing = LinearEasing),
                tween(easing = LinearEasing)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        Checkbox(
            checked = state.isCompleted,
            onCheckedChange = { isChecked ->
                taskDelegate.invertTaskCompleted(state)
            }
        )
        Column(
            modifier = Modifier
                .weight(1.0f)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = state.content,
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                textDecoration = TextDecoration.LineThrough.takeIf { state.isCompleted }
            )
            if (state.isCompleted) {
                Text(text = "Completed: ${state.stringUpdatedAt}", modifier = Modifier.padding(horizontal = 4.dp))
            }
        }
        if(!state.isCompleted) {
        Image(
            painter = if (state.isFavorite) painterResource(R.drawable.ic_favorite) else painterResource(R.drawable.ic_unfavorite),
            contentDescription = null,
            modifier = Modifier
                .padding(6.dp)
                .clickable {
                    taskDelegate.invertTaskFavorite(state)
                }
        )
    }
    }
}

//@Preview
//@Composable
//fun TaskItemLayoutPreview() {
//    TaskItemLayout(
//        state = TaskUiState(
//            id = 1,
//            content = "Task 1",
//            isCompleted = false,
//            isFavorite = true,
//            collectionId = 1,
//            updatedAt = 1000
//        ), taskDelegate = object : TaskDelegate {}
//    )
//}