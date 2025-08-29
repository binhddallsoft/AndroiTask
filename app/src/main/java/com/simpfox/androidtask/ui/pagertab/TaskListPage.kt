package com.simpfox.androidtask.ui.pagertab

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.simpfox.androidtask.R
import com.simpfox.androidtask.TaskDelegate
import com.simpfox.androidtask.ui.items.activeTasksHeader
import com.simpfox.androidtask.ui.items.bottomCorner
import com.simpfox.androidtask.ui.items.emptyState
import com.simpfox.androidtask.ui.items.listTaskItems
import com.simpfox.androidtask.ui.items.spacer
import com.simpfox.androidtask.ui.items.topCorner
import com.simpfox.androidtask.ui.pagertab.state.TaskGroupUiState
import com.simpfox.androidtask.ui.pagertab.state.TaskPageUiState

@Composable
fun TaskListPage(state: TaskGroupUiState, taskDelegate: TaskDelegate) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
            .fillMaxHeight()
            .padding(12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        topCorner()
        activeTasksHeader("header", state, taskDelegate)
        emptyState("empty", state.page)
        listTaskItems(TaskListStatus.ACTIVE, state.page.activeTaskList, taskDelegate)
        bottomCorner()
        spacer(24)

        if (state.page.completedTaskList.isNotEmpty()) {
            topCorner()
            listTaskItems(TaskListStatus.COMPLETE, state.page.completedTaskList, taskDelegate)
            bottomCorner()
        }
//        item { ActiveTaskListSection(collectionId,state.activeTaskList, taskDelegate) }
//        item { CompletedTaskListSection(state.completedTaskList, taskDelegate) }
    }
}

enum class TaskListStatus(val value: String) {
    ACTIVE("active"),
    COMPLETE("complete"),
}