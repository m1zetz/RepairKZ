package com.example.repairkz.common.models

import java.math.BigDecimal
import java.time.LocalDateTime

data class Order(
    val masterSpecialization: String,
    val masterName: String,
    val time: LocalDateTime,
    val description: String,
    val cost: BigDecimal,
    val paymentMethod: PaymentMethod
)

enum class PaymentMethod {
    CASH,
    CARD
}
