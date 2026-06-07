package com.example.repairkz.ui.features.orderReg

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.repairkz.data.remote.dto.order.OrderRequestDTO
import com.example.repairkz.domain.useCases.order.CreateOrderRequestUseCase
import com.example.repairkz.domain.useCases.userData.GetUserDataUseCase
import com.example.repairkz.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
): BaseViewModel<OrderRegistrationState, OrderRegistrationIntent, OrderRegistrationEffect>() {

    override val initialState = OrderRegistrationState()

    private val masterId = savedStateHandle.get<Long>("masterId")
    private var currentUserId: Long? = null

    init {
        viewModelScope.launch {
            getUserDataUseCase().collect { user ->
                currentUserId = user?.id
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun handleIntent(intent: OrderRegistrationIntent){
        when(intent){
            is OrderRegistrationIntent.ChangeAddress -> {
                setState {
                    copy(
                        clientAddress = intent.address
                    )
                }

            }
            is OrderRegistrationIntent.ChangeDescription -> {
               setState {
                   copy(
                       description = intent.description
                   )
               }

            }
            is OrderRegistrationIntent.ChangeNumber -> {
                setState {
                    copy(
                        clientNumber = intent.number
                    )
                }
            }
            is OrderRegistrationIntent.ChangePrice -> {
                setState {
                    copy(
                        price = intent.price
                    )
                }
            }

            is OrderRegistrationIntent.ChangePaymentMethod -> {
                setState {
                    copy(
                        paymentMethod = intent.method
                    )
                }
            }

            is OrderRegistrationIntent.CreateOrderRequest -> {
                val orderData = _state.value
                viewModelScope.launch {
                    val requestDto = OrderRequestDTO(
                        masterId = masterId,
                        userId = currentUserId,
                        description = orderData.description,
                        clientPhoneNumber = orderData.clientNumber,
                        clientAddress = orderData.clientAddress,
                        orderDate = orderData.date,
                        offeredPrice = orderData.price.toIntOrNull() ?: 0,
                        paymentMethod = orderData.paymentMethod
,                    )
                    createOrderRequestUseCase(requestDto).onSuccess {
                        sendEffect(OrderRegistrationEffect.NavigateBack)
                        Log.d("ORDER", "успешно")
                    }.onFailure {
                        Log.d("ORDER", it.message.toString())
                    }
                }

            }

            is OrderRegistrationIntent.ChangeDate -> {
                val millis = intent.millis
                val localDateTime = Instant.ofEpochMilli(millis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                setState {
                    copy(
                        dateMillis = millis
                    )
                }

            }

            OrderRegistrationIntent.CloseDayPicker -> {
                setState {
                    copy(
                        isDayModalOpen = false
                    )
                }
            }
            OrderRegistrationIntent.OpenDayPicker -> {
                setState {
                    copy(
                        isDayModalOpen = true
                    )
                }
            }

            OrderRegistrationIntent.CloseTimePicker -> {
                setState {
                    copy(
                        isTimeModalOpen = false
                    )
                }
            }
            OrderRegistrationIntent.OpenTimePicker -> {
                setState {
                    copy(
                        isTimeModalOpen = true
                    )
                }
            }

            is OrderRegistrationIntent.ChangeTime -> {
                setState {
                    copy(
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
                setState {
                    copy(
                        date = dateTime
                    )
                }

            }
        }
    }

}