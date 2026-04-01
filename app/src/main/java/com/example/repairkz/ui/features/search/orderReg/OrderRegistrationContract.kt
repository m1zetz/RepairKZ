package com.example.repairkz.ui.features.search.orderReg

import com.example.repairkz.common.enums.PaymentMethod
import java.time.LocalDateTime

data class OrderRegistrationState(
    val isLoading: Boolean = false,
    val error: String = "",
    val description: String = "",
    val clientNumber: String = "",
    val clientAddress: String = "",
    val price : String = "",
    val paymentMethod: PaymentMethod = PaymentMethod.UNDEFINED,
    val date: LocalDateTime? = null
)

sealed class OrderRegistrationIntent{
    data class ChangeDescription(val description: String):OrderRegistrationIntent()
    data class ChangeNumber(val number: String):OrderRegistrationIntent()
    data class ChangeAddress(val address: String):OrderRegistrationIntent()
    data class ChangePrice(val price: String):OrderRegistrationIntent()
    data class ChangePaymentMethod(val method: PaymentMethod):OrderRegistrationIntent()
}