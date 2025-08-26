package com.simpfox.androidtask.ui.floataction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun AppFloatActionButton(
    modifier: Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
) {
    Box (
        modifier = modifier
            .clickable(enabled) {
                onClick()
            }
    ) {
        Text(
            "+",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}