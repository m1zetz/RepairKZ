package com.example.RepairKZ_Backend.entity

import com.example.RepairKZ_Backend.common.enums.OrderStatus
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @OneToOne
    @JoinColumn(name = "order_request_id")
    val orderRequest: OrderRequest? = null,
    @Enumerated(EnumType.STRING)
    var orderStatus: OrderStatus? = OrderStatus.RUNNING,
    val createdAt: LocalDateTime? = null
)

