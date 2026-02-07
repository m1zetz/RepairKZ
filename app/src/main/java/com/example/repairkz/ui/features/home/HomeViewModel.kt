package com.example.repairkz.ui.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _effectsChannel = Channel<Effects>(Channel.BUFFERED)
    val effectsChannel = _effectsChannel.receiveAsFlow()

    fun handleIntent(intent: HomeScreenIntent){
        when(intent){
            is HomeScreenIntent.ClickOnCard -> {
                viewModelScope.launch {
                    _effectsChannel.send(Effects.NavigateToSearch(intent.pattern))
                }
            }
        }
    }
}

