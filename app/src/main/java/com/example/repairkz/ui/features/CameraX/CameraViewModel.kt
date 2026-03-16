package com.example.repairkz.ui.features.CameraX

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel(){
    private val _state = MutableStateFlow(CameraState())
    val state = _state.asStateFlow()

    fun setPhoto(uri: Uri){
        _state.update {cameraState ->
            cameraState.copy(uri)
        }
    }

    fun clearPhoto(){
        _state.update {cameraState ->
            cameraState.copy(isConfirmed = false)
        }
    }
    fun handleIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.SetPhoto -> setPhoto(intent.uri)
            CameraIntent.ClearPhoto -> clearPhoto()
            CameraIntent.ConfirmPhoto -> {
                _state.update {cameraState ->
                    cameraState.copy(isConfirmed = true)
                }
            }
        }
    }
}

