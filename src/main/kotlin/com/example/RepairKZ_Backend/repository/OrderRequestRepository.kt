package com.example.RepairKZ_Backend.repository

import com.example.RepairKZ_Backend.entity.OrderRequest
import com.example.RepairKZ_Backend.model.OrderRequestDTO
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRequestRepository : JpaRepository<OrderRequest, Long> {
    fun findAllByMaster_Id(masterId: Long): List<OrderRequest>
    fun findAllByUser_Id(userId: Long): List<OrderRequest>
}