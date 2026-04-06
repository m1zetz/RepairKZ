package com.example.repairkz.data.remote.api

import com.example.repairkz.data.remote.dto.order.ChangeOrderRequestStatusDTO
import com.example.repairkz.data.remote.dto.order.ChangeOrderStateDTO
import com.example.repairkz.data.remote.dto.order.ClientHistoryItemDTO
import com.example.repairkz.data.remote.dto.order.MasterHistoryItemDTO
import com.example.repairkz.data.remote.dto.order.OrderRequestDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface OrderApi {

    @GET("api/orders/get-master-order-history/{userid}")
    suspend fun getMasterOrderHistory(@Path("userid") userId: Long) : Response<List<MasterHistoryItemDTO>>

    @GET("api/orders/get-client-order-history/{userid}")
    suspend fun getClientOrderHistory(@Path("userid") userId: Long) : Response<List<ClientHistoryItemDTO>>

    @POST("api/orders/create-order")
    suspend fun createOrderRequest(@Body requestDto: OrderRequestDTO) : Response<Unit>

    @POST("api/orders/accept-reject-order")
    suspend fun acceptOrRejectOrder(@Body changeDto: ChangeOrderRequestStatusDTO) : Response<Unit>

    @POST("api/orders/change-order-state")
    suspend fun changeOrderState(@Body stateDto: ChangeOrderStateDTO) : Response<Unit>

}