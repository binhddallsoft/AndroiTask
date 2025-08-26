package com.simpfox.androidtask

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.simpfox.androidtask.ui.floataction.AppFloatActionButton
import com.simpfox.androidtask.ui.pagertab.PagerTabLayout
import com.simpfox.androidtask.ui.theme.AndroidTaskTheme
import com.simpfox.androidtask.ui.topbar.TopBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate $mainViewModel")
        enableEdgeToEdge()
        setContent {
            AndroidTaskTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), floatingActionButton = {
                    AppFloatActionButton(
                        Modifier.background(
                            color = Color.Black.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .size(58.dp).clip(RoundedCornerShape(12.dp))
                    ) {
                        Log.d("MainActivity", "onCreate: Clicked")
                    }
                }) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding).fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TopBar()
                        PagerTabLayout()
                    }
                }
            }
        }
    }
}
