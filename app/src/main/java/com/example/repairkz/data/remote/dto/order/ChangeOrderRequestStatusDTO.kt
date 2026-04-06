package com.example.repairkz.data.remote.dto.order

import com.example.repairkz.common.enums.OrderRequestStatus

data class ChangeOrderRequestStatusDTO(
    val orderRequestId: Long,
    val orderStatus: OrderRequestStatus
)