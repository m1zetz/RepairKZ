package com.example.RepairKZ_Backend.repository

import com.example.RepairKZ_Backend.entity.Order
import com.example.RepairKZ_Backend.entity.OrderRequest
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, Long> {
    fun findAllByOrderRequest_Master_Id(masterId: Long) : List<Order>
    fun findAllByOrderRequest_User_Id(userId: Long) : List<Order>
    fun findByOrderRequest_Id(orderRequestId: Long) : Order?
}