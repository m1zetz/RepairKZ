package com.example.repairkz.domain.useCases.order

import com.example.repairkz.data.orderData.OrderRepository
import javax.inject.Inject

class GetMasterOrderHistoryUseCase @Inject constructor(
    private val repository: OrderRepository
) {
    suspend operator fun invoke(userId: Long) = repository.getMasterOrderHistory(userId)
}