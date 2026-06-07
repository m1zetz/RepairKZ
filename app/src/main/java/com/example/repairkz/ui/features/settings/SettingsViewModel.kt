package com.example.repairkz.ui.features.settings

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
import com.example.repairkz.ui.base.BaseViewModel
import com.example.repairkz.ui.features.settings.SettingsEffect.*
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
) : BaseViewModel<SettingsState, SettingsIntent, SettingsEffect>() {

    override val initialState = SettingsState()


    init {
        viewModelScope.launch {
            getUserDataUseCase().collect { user ->
                setState {
                    copy(
                        user = user
                    )
                }
            }
        }
    }


    override fun handleIntent(intent: SettingsIntent) {
        when (intent) {
            is SettingsIntent.ToUserScreen -> {
                sendEffect(NavigateToUserInfo)
            }

            is SettingsIntent.SwitchStatus -> {
                viewModelScope.launch {
                    setState {
                        copy(isChangeStatusLoading = true)
                    }
                    val result = switchUserStatus(intent.status)
                    result.onFailure {
                        sendEffect(ShowError(SettingsError.ChangeStatusError))
                    }
                    setState {
                        copy(isChangeStatusLoading = false)
                    }
                }
            }

            SettingsIntent.Exit -> {
                viewModelScope.launch {
                    dataStoreManager.clearDataStore()
                    withContext(Dispatchers.IO) {
                        roomDB.clearAllTables()
                    }
                    sendEffect(SettingsEffect.NavigateToLogin)
                }


            }
        }
    }

    private suspend fun switchUserStatus(newStatus: StatusOfUser) : Result<Unit>{
        val currentState = _state.value
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