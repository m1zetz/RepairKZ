package com.example.RepairKZ_Backend.model

import com.example.RepairKZ_Backend.common.enums.OrderRequestStatus
import com.example.RepairKZ_Backend.common.enums.OrderStatus

data class ChangeOrderRequestStatusDTO(
    val orderRequestId: Long,
    val orderStatus: OrderRequestStatus
)
