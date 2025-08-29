package com.simpfox.androidtask.ui

import androidx.compose.foundation.background
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.simpfox.androidtask.MainEvent
import com.simpfox.androidtask.MainViewModel
import com.simpfox.androidtask.ui.floataction.AppFloatActionButton
import com.simpfox.androidtask.ui.home.HomeLayout
import com.simpfox.androidtask.ui.pagertab.PagerTabLayout
import com.simpfox.androidtask.ui.theme.AndroidTaskTheme
import com.simpfox.androidtask.ui.topbar.TopBar

@Composable
fun TaskApp(mainViewModel: MainViewModel = hiltViewModel()) {
    AndroidTaskTheme {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            startDestination = NavScreen.HOME.route
        ) {
            composable(route = NavScreen.HOME.route) {
                HomeLayout()
            }
        }
        HomeLayout()
    }
}

enum class NavScreen(val route: String) {
    HOME("home"),
    TASK("task/{taskId}"),
    COLLECTION("collection/{collectionId}"),
    SETTINGS("settings")
}