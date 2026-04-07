package com.example.RepairKZ_Backend.model

import com.example.RepairKZ_Backend.common.enums.OrderStatus
import com.example.RepairKZ_Backend.entity.OrderRequest

data class ChangeOrderStateDTO(
    val orderID: Long,
    val state: OrderStatus = OrderStatus.RUNNING
)
