package com.example.repairkz.ui.features.home

sealed class Effects{
    data class NavigateToSearch(val pattern: String = "") : Effects()
}

sealed class HomeScreenIntent{
    data class ClickOnCard(val pattern: String) : HomeScreenIntent()
}