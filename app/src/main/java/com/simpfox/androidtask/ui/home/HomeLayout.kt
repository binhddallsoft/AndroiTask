package com.simpfox.androidtask.ui.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.simpfox.androidtask.MainEvent
import com.simpfox.androidtask.MainViewModel
import com.simpfox.androidtask.ui.AppMenuItem
import com.simpfox.androidtask.ui.floataction.AppFloatActionButton
import com.simpfox.androidtask.ui.pagertab.PagerTabLayout
import com.simpfox.androidtask.ui.topbar.TopBar
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeLayout(mainViewModel: MainViewModel = hiltViewModel()) {
    val listTabGroup by mainViewModel.listTabGroup.collectAsStateWithLifecycle(emptyList()) // Tai sao phai by va tai sao lai dung collectAsStateWithLifecycle -> Tu lang nghe vong doi cua activity
    val taskDelegate = remember { mainViewModel }
    var isShowAddTaskBottomSheet by remember { mutableStateOf(false) }
    var isShowAddCollectionBottomSheet by remember { mutableStateOf(false) }

    var menuListBottomSheet by remember { mutableStateOf<List<AppMenuItem>?>(null) }

    LaunchedEffect(Unit) {
        mainViewModel.eventFlow.collect {
            when(it) {
                MainEvent.RequestAddNewCollection -> {
                    isShowAddCollectionBottomSheet = true
                }
                is MainEvent.RequestShowBottomSheetOptions -> {
                    Log.d("HomeLayout", "RequestShowBottomSheetOptions: ${it.list}")
                    menuListBottomSheet = it.list
                }
            }
        }
    }


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
            isShowAddTaskBottomSheet = taskDelegate.currentCollectionId() > 0
        }
    }) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBar(taskDelegate)
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
            isShowAddTaskBottomSheet = false
        }) {
            Text("Input Task Content Here", modifier = Modifier.fillMaxWidth())
            TextField(value = inputTaskContent, onValueChange = {inputTaskContent = it}, modifier = Modifier.fillMaxWidth())
            Button({
                if(inputTaskContent.isNotEmpty()) {
                    taskDelegate.addNewTaskToCurrentCollection(inputTaskContent)
                    inputTaskContent = ""
                }
                isShowAddTaskBottomSheet = false
            }, modifier = Modifier.align( Alignment.CenterHorizontally )) {
                Text("Add Task")
            }
        }
    }
    if (isShowAddCollectionBottomSheet) {
        var inputTaskCollection by remember { mutableStateOf("") }
        ModalBottomSheet({
            isShowAddCollectionBottomSheet = false
        }) {
            Text("Input Task Content Here", modifier = Modifier.fillMaxWidth())
            TextField(value = inputTaskCollection, onValueChange = {inputTaskCollection = it}, modifier = Modifier.fillMaxWidth())
            Button({
                if(inputTaskCollection.isNotEmpty()) {
                    taskDelegate.addNewCollection(inputTaskCollection)
                    inputTaskCollection = ""
                }
                isShowAddCollectionBottomSheet = false
            }, modifier = Modifier.align( Alignment.CenterHorizontally )) {
                Text("Add Task")
            }
        }
    }

    if(!menuListBottomSheet.isNullOrEmpty()) {
        ModalBottomSheet({
            menuListBottomSheet = null
        }) {
            menuListBottomSheet?.forEach {
                Text(it.title, modifier = Modifier.fillMaxWidth().clickable {
                    it.action.invoke()
                    menuListBottomSheet = null
                }.padding(12.dp))
            }
        }
    }
}