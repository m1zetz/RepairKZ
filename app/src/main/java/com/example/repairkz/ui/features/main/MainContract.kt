package com.example.repairkz.ui.features.main

sealed class MainIntent{
    data class ChangeScreen(val index: Int) : MainIntent()
}
data class MainUiState(
    val selectedIndex: Int = 0
)