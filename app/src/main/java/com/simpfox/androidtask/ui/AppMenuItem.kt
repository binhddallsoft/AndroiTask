package com.simpfox.androidtask.ui

data class AppMenuItem (
    val title: String,
    val action: () -> Unit
)