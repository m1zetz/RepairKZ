package com.example.repairkz.ui.features.search

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false
)

sealed class SearchIntents{
    data class ChangeText(val text: String) : SearchIntents()

    object NavigateToBack : SearchIntents()
}

sealed interface SearchEffects{
    object NavigateBack : SearchEffects
}