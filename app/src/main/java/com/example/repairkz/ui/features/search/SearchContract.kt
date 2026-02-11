package com.example.repairkz.ui.features.search

import com.example.repairkz.common.models.Master

data class SearchUiState(
    val query: String = "",
    val result: SearchResult = SearchResult.Idle
)

sealed class SearchResult {
    data object Idle : SearchResult()
    data object Loading : SearchResult()
    data class Success(val masters: List<Master>) : SearchResult()
    data class Error(val message: String) : SearchResult()
}


sealed class SearchIntents{
    data class ChangeText(val text: String) : SearchIntents()
    object GetData: SearchIntents()
    object NavigateToBack : SearchIntents()
}

sealed interface SearchEffects{
    object NavigateBack : SearchEffects
}