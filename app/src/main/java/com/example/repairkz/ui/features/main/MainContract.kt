package com.example.repairkz.ui.features.main

import com.example.repairkz.ui.base.UiEffect
import com.example.repairkz.ui.base.UiIntent
import com.example.repairkz.ui.base.UiState

sealed class MainIntent : UiIntent{
    data class ChangeScreen(val index: Int) : MainIntent()
}
object MainEffect : UiEffect
data class MainState(
    val selectedIndex: Int = 0
) : UiState