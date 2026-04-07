package com.example.RepairKZ_Backend.model

import com.example.RepairKZ_Backend.common.enums.MasterSpetializationsEnum
import com.example.RepairKZ_Backend.common.enums.OrderRequestStatus
import com.example.RepairKZ_Backend.common.enums.OrderStatus
import com.example.RepairKZ_Backend.common.enums.OrderType
import com.example.RepairKZ_Backend.common.enums.PaymentMethod
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.LocalDateTime

data class MasterHistoryItemDTO(
    val id: Long,
    val clientFirstName: String,
    val clientLastName: String,
    val type: OrderType,
    val description: String? = null,
    val clientPhoneNumber: String? = null,
    val clientAddress: String? = null,
    val orderDate: String? = null,
    val paymentMethod: PaymentMethod? = null,
    val offeredPrice: Int? = null,
    var orderStatus: OrderStatus? = null,
    var orderRequestStatus: OrderRequestStatus? = null,
    val createdAt: String? = null
)

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

