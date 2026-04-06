package com.example.repairkz.domain.useCases.order

import com.example.repairkz.data.orderData.OrderRepository
import com.example.repairkz.data.remote.dto.order.ChangeOrderRequestStatusDTO
import javax.inject.Inject

class AcceptOrRejectOrderUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(dto: ChangeOrderRequestStatusDTO) = repository.acceptOrRejectOrder(dto)
}