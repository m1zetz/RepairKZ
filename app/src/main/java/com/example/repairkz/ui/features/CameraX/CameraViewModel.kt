package com.example.repairkz.ui.features.CameraX


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class CameraViewModel @Inject constructor(

) : ViewModel(){
    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun handleIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.SetPhoto -> _state.update { it.copy(uri = intent.uri) }
            CameraIntent.ClearPhoto -> _state.update { it.copy(uri = null) }
        }
    }
}

