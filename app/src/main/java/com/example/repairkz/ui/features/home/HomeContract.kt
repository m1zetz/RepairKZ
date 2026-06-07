package com.example.repairkz.ui.features.home

import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.ui.base.UiEffect
import com.example.repairkz.ui.base.UiIntent
import com.example.repairkz.ui.base.UiState

object HomeState : UiState
sealed class HomeEffect : UiEffect{
    data class NavigateToSearch(val patternResId: Int?) : HomeEffect()
}

sealed class HomeIntent : UiIntent{
    data class ClickOnCard(val pattern: MasterSpetializationsEnum?) : HomeIntent()
}