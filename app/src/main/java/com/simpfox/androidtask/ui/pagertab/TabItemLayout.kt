package com.simpfox.androidtask.ui.pagertab

import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.simpfox.androidtask.ui.pagertab.state.TabUiState

@Composable
fun TabItemLayout(state: TabUiState, isSelected: Boolean, onTabSelected: () -> Unit) {
    Tab(
        text = { Text(state.title) },
        selected = isSelected,
        onClick = onTabSelected
    )
}