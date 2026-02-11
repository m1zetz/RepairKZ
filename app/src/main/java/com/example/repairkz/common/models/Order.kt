package com.example.repairkz.common.models

import com.example.repairkz.common.enums.PaymentMethod
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

