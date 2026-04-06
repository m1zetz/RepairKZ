package com.example.repairkz.data.remote.dto.order

import com.example.repairkz.common.enums.MasterSpetializationsEnum
import com.example.repairkz.common.enums.OrderRequestStatus
import com.example.repairkz.common.enums.OrderStatus
import com.example.repairkz.common.enums.OrderType
import com.example.repairkz.common.enums.PaymentMethod
import java.time.LocalDateTime

data class ClientHistoryItemDTO(
    val id: Long,
    val masterFirstName: String,
    val masterLastName: String,
    val masterSpec: MasterSpetializationsEnum = MasterSpetializationsEnum.UNKNOWN,
    val type: OrderType,
    val description: String? = null,
    val clientPhoneNumber: String? = null,
    val clientAddress: String? = null,
    val paymentMethod: PaymentMethod? = null,
    val orderDate: String? = null,
    val offeredPrice: Int? = null,
    var orderStatus: OrderStatus? = null,
    var orderRequestStatus: OrderRequestStatus? = null,
    val createdAt: String? = null
)
