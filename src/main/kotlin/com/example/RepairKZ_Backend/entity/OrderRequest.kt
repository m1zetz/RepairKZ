package com.example.RepairKZ_Backend.entity

import com.example.RepairKZ_Backend.common.enums.OrderRequestStatus
import com.example.RepairKZ_Backend.common.enums.PaymentMethod
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "order_requests")
data class OrderRequest(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "user_id")
    val user: User? = null,
    @ManyToOne
    @JoinColumn(name = "master_id")
    val master: Master? = null,
    val description: String? = null,
    val clientPhoneNumber: String? = null,
    val clientAddress: String? = null,
    val orderDate: LocalDateTime? = null,
    val offeredPrice: Int? = null,
    @Enumerated(EnumType.STRING)
    val paymentMethod: PaymentMethod? = null,
    @Enumerated(EnumType.STRING)
    var orderRequestStatus: OrderRequestStatus = OrderRequestStatus.PENDING,
    val createdAt: LocalDateTime? = null,
    )
