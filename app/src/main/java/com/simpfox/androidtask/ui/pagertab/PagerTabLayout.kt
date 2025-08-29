package com.simpfox.androidtask.ui.pagertab

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.simpfox.androidtask.ID_ADD_NEW_LIST
import com.simpfox.androidtask.TaskDelegate
import com.simpfox.androidtask.ui.pagertab.state.TabUiState
import com.simpfox.androidtask.ui.pagertab.state.TaskGroupUiState
import com.simpfox.androidtask.ui.pagertab.state.TaskPageUiState
import com.simpfox.androidtask.ui.pagertab.state.TaskUiState
import kotlinx.coroutines.launch

@Composable
fun PagerTabLayout(state: List<TaskGroupUiState>, taskDelegate: TaskDelegate) {
    // So page duoc tao ra -> by remember de luu lai trang thai sau khi recomponse (ve lai composable)
    var pageCount by remember { mutableIntStateOf(0) }
    var internalState by remember { mutableStateOf(state) }
    internalState = state
    // Tao pager state cung cap pageCount duoi dang lambda -> Dieu khien trang thai cuon trang va hien thi dang o trang nao
    val pagerState = rememberPagerState {
        pageCount
    } // Dung de dieu khien va sync giua tabrow va horizontalpager
    val scope = rememberCoroutineScope()

    pageCount = state.count {
        it.tab.id != ID_ADD_NEW_LIST
    }


    // Tai sao lai dung LunchedEffect? va snapshotFlow?
    LaunchedEffect(Unit) {
        snapshotFlow { pagerState.currentPage }.collect { index ->
            internalState.getOrNull(index)?.tab?.id?.let { currentCollectionId ->
                taskDelegate.updateCurrentCollectionId(currentCollectionId)
            }
        }
    }

    AppTabRowLayout(
        selectedTabIndex = pagerState.currentPage,
        listTabs = state.map { it.tab },
        onTabSelected = { index ->
            if((state.getOrNull(index)?.tab?.id ?: 0) == ID_ADD_NEW_LIST) {
                taskDelegate.requestAddNewCollection()
            } else {
                scope.launch {
                    pagerState.scrollToPage(index)
                }
            }
        }
    )
    HorizontalPager(pagerState, key = { it }, beyondViewportPageCount = 2) { pageIndex ->
        TaskListPage(state[pageIndex], taskDelegate)
    }
}