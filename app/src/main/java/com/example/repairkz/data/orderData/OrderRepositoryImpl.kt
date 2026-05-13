package com.example.repairkz.data.orderData

import android.util.Log
import com.example.repairkz.data.remote.api.OrderApi
import com.example.repairkz.data.remote.dto.order.ChangeOrderRequestStatusDTO
import com.example.repairkz.data.remote.dto.order.ChangeOrderStateDTO
import com.example.repairkz.data.remote.dto.order.ClientHistoryItemDTO
import com.example.repairkz.data.remote.dto.order.MasterHistoryItemDTO
import com.example.repairkz.data.remote.dto.order.OrderRequestDTO
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi
) : OrderRepository {
    override suspend fun getMasterOrderHistory(userId: Long): Result<List<MasterHistoryItemDTO>> {
        return try {
            val response = orderApi.getMasterOrderHistory(userId)
            if(response.isSuccessful) {
                val body = response.body()
                return Result.success(body?: emptyList())
            } else{
                Result.failure(Exception(response.code().toString()))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getClientOrderHistory(userId: Long): Result<List<ClientHistoryItemDTO>> {
        return try {
            val response = orderApi.getClientOrderHistory(userId)
            if(response.isSuccessful) {
                val body = response.body()
                return Result.success(body?: emptyList())
            } else{
                Result.failure(Exception(response.code().toString()))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun createOrderRequest(requestDto: OrderRequestDTO) : Result<Unit>{
        return try {
            val response = orderApi.createOrderRequest(requestDto)
            if(response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.code().toString()))
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun acceptOrRejectOrder(changeDto: ChangeOrderRequestStatusDTO) : Result<Unit>{
        return try {
            val response = orderApi.acceptOrRejectOrder(changeDto)
            if(response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.code().toString()))
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun changeOrderState(stateDto: ChangeOrderStateDTO) : Result<Unit>{
        return try {
            val response = orderApi.changeOrderState(stateDto)
            if(response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception(response.code().toString()))
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}