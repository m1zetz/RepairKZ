package com.example.RepairKZ_Backend.model

import com.example.RepairKZ_Backend.common.enums.PaymentMethod
import com.example.RepairKZ_Backend.entity.Master
import com.example.RepairKZ_Backend.entity.User
import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

data class OrderRequestDTO(
    val userId: Long? = null,
    val masterId: Long? = null,
    val description: String? = null,
    val clientPhoneNumber: String? = null,
    val clientAddress: String? = null,
    val orderDate: LocalDateTime? = null,
    val paymentMethod: PaymentMethod = PaymentMethod.UNDEFINED,
    val offeredPrice: Int? = null,
)
