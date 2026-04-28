package com.example.repairkz.data.remote.dto.order

import com.example.repairkz.common.enums.OrderRequestStatus
import com.example.repairkz.common.enums.OrderStatus
import com.example.repairkz.common.enums.OrderType
import com.example.repairkz.common.enums.PaymentMethod
import java.time.LocalDateTime

data class MasterHistoryItemDTO(
    val id: Long,
    val clientFirstName: String,
    val clientLastName: String,
    val type: OrderType,
    val description: String? = null,
    val clientPhoneNumber: String? = null,
    val clientAddress: String,
    val paymentMethod: PaymentMethod? = null,
    val orderDate: String? = null,
    val offeredPrice: Int? = null,
    var orderStatus: OrderStatus? = null,
    var orderRequestStatus: OrderRequestStatus? = null,
    val createdAt: String? = null
)
