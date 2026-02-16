package com.example.repairkz.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.data.userData.UserRepository
import com.example.repairkz.domain.useCases.userData.GetProfileTypeUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserStatusUseCase
import com.example.repairkz.ui.features.settings.SettingsEffects.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updateUserStatusUseCase: UpdateUserStatusUseCase,
    private val getProfileTypeUseCase: GetProfileTypeUseCase
) : ViewModel() {
    private var _uiState = MutableStateFlow<SettingsState>(SettingsState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _settingEffectsChannel = Channel<SettingsEffects>(Channel.BUFFERED)
    val settingEffectsChannel = _settingEffectsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val user = getUserDataUseCase()
            user.onSuccess { user ->
                _uiState.value = SettingsState.Success(user)
            }
            user.onFailure {exception ->
                _uiState.value = SettingsState.Error(exception.message ?: "Неизвестная ошибка")
            }

        }
    }

    fun handleIntent(intent: SettingIntent) {
        when (intent) {
            is SettingIntent.toUserScreen -> {
                viewModelScope.launch {
                    _settingEffectsChannel.send(NavigateToUserInfo(intent.id))
                }
            }

            is SettingIntent.BecomeAMaster -> {
                viewModelScope.launch {
                    updateUserStatusUseCase(intent.status)
                }
            }
        }
    }

}