package com.simpfox.androidtask

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simpfox.androidtask.ui.floataction.AppFloatActionButton
import com.simpfox.androidtask.ui.pagertab.PagerTabLayout
import com.simpfox.androidtask.ui.theme.AndroidTaskTheme
import com.simpfox.androidtask.ui.topbar.TopBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate $mainViewModel")
        enableEdgeToEdge()
        setContent {
            AndroidTaskTheme {

                val listTabGroup by mainViewModel.listTabGroup.collectAsStateWithLifecycle() // Tai sao phai by va tai sao lai dung collectAsStateWithLifecycle -> Tu lang nghe vong doi cua activity
                val taskDelegate = remember { mainViewModel }
                var isShowAddTaskBottomSheet by remember { mutableStateOf(false) }
                Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
                    AppFloatActionButton(
                        Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .size(58.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        isShowAddTaskBottomSheet = true
                    }
                }) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TopBar()
                        if(listTabGroup.isNotEmpty()) {
                            PagerTabLayout(listTabGroup, taskDelegate)
                        } else {
                            Text("NO TASK AVAILABLE")
                        }
                    }
                }
                if (isShowAddTaskBottomSheet) {
                    var inputTaskContent by remember { mutableStateOf("") }
                    ModalBottomSheet({
                    }) {
                        Text("Input Task Content Here")
                        TextField(value = "inputTaskContent", onValueChange = {inputTaskContent = it})
                        Button({
                            if(inputTaskContent.isNotEmpty()) {
                                taskDelegate.addNewTaskToCurrentCollection(inputTaskContent)
                                inputTaskContent = ""
                                isShowAddTaskBottomSheet = false
                            }
                        }) {
                            Text("Add Task")
                        }
                    }
                }
            }
        }
    }
}
