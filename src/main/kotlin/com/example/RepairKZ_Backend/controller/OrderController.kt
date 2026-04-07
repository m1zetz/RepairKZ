package com.example.RepairKZ_Backend.controller

import com.example.RepairKZ_Backend.common.enums.OrderRequestStatus
import com.example.RepairKZ_Backend.model.ChangeOrderRequestStatusDTO
import com.example.RepairKZ_Backend.model.ChangeOrderStateDTO
import com.example.RepairKZ_Backend.model.ClientHistoryItemDTO

import com.example.RepairKZ_Backend.model.MasterHistoryItemDTO
import com.example.RepairKZ_Backend.model.OrderRequestDTO
import com.example.RepairKZ_Backend.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService
) {
    @GetMapping("/get-master-order-history/{userId}")
    fun getMasterOrderHistory(
        @PathVariable userId: Long
    ): ResponseEntity<List<MasterHistoryItemDTO>>{
        val result = orderService.getMasterOrderHistory(userId)
        return ResponseEntity.ok(result)
    }

    @GetMapping("/get-client-order-history/{userId}")
    fun getClientOrderHistory(
        @PathVariable userId: Long
    ): ResponseEntity<List<ClientHistoryItemDTO>>{
        val result = orderService.getClientOrderHistory(userId)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/create-order")
    fun createOrder(@RequestBody orderRequestDTO: OrderRequestDTO): ResponseEntity<Unit> {
        val result = orderService.createOrder(orderRequestDTO)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/accept-reject-order")
    fun acceptOrRejectOrder(@RequestBody changeDto: ChangeOrderRequestStatusDTO): ResponseEntity<Unit> {
        val result = orderService.changeRequestStatus(changeDto)
        return ResponseEntity.ok(result)
    }

    @PostMapping("/change-order-state")
    fun changeOrderStatus(@RequestBody stateDto: ChangeOrderStateDTO): ResponseEntity<Unit> {
        val result = orderService.changeOrderStatus(stateDto)
        return ResponseEntity.ok(result)
    }


}