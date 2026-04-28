package com.example.repairkz.ui.features.search.orderReg

import com.example.repairkz.common.enums.PaymentMethod
import java.time.LocalDateTime

data class OrderRegistrationState(
    val isLoading: Boolean = false,
    val error: String = "",
    val isDayModalOpen: Boolean = false,
    val isTimeModalOpen: Boolean = false,
    val description: String = "",
    val clientNumber: String = "",
    val clientAddress: String = "",
    val price : String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.UNDEFINED,
    val date: LocalDateTime? = null,
    val dateMillis: Long? = null,
    val hour: Int? = null,
    val minute: Int? = null,
)

sealed class OrderRegistrationIntent{
    data class ChangeDescription(val description: String):OrderRegistrationIntent()
    data class ChangeNumber(val number: String):OrderRegistrationIntent()
    data class ChangeAddress(val address: String):OrderRegistrationIntent()
    data class ChangePrice(val price: String):OrderRegistrationIntent()
    data class ChangeDate(val millis: Long):OrderRegistrationIntent()
    data class ChangeTime(val hour: Int, val minute: Int):OrderRegistrationIntent()
    object ChangeDateTime:OrderRegistrationIntent()
    object OpenDayPicker :OrderRegistrationIntent()
    object CloseDayPicker :OrderRegistrationIntent()

    object OpenTimePicker :OrderRegistrationIntent()
    object CloseTimePicker :OrderRegistrationIntent()
    data class ChangePaymentMethod(val method: PaymentMethod):OrderRegistrationIntent()
    object CreateOrderRequest:OrderRegistrationIntent()
}

sealed class OrderRegistrationEffects{
    object NavigateBack : OrderRegistrationEffects()
}