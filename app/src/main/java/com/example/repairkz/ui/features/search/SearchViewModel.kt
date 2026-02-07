package com.example.repairkz.ui.features.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val initialPattern: String = savedStateHandle.get<String>("pattern") ?: ""
    private val _uiState = MutableStateFlow(SearchUiState(initialPattern))
    val uiState = _uiState.asStateFlow()

    private val _searchEffectsChannel = Channel<SearchEffects>(Channel.BUFFERED)
    val searchEffectsChannel = _searchEffectsChannel.receiveAsFlow()

    fun handleIntent(intent: SearchIntents) {
        when (intent) {
            is SearchIntents.ChangeText -> _uiState.update {
                it.copy(query = intent.text)
            }
            is SearchIntents.NavigateToBack -> {
                viewModelScope.launch {
                    _searchEffectsChannel.send(SearchEffects.NavigateBack)
                }
            }
        }
    }
}


