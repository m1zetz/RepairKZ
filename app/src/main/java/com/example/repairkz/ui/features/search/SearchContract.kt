package com.example.repairkz.ui.features.search

import com.example.repairkz.common.models.Master

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val listOfMasters: List<Master>? = null,
    val error: String? = null,
)

sealed class SearchIntents{
    data class ChangeText(val text: String) : SearchIntents()
    object GetData: SearchIntents()
    object NavigateToBack : SearchIntents()
}

sealed interface SearchEffects{
    object NavigateBack : SearchEffects
}