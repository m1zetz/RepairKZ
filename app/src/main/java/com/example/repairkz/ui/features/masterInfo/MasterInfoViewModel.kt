package com.example.repairkz.ui.features.masterInfo

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.domain.useCases.files.PreparePhotoPartUseCase
import com.example.repairkz.domain.useCases.files.SaveToInternalUseCase
import com.example.repairkz.domain.useCases.masterData.GetMasterByIdUseCase
import com.example.repairkz.domain.useCases.order.CreateOrderRequestUseCase
import com.example.repairkz.domain.useCases.services.CreateServiceUseCase
import com.example.repairkz.domain.useCases.services.DeleteServiceUseCase
import com.example.repairkz.domain.useCases.services.GetServicesUseCase
import com.example.repairkz.domain.useCases.services.UpdateServiceUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserDataUseCase
import com.example.repairkz.domain.useCases.userData.UpdateUserPhotoUseCase
import com.example.repairkz.ui.features.UserInfo.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.onSuccess

@HiltViewModel
class MasterInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMasterByIdUseCase: GetMasterByIdUseCase,
    private val createOrderRequestUseCase: CreateOrderRequestUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MasterProfileState())
    val uiState = _uiState.asStateFlow()

    val _channel = Channel<MasterInfoEffect>(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    val comingId: Long? = try {
        savedStateHandle.get<Long>("userId")
    } catch (e: Exception) {
        savedStateHandle.get<String>("userId")?.toLongOrNull()
    }

    init {
        viewModelScope.launch {
            getMasterData()
        }

    }

    private suspend fun getMasterData() {
        comingId?.let { id ->
            _uiState.update {
                it.copy(isLoading = true)
            }
            val result = getMasterByIdUseCase(id)
            result.fold(
                onSuccess = { master ->
                    _uiState.update {
                        it.copy(master = master)
                    }
                },
                onFailure = {e ->
                    _uiState.update {
                        it.copy(error = e.message)
                    }
                }
            )
            _uiState.update {
                it.copy(isLoading = false)
            }
        }
    }

    fun handleIntent(intent: MasterInfoIntent) {
        when (intent) {
            is MasterInfoIntent.AddToFavorites -> {

            }

            is MasterInfoIntent.DoOrder -> {
                viewModelScope.launch {
                    comingId?.let {
                        _channel.send(MasterInfoEffect.NavigateToOrderReg(comingId))
                    }

                }
            }

            is MasterInfoIntent.Report -> {

            }
        }
    }

}