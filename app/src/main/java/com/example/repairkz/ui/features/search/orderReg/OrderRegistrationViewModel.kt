package com.example.repairkz.ui.features.search.orderReg

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.repairkz.common.utils.formattedTime
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
import kotlinx.datetime.LocalTime
import java.time.Instant

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
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
                        orderDate = orderData.date,
                        offeredPrice = orderData.price.toIntOrNull() ?: 0,
                        paymentMethod = orderData.paymentMethod
,                    )
                    createOrderRequestUseCase(requestDto).onSuccess {
                        _channel.send(OrderRegistrationEffects.NavigateBack)
                    }
                }

            }

            is OrderRegistrationIntent.ChangeDate -> {
                val millis = intent.millis
                val localDateTime = Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                _state.update { state ->
                    state.copy(
                        dateMillis = millis
                    )
                }
            }

            OrderRegistrationIntent.CloseDayPicker -> {
                _state.update {state -> 
                    state.copy(
                        isDayModalOpen = false
                    )
                }
            }
            OrderRegistrationIntent.OpenDayPicker -> {
                _state.update {state ->
                    state.copy(
                        isDayModalOpen = true
                    )
                }
            }

            OrderRegistrationIntent.CloseTimePicker -> {
                _state.update {state ->
                    state.copy(
                        isTimeModalOpen = false
                    )
                }
            }
            OrderRegistrationIntent.OpenTimePicker -> {
                _state.update {state ->
                    state.copy(
                        isTimeModalOpen = true
                    )
                }
            }

            is OrderRegistrationIntent.ChangeTime -> {
                _state.update {
                    it.copy(
                        hour = intent.hour,
                        minute = intent.minute
                    )
                }
            }

            OrderRegistrationIntent.ChangeDateTime -> {
                val state = _state.value
                val hour = state.hour ?: return
                val minute = state.minute ?: return
                val dateMillis = state.dateMillis ?: return

                val date = LocalDateTime.ofEpochSecond(dateMillis/1000,0,
                    ZoneOffset.UTC).toLocalDate()
                val dateTime = LocalDateTime.of(date, java.time.LocalTime.of(hour,minute))
                _state.update {
                    it.copy(
                        date = dateTime
                    )
                }

            }
        }
    }

}