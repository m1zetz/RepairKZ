package com.example.repairkz.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    private var _uiState = MutableStateFlow<SettingsState>(SettingsState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _settingEffectsChannel = Channel<SettingsEffects>(Channel.BUFFERED)
    val settingEffectsChannel = _settingEffectsChannel.receiveAsFlow()

    fun handleIntent(intent: SettingIntent) {
        when (intent) {
            is SettingIntent.toUserScreen -> {
                viewModelScope.launch {
                    val currentState = _uiState.value
                    if(currentState is SettingsState.Success){
                        val id = currentState.userData.userId
                        _settingEffectsChannel.send(SettingsEffects.NavigateToUserInfo(id))
                    }

                }
            }
        }
    }

}