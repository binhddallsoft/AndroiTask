package com.simpfox.androidtask.ui.topbar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.simpfox.androidtask.TaskDelegate

@Composable
fun TopBar(taskDelegate: TaskDelegate) {
    Box(
        modifier = Modifier.fillMaxWidth().height(52.dp).padding(horizontal = 12.dp)
    ) {
        Text(
            "Tasks", style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.Center)
        )
//        Button(onClick = {
//            taskDelegate.requestAddNewCollection()
//        }) {
//            Text("+ New List")
//        }
    }
}