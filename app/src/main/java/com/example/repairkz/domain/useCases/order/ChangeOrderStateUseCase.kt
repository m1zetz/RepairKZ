package com.example.repairkz.domain.useCases.order

import com.example.repairkz.data.orderData.OrderRepository
import com.example.repairkz.data.remote.dto.order.ChangeOrderStateDTO
import javax.inject.Inject

class ChangeOrderStateUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(dto: ChangeOrderStateDTO) = repository.changeOrderState(dto)
}