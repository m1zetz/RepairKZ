package com.example.RepairKZ_Backend.service

import com.example.RepairKZ_Backend.common.enums.MasterSpetializationsEnum
import com.example.RepairKZ_Backend.common.enums.OrderRequestStatus
import com.example.RepairKZ_Backend.common.enums.OrderStatus
import com.example.RepairKZ_Backend.common.enums.OrderType

import com.example.RepairKZ_Backend.entity.Order
import com.example.RepairKZ_Backend.entity.OrderRequest
import com.example.RepairKZ_Backend.model.ChangeOrderRequestStatusDTO
import com.example.RepairKZ_Backend.model.ChangeOrderStateDTO
import com.example.RepairKZ_Backend.model.ClientHistoryItemDTO

import com.example.RepairKZ_Backend.model.MasterHistoryItemDTO
import com.example.RepairKZ_Backend.model.OrderRequestDTO
import com.example.RepairKZ_Backend.repository.MasterRepository
import com.example.RepairKZ_Backend.repository.OrderRepository
import com.example.RepairKZ_Backend.repository.OrderRequestRepository
import com.example.RepairKZ_Backend.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.util.Assert.state
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime
import java.time.ZoneId

@Service
class OrderService(
    private val userRepository: UserRepository,
    private val masterRepository: MasterRepository,
    private val orderRequestRepository: OrderRequestRepository,
    private val orderRepository: OrderRepository,
) {
    fun createOrder(dto: OrderRequestDTO) {
        val user = userRepository.findByIdOrNull(dto.userId!!) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val master = masterRepository.findByIdOrNull(dto.masterId!!) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val orderRequestEntity = OrderRequest(
            user = user,
            master = master,
            clientPhoneNumber = dto.clientPhoneNumber,
            clientAddress = dto.clientAddress,
            description = dto.description,
            orderDate = dto.orderDate,
            offeredPrice = dto.offeredPrice,
            createdAt = LocalDateTime.now(ZoneId.of("Asia/Almaty")),
            paymentMethod = dto.paymentMethod
        )
        orderRequestRepository.save(
            orderRequestEntity
        )
    }

    @Transactional
    fun changeRequestStatus(changeDto: ChangeOrderRequestStatusDTO) {
        val orderRequest = orderRequestRepository.findByIdOrNull(changeDto.orderRequestId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        orderRequest.apply {
            orderRequestStatus = changeDto.orderStatus
        }
        if (changeDto.orderStatus == OrderRequestStatus.ACCEPTED) {
            val order = Order(
                orderRequest = orderRequest,
                createdAt = LocalDateTime.now(ZoneId.of("Asia/Almaty")),
            )
            orderRepository.save(order)
        }

    }
    @Transactional
    fun changeOrderStatus(dto: ChangeOrderStateDTO) {
        val order = orderRepository.findByOrderRequest_Id(dto.orderID)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        order.apply {
            orderStatus = dto.state
        }
    }

    @Transactional
    fun getMasterOrderHistory(userId: Long) : List<MasterHistoryItemDTO> {
        val master = masterRepository.findByUserId(userId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val masterId = master.id!!
        val orderRequestsList = orderRequestRepository.findAllByMaster_Id(masterId).map { request ->
            MasterHistoryItemDTO(
                id = request.id?:throw ResponseStatusException(HttpStatus.NOT_FOUND),
                clientFirstName = request.user?.firstName ?: "",
                clientLastName = request.user?.lastName ?: "",
                type = OrderType.ORDER_REQUEST,
                description = request.description,
                clientPhoneNumber = request.clientPhoneNumber,
                clientAddress = request.clientAddress,
                orderDate = request.orderDate?.toString(),
                offeredPrice = request.offeredPrice,
                orderStatus = null,
                orderRequestStatus = request.orderRequestStatus,
                createdAt = request.createdAt?.toString(),
                paymentMethod = request.paymentMethod
            )
        }
            val orderList = orderRepository.findAllByOrderRequest_Master_Id(masterId).map { order ->
                val request = order.orderRequest
                MasterHistoryItemDTO(
                    id = request?.id?:throw ResponseStatusException(HttpStatus.NOT_FOUND),
                    clientFirstName = request.user?.firstName ?: "",
                    clientLastName = request.user?.lastName ?: "",
                    type = OrderType.ORDER,
                    description = request.description,
                    clientPhoneNumber = request.clientPhoneNumber,
                    clientAddress = request.clientAddress,
                    orderDate = request.orderDate?.toString(),
                    offeredPrice = request.offeredPrice,
                    orderStatus = order.orderStatus,
                    orderRequestStatus = null,
                    createdAt = request.createdAt?.toString(),
                    paymentMethod = request.paymentMethod
                )
            }
        val fullList = orderRequestsList + orderList
        return fullList
    }

    @Transactional
    fun getClientOrderHistory(userId: Long) : List<ClientHistoryItemDTO>{
        val orderRequestsList = orderRequestRepository.findAllByUser_Id(userId).map { request ->
            ClientHistoryItemDTO(
                id = request.id?:throw ResponseStatusException(HttpStatus.NOT_FOUND),
                masterFirstName = request.master?.user?.firstName ?: "",
                masterLastName = request.master?.user?.lastName ?: "",
                masterSpec = request.master?.masterSpecialization?: MasterSpetializationsEnum.UNKNOWN,
                type = OrderType.ORDER_REQUEST,
                description = request.description,
                clientPhoneNumber = request.clientPhoneNumber,
                clientAddress = request.clientAddress,
                orderDate = request.orderDate?.toString(),
                offeredPrice = request.offeredPrice,
                orderStatus = null,
                orderRequestStatus = request.orderRequestStatus,
                createdAt = request.createdAt?.toString(),
                paymentMethod = request.paymentMethod
            )
        }
        val orderList = orderRepository.findAllByOrderRequest_User_Id(userId).map { order ->
            val request = order.orderRequest
            ClientHistoryItemDTO(
                id = request?.id?:throw ResponseStatusException(HttpStatus.NOT_FOUND),
                masterFirstName = request.master?.user?.firstName ?: "",
                masterLastName = request.master?.user?.lastName ?: "",
                masterSpec = request.master?.masterSpecialization?: MasterSpetializationsEnum.UNKNOWN,
                type = OrderType.ORDER,
                description = request.description,
                clientPhoneNumber = request.clientPhoneNumber,
                clientAddress = request.clientAddress,
                orderDate = request.orderDate?.toString(),
                offeredPrice = request.offeredPrice,
                orderStatus = order.orderStatus,
                orderRequestStatus = null,
                createdAt = request.createdAt?.toString(),
                paymentMethod = request.paymentMethod
            )
        }
        val fullList = orderRequestsList + orderList
        return fullList
    }
}