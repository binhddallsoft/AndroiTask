package com.simpfox.androidtask

import android.util.Log
import androidx.lifecycle.ViewModel
import com.simpfox.androidtask.repository.TaskRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val taskRepo: TaskRepo
) : ViewModel() {
    init {
        Log.d("MainViewModel", "init ${taskRepo}")
    }

}