package com.example.repairkz.data.remote.dto.order

import com.example.repairkz.common.enums.PaymentMethod

data class OrderRequestDTO(
    val userId: Long? = null,
    val masterId: Long? = null,
    val description: String? = null,
    val clientPhoneNumber: String? = null,
    val clientAddress: String? = null,
    val paymentMethod: PaymentMethod = PaymentMethod.UNDEFINED,
    val orderDate: String? = null,
    val offeredPrice: Int? = null,
)