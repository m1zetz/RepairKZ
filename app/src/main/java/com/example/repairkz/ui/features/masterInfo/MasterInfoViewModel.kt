package com.example.repairkz.ui.features.masterInfo

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.domain.useCases.masterData.GetMasterByIdUseCase
import com.example.repairkz.domain.useCases.order.CreateOrderRequestUseCase
import com.example.repairkz.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MasterInfoViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getMasterByIdUseCase: GetMasterByIdUseCase,
    private val createOrderRequestUseCase: CreateOrderRequestUseCase
) : BaseViewModel<MasterProfileState, MasterProfileIntent, MasterProfileEffect>() {

    override val initialState = MasterProfileState()

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
            setState {
                copy(isLoading = true)
            }
            val result = getMasterByIdUseCase(id)
            result.fold(
                onSuccess = { master ->
                    setState {
                        copy(master = master)
                    }
                },
                onFailure = {e ->
                    setState {
                        copy(error = e.message)
                    }
                }
            )
            setState {
                copy(isLoading = false)
            }
        }
    }

    override fun handleIntent(intent: MasterProfileIntent) {
        when (intent) {
            is MasterProfileIntent.AddToFavorites -> {

            }

            is MasterProfileIntent.DoOrder -> {
                viewModelScope.launch {
                    comingId?.let {
                       sendEffect(MasterProfileEffect.NavigateToOrderReg(comingId))
                    }

                }
            }

            is MasterProfileIntent.Report -> {

            }
        }
    }

}