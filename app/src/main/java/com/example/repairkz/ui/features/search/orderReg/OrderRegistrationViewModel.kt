package com.example.repairkz.ui.features.search.orderReg

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.data.remote.dto.order.OrderRequestDTO
import com.example.repairkz.domain.useCases.order.CreateOrderRequestUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class OrderRegistrationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUserDataUseCase: GetUserDataUseCase,
    private val createOrderRequestUseCase: CreateOrderRequestUseCase
): ViewModel() {

    private val _state = MutableStateFlow(OrderRegistrationState())
    val state = _state.asStateFlow()

    private val _channel = Channel<OrderRegistrationEffects>(Channel.BUFFERED)
    val channel = _channel.receiveAsFlow()

    private val masterId = savedStateHandle.get<Long>("masterId")


    @RequiresApi(Build.VERSION_CODES.O)
    fun handleIntent(intent: OrderRegistrationIntent){
        when(intent){
            is OrderRegistrationIntent.ChangeAddress -> {
                _state.update { state ->
                    state.copy(
                        clientAddress = intent.address
                    )
                }
            }
            is OrderRegistrationIntent.ChangeDescription -> {
                _state.update { state ->
                    state.copy(
                        description = intent.description
                    )
                }

            }
            is OrderRegistrationIntent.ChangeNumber -> {
                _state.update { state ->
                    state.copy(
                        clientNumber = intent.number
                    )
                }
            }
            is OrderRegistrationIntent.ChangePrice -> {
                _state.update { state ->
                    state.copy(
                        price = intent.price
                    )
                }
            }

            is OrderRegistrationIntent.ChangePaymentMethod -> {
                _state.update { state ->
                    state.copy(
                        paymentMethod = intent.method
                    )
                }
            }

            is OrderRegistrationIntent.CreateOrderRequest -> {
                val orderData = _state.value
                viewModelScope.launch {
                    val user = getUserDataUseCase()
                    val requestDto = OrderRequestDTO(
                        userId = user?.id,
                        masterId = masterId,
                        description = orderData.description,
                        clientPhoneNumber = orderData.clientNumber,
                        clientAddress = orderData.clientAddress,
                        orderDate = LocalDateTime.now().toString(),
                        offeredPrice = orderData.price.toIntOrNull() ?: 0,
                        paymentMethod = orderData.paymentMethod
,                    )
                    createOrderRequestUseCase(requestDto).onSuccess {
                        _channel.send(OrderRegistrationEffects.NavigateBack)
                    }
                }

            }
        }
    }

}