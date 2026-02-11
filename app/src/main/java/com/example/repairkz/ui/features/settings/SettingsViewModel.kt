package com.example.repairkz.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.data.userData.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {
    private var _uiState = MutableStateFlow<SettingsState>(SettingsState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _settingEffectsChannel = Channel<SettingsEffects>(Channel.BUFFERED)
    val settingEffectsChannel = _settingEffectsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            userRepository.userData.collect { user ->
                if (user!=null){
                    _uiState.value = SettingsState.Success(user)
                } else{
                    _uiState.value = SettingsState.Error("Ошибка запроса")
                }

            }
        }
    }

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