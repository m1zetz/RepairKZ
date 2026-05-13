package com.example.repairkz.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.enums.StatusOfUser
import com.example.repairkz.common.models.Master
import com.example.repairkz.data.local.dataBase.RepairDataBase
import com.example.repairkz.data.local.dataStore.DataStoreManager
import com.example.repairkz.data.remote.dto.order.ChangeStatusRequestDTO
import com.example.repairkz.data.remote.dto.MasterRequestDTO
import com.example.repairkz.data.userData.UserRepository
import com.example.repairkz.domain.errors.SettingsError
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserStatusUseCase
import com.example.repairkz.ui.features.settings.SettingsEffect.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val updateUserStatusUseCase: UpdateUserStatusUseCase,
    private val userRepository: UserRepository,
    private val dataStoreManager: DataStoreManager,
    private val roomDB: RepairDataBase
) : ViewModel() {
    private var _uiState = MutableStateFlow<SettingsState>(SettingsState())
    val uiState = _uiState.asStateFlow()

    private val _settingEffectsChannel = Channel<SettingsEffect>(Channel.BUFFERED)
    val settingEffectsChannel = _settingEffectsChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            getUserDataUseCase().collect { user ->
                _uiState.update {currentState ->
                    currentState.copy(
                        user = user
                    )
                }
            }
        }
    }


    fun handleIntent(intent: SettingIntent) {
        when (intent) {
            is SettingIntent.ToUserScreen -> {
                viewModelScope.launch {
                    _settingEffectsChannel.send(NavigateToUserInfo)
                }
            }

            is SettingIntent.SwitchStatus -> {
                viewModelScope.launch {
                    val currentState = _uiState.value
                    _uiState.value = currentState.copy(isChangeStatusLoading = true)
                    val result = switchUserStatus(intent.status)
                    result.onFailure {
                        _settingEffectsChannel.send(ShowError(SettingsError.ChangeStatusError))
                    }
                    val updatedState = _uiState.value
                    _uiState.value = updatedState.copy(isChangeStatusLoading = false)
                }
            }

            SettingIntent.Exit -> {
                viewModelScope.launch {
                    dataStoreManager.clearDataStore()
                    withContext(Dispatchers.IO) {
                        roomDB.clearAllTables()
                    }
                    _settingEffectsChannel.send(SettingsEffect.NavigateToLogin)

                }

            }
        }
    }

    private suspend fun switchUserStatus(newStatus: StatusOfUser) : Result<Unit>{
        val currentState = _uiState.value
        val user = currentState.user?: return Result.failure(Exception("current user is null"))

        val request = ChangeStatusRequestDTO(
            statusOfUser = newStatus,
            masterData = if (user is Master) {
                MasterRequestDTO(
                    experienceInYears = user.experienceInYears,
                    description = user.description,
                    masterSpecialization = user.masterSpecialization,
                    userId = user.id
                )
            } else null
        )
        return updateUserStatusUseCase(id = user.id, request)

    }

}