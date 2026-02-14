package com.example.repairkz.ui.features.home

import com.example.repairkz.common.enums.MasterSpetializationsEnum

sealed class Effects{
    data class NavigateToSearch(val patternResId: Int?) : Effects()
}

sealed class HomeScreenIntent{
    data class ClickOnCard(val pattern: MasterSpetializationsEnum?) : HomeScreenIntent()
}