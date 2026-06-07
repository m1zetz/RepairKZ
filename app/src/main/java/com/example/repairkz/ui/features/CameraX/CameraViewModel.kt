package com.example.repairkz.ui.features.CameraX


import android.net.Uri
import com.example.repairkz.ui.features.CameraX.CameraEffect
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.ui.base.BaseViewModel
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

) : BaseViewModel<CameraState, CameraIntent, CameraEffect>(){

    override val initialState = CameraState()

    override fun handleIntent(intent: CameraIntent) {
        when (intent) {
            is CameraIntent.SetPhoto -> setState { copy(uri = intent.uri) }
            CameraIntent.ClearPhoto -> setState { copy(uri = null) }
        }
    }
}

