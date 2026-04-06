package com.example.repairkz.data.orderData

import com.example.repairkz.data.remote.dto.order.ChangeOrderRequestStatusDTO
import com.example.repairkz.data.remote.dto.order.ChangeOrderStateDTO
import com.example.repairkz.data.remote.dto.order.ClientHistoryItemDTO
import com.example.repairkz.data.remote.dto.order.MasterHistoryItemDTO
import com.example.repairkz.data.remote.dto.order.OrderRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderRepository {

    suspend fun getMasterOrderHistory(userId: Long) : Result<List<MasterHistoryItemDTO>>

    suspend fun getClientOrderHistory(userId: Long) : Result<List<ClientHistoryItemDTO>>

    suspend fun createOrderRequest(requestDto: OrderRequestDTO) : Result<Unit>

    suspend fun acceptOrRejectOrder(changeDto: ChangeOrderRequestStatusDTO) : Result<Unit>


    suspend fun changeOrderState(stateDto: ChangeOrderStateDTO) : Result<Unit>
}