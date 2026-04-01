package com.example.repairkz.ui.features.search.orderReg

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class OrderRegistrationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _state = MutableStateFlow(OrderRegistrationState())
    val state = _state.asStateFlow()

    private val masterId = savedStateHandle.get<Long>("masterId")

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
        }
    }

}