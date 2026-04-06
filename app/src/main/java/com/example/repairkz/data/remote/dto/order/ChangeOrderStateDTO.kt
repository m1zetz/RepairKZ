package com.example.repairkz.data.remote.dto.order

import com.example.repairkz.common.enums.OrderStatus

data class ChangeOrderStateDTO(
    val orderID: Long,
    val state: OrderStatus = OrderStatus.RUNNING
)