package com.example.repairkz.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UiState, I : UiIntent, E : UiEffect > : ViewModel() {
    abstract val initialState: S
    protected val _state by lazy {MutableStateFlow(initialState)}
    val state by lazy { _state.asStateFlow() }

    private val _channel by lazy { Channel<E>(Channel.BUFFERED)}
    val channel by lazy { _channel.receiveAsFlow() }

    abstract fun handleIntent(intent: I)

    protected fun setState(reducer: S.() -> S){
        _state.update {
            it.reducer()
        }
    }
    protected fun sendEffect(effect: E){
        viewModelScope.launch {
            _channel.send(effect)
        }

    }

}