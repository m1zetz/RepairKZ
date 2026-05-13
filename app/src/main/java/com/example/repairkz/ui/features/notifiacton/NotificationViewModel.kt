package com.example.repairkz.ui.features.notifiacton

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.data.notificationData.NotificationRepository
import com.example.repairkz.data.remote.dto.order.ChangeOrderRequestStatusDTO
import com.example.repairkz.data.remote.dto.order.ChangeOrderStateDTO
import com.example.repairkz.data.remote.dto.order.ChangeStatusRequestDTO
import com.example.repairkz.domain.useCases.order.AcceptOrRejectOrderUseCase
import com.example.repairkz.domain.useCases.order.ChangeOrderStateUseCase
import com.example.repairkz.domain.useCases.order.GetClientOrderHistoryUseCase
import com.example.repairkz.domain.useCases.order.GetMasterOrderHistoryUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import com.example.repairkz.ui.features.notifiacton.HistoryItem.*
import com.example.repairkz.ui.features.notifiacton.NotificationState.*

import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val getClientOrderHistoryUseCase: GetClientOrderHistoryUseCase,
    private val getMasterOrderHistoryUseCase: GetMasterOrderHistoryUseCase,
    private val acceptOrRejectOrderUseCase: AcceptOrRejectOrderUseCase,
    private val changeOrderStateUseCase: ChangeOrderStateUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow<NotificationState>(Loading)
    val state = _state.asStateFlow()

    private var currentUserId: Long? = null

    init {
        viewModelScope.launch {
            getUserDataUseCase().collect { user ->
                currentUserId = user?.id
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun handleIntent(intent: NotificationIntent) {
        when (intent) {
            is NotificationIntent.GetNotifications -> {

                viewModelScope.launch {
                    val userId = currentUserId ?: return@launch
                    _state.value = Loading
                    try {
                        coroutineScope {
                            val clientResult = async { getClientOrderHistoryUseCase(userId) }
                            val masterResult = async { getMasterOrderHistoryUseCase(userId) }
                            val clientList = clientResult.await().getOrNull() ?: emptyList()
                            val masterList = masterResult.await().getOrNull() ?: emptyList()
                            val combined = (
                                    masterList.map {
                                        MasterItem(it)
                                    }
                                            + clientList.map {
                                        ClientItem(it)
                                    }
                                    ).sortedBy { it.createdAt }
                            _state.value =
                                Success(notifications = combined)
                        }
                    } catch (e: Exception) {
                        _state.value = Error(message = "Ошибка запроса")

                    }

                }
            }

            is NotificationIntent.ShowDetails -> {
                val currentState = _state.value
                if (currentState is Success) {
                    _state.value = currentState.copy(selectedOrder = intent.order)
                }

            }

            NotificationIntent.HideDetails -> {
                val currentState = _state.value
                if (currentState is Success) {
                    _state.value = currentState.copy(selectedOrder = null)
                }
            }

            is NotificationIntent.AcceptOrReject -> {
                viewModelScope.launch {
                    val changeDto = ChangeOrderRequestStatusDTO(
                        orderRequestId = intent.request.data.id,
                        orderStatus = intent.status
                    )
                    acceptOrRejectOrderUseCase(changeDto).onSuccess {
                        handleIntent(NotificationIntent.GetNotifications)
                    }

                }

            }

            is NotificationIntent.ChangeOrderStatus -> {
                viewModelScope.launch {
                    val changeStatusDto = ChangeOrderStateDTO(
                        orderID = intent.order.data.id,
                        state = intent.status
                    )
                    changeOrderStateUseCase(
                        changeStatusDto
                    ).onSuccess {
                        handleIntent(NotificationIntent.GetNotifications)
                    }
                }
            }
        }
    }

}